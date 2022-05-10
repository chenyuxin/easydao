package com.wondersgroup.common.spring.util.container;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.wondersgroup.common.spring.util.feature.CommonTransactionFeature;
import com.wondersgroup.common.spring.util.thread.AsyncTransactionThread;
import com.wondersgroup.commondao.dao.daoutil.DaoUtil;
import com.wondersgroup.commonutil.baseutil.BaseUtil;
import com.wondersgroup.commonutil.constant.StringMeanPool;
import com.wondersgroup.commonutil.constant.StringPool;

/**
 * 多事务管理,总事务
 */
public class TotalTransactionManager {
	
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TotalTransactionManager.class);
	
	private String transactionSessionId;//事务当前会话ID
	
	private long startTransactionTime;//事务启动时间
	
	private String[] dataSourceBeanNames;//数据源的beanNames
	
	//多个TransactionManager使用的线程集合
	private Map<String, AsyncTransactionThread> transactionThreadsMap;
	
	private StringBuffer errorMessage = new StringBuffer();//用于记录事务的消息
	
	private boolean readOnly = false;//只读事务
	
	TotalTransactionManager(){}
	
	/**
	 * 创建多数据源的总事务管理<br>
	 * 与注解 @CommonAop(cuterClass = MultipleManagerAsyncTransaction.class) 配合使用<br>
	 * 可选AOP配置commonAopFeatures = {CommonTransactionFeature.ReadOnly.class}<br>
	 * @param dataSourceBeanNames 数据源的beanNames
	 */
	public TotalTransactionManager(String... dataSourceBeanNames) {
		if (null == dataSourceBeanNames) {
			throw new RuntimeException("CommonAop中定义的MultipleManagerTransaction管理的方法必须传有TotalTransactionManager总事务管理和包含他的数据源String数组参数");
		}
		this.dataSourceBeanNames = dataSourceBeanNames;
		this.transactionThreadsMap = new ConcurrentHashMap<String, AsyncTransactionThread>(dataSourceBeanNames.length);
		this.transactionSessionId = BaseUtil.toStringUUID64(UUID.randomUUID());
	}

	/**
	 * 事务会话id
	 * @return
	 */
	public String getTransactionSessionId() {
		return transactionSessionId;
	}

	/**
	 * 事务开启时间
	 * @return
	 */
	public long getStartTransactionTime() {
		return startTransactionTime;
	}

	public Map<String, AsyncTransactionThread> getTransactionThreadsMap() {
		return transactionThreadsMap;
	}
	
	/**
	 * 开启总事务<br>
	 * 以下创建的事务不在同一线程<br>
	 * 需要对应数据源开启事务的线程<br>
	 * @param applicationContext spring上下文获取各个支线事务管理
	 * @param 事务特性
	 */
	public void startTransaction(ApplicationContext applicationContext,Class<?>... features) {
		synchronized (startTransaction) {
			if(startTransaction) {
				return;
			} else {
				startTransaction = true;
			}
		}
		
		this.startTransactionTime = System.currentTimeMillis();//事务启动时的时间
		
		for(Class<?> feature : features) {//遍历配置
			if ( CommonTransactionFeature.ReadOnly.class == feature ) {
				this.readOnly = true;//开启只读
			}
		}
		
		Arrays.stream(this.dataSourceBeanNames).parallel().forEach(dataSourceBeanName -> {
			DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) applicationContext.getBean(dataSourceBeanName + DaoUtil.TransactionManager);
			AsyncTransactionThread asyncTransactionThread = new AsyncTransactionThread(transactionManager, this.startTransactionTime, this.transactionSessionId,this.readOnly);
			this.transactionThreadsMap.put(dataSourceBeanName,asyncTransactionThread);
			asyncTransactionThread.start();
		});
		
	}
	private Boolean startTransaction = false;//只开启一次总事务
	
	public void commit() {
		//log.debug("-------------总事务提交---------------");
		if (this.readOnly) {
			this.rollback();
		} else {
			this.transactionThreadsMap.values().parallelStream().forEach(
				asyncTransactionThread -> {
					asyncTransactionThread.offer( ()-> {return StringMeanPool.COMMIT;} );	
					try {
						asyncTransactionThread.join();//等待所有事务提交完成
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			);
		}
		
	}
	
	public void rollback() {
		//log.debug("-------------总事务rollback---------------");
		this.transactionThreadsMap.values().parallelStream().forEach(
			asyncTransactionThread -> {
			if (asyncTransactionThread.isAlive()) {
				asyncTransactionThread.offer( () -> {return StringMeanPool.ROLLBACK;} );
			}
		});		
	}
	
	/**
	 * 总线程中的
	 * 全部分支执行任务,使用方法:<br>
	 * totalTransactionManager.execute(dataSourceBeanName -> {																				<br>
	 * 		//todosomething 自定义执行逻辑                                                   		    													<br>
	 *		String r = commonDao.saveOrUpdateObj(testPo,dataSourceBeanName,DaoEnumOptions.RuntimeException); //当有异常时抛出异常让总事务回滚 <br>
	 *		return r;  //返回执行消息或数据查询结果             																										<br>
	 * });                                                                         															<br>
	 * @param <T>
	 * @param function 入参固定为循环遍历分支dataSrouceBeanName,返回为对应的线程操作类的结果.
	 */
	public String execute(Function<String,Object> function) {
		//总事务管理检测各个支线事务是否有异常,有就回滚
		if(this.errorMessage.length() == 0) {
			Map<String, String> map1 = new ConcurrentHashMap<String, String>();
			this.transactionThreadsMap.forEach((dataSourceBeanName,asyncTransactionThread) -> {
				map1.put(dataSourceBeanName, dataSourceBeanName);
				
				//检查每个线程的执行情况，执行完成再继续，等待执行完成没有任何报错。
				asyncTransactionThread.cleanInfoMessage();//清空执行消息，为了本次执行使用
				
			});
			Set<String> executeTransactionDataSourceBeanNames = map1.keySet();
			
			this.transactionThreadsMap.entrySet().parallelStream().forEach(
				entrySet -> {
					String dataSourceBeanName = entrySet.getKey();
					AsyncTransactionThread asyncTransactionThread = entrySet.getValue();
					
					//开始执行事务,放入分支事务的执行队列
					asyncTransactionThread.offer(
						() -> {
							return function.apply(dataSourceBeanName);
							//return StringMeanPool.SUCCESS_MESSAGE;//返回非空的值
						}
					);
					
				}
			);
			
			this.checkBranchInfo(executeTransactionDataSourceBeanNames);//检查分支事务完成情况，完成的移除
			//log.debug("执行完成executeTransactionDataSourceBeanNames完成了的情况：" + executeTransactionDataSourceBeanNames.size());
			//log.debug("执行完成transactionThreadsMap的情况：" + this.transactionThreadsMap.size());
			if (this.errorMessage.length() > 0) {
				this.rollback();
				return this.errorMessage.toString();
			} else {
				return StringMeanPool.SUCCESS_MESSAGE;
			}
			
		} else {
			this.rollback();
			return this.errorMessage.toString();
		}
		
		
	}
	

	/**
	 * 检查分支执行任务返回信息<br>
	 * 完成一项移除一项，直到全部完成
	 * @param executeTransactionDataSourceBeanNames
	 */
	private void checkBranchInfo(Set<String> executeTransactionDataSourceBeanNames) {
		executeTransactionDataSourceBeanNames.parallelStream().forEach(
			dataSourceBeanName -> {
				//log.debug(dataSourceBeanName + "-------------checkBranchInfo---------------");
				AsyncTransactionThread asyncTransactionThread = this.transactionThreadsMap.get(dataSourceBeanName);
				while(StringPool.BLANK.equals(asyncTransactionThread.getInfoMessage())) {
					Thread.yield();
					if( asyncTransactionThread.getErrorMessage().length() > 0 ) {
						if (this.errorMessage.indexOf(asyncTransactionThread.getErrorMessage()) == -1) {//相同错误消息合并
							this.errorMessage.append(asyncTransactionThread.getErrorMessage());
						}
						log.debug("-------------有了errorMessage---------------");
						break;
					}
				}
				executeTransactionDataSourceBeanNames.remove(dataSourceBeanName);
			}	
		);
	}

	/**
	 * 总事务管理检测正在执行任务的各个支线事务是否有异常,
	 * 以便发现异常进行全部回滚
	 */
	public StringBuffer checkBranchError() {
		if (this.errorMessage.length() == 0) {
			this.transactionThreadsMap.entrySet().parallelStream().forEach(
					entrySet -> {
					//String dataSourceBeanName = entrySet.getKey();
					AsyncTransactionThread asyncTransactionThread = entrySet.getValue();
					//log.debug("-------------checkBranchError---------------");
					if(!StringPool.BLANK.equals(asyncTransactionThread.getErrorMessage())) {
						if (this.errorMessage.indexOf(asyncTransactionThread.getErrorMessage()) == -1) {//相同错误消息合并
							this.errorMessage.append(asyncTransactionThread.getErrorMessage());
						}
					}	
				}	
			);	
		}
		return this.errorMessage;
	}
	

}
