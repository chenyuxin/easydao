package com.wondersgroup.common.spring.util.thread;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.wondersgroup.commonutil.constant.StringMeanPool;
import com.wondersgroup.commonutil.constant.StringPool;

/**
 * 多线程数据事务
 * 的一支线程<br>
 * 此线程类确保多线程事务
 * @param <T>
 */
public class AsyncTransactionThread extends Thread {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AsyncTransactionThread.class);
	
	/**
	 * 事务当前会话ID，由总事务统一传入
	 */
	private String transactionSessionId;
	
	/**
	 * 某一支数据源的事务，对应到本事务线程
	 */
	private DataSourceTransactionManager transactionManager;
	
	/**
	 * 事务启动时间，由总事务统一传入
	 * 用于超时判断System.currentTimeMillis()
	 */
	//private long startTransactionTime;
	
	/**
	 * 事务超时时间
	 * <br>默认1天
	 */
	private long timeOut = 86400000L;//1天
//	private long timeOut = 10000L;//test
	/**
	 * 事务开启最后截至时间
	 */
	private long lastEndTransactionTime;
	
	/**
	 * 事务是否只读
	 */
	private boolean readOnly = false;
	
	/**
	 * 本线程执行任务队列
	 */
	private final Queue<Supplier<?>> executeQueue = new LinkedBlockingDeque<Supplier<?>>();
	
	/**
	 * 错误消息<br>
	 * 本线程出现错误时,捕获消息放入此处,让总事务监测到错误,进行回滚
	 */
	private String errorMessage = StringPool.BLANK;
	
	/**
	 * 一般信息<br>
	 * 本线程将本次执行步骤结果覆盖,<br>
	 * 让总事务监测到执行结果已经出来。
	 */
	private Object infoMessage = StringPool.BLANK;
	
	/**
	 * 获取transactionSessionId
	 * @return
	 */
	public String getTransactionSessionId() {
		return transactionSessionId;
	}
		
	
	
	AsyncTransactionThread () {}
	
	/**
	 * 某一数据源的事务管理器，对应到本事务线程
	 * @param transactionManager 某一支数据源的事务，对应到本事务线程
	 * @param startTransactionTime 事务启动时间，由总事务同一传入
	 * @param transactionSessionId 事务当前会话ID，由总事务统一传入
	 */
	public AsyncTransactionThread ( DataSourceTransactionManager transactionManager, long startTransactionTime, String transactionSessionId, boolean readOnly) {
		this.transactionManager = transactionManager;
		//this.startTransactionTime = startTransactionTime;
		this.lastEndTransactionTime = timeOut + startTransactionTime;
		this.transactionSessionId = transactionSessionId;
		this.readOnly = readOnly;
	}
	
	/**
	 * 重设,然后可以新放入某一数据源的事务管理器，对应到本事务线程
	 * @param transactionManager 某一支数据源的事务，对应到本事务线程
	 * @param startTransactionTime 事务启动时间，由总事务统一传入
	 * @param transactionSessionId 事务当前会话ID，由总事务统一传入
	 * @return 是否重设完成，正在执行不能重设
	 */
	public boolean resetAsyncTransactionThread ( DataSourceTransactionManager transactionManager, long startTransactionTime, String transactionSessionId,boolean readOnly) {
		if (this.isAlive()) {
			//throw new RuntimeException("线程正在执行任务,不能重新设置");
			return false;
		}
		this.transactionManager = transactionManager;
		//this.startTransactionTime = startTransactionTime;
		this.lastEndTransactionTime = timeOut + startTransactionTime;
		this.transactionSessionId = transactionSessionId;
		this.readOnly = readOnly;
		return true;
	}
	
	public boolean clean() {
		if (this.isAlive()) {
			//throw new RuntimeException("线程正在执行任务,不能重新设置");
			return false;
		}
		this.transactionSessionId = null;
		this.transactionManager = null;
		//this.lastEndTransactionTime = 0;
		return true;
	}
	
	/**
	 * 错误消息<br>
	 * 本线程出现错误时,捕获消息放入此处,让总事务监测到错误,进行回滚
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * 一般信息<br>
	 * 本线程将本次执行步骤结果覆盖,<br>
	 * 让总事务监测到执行结果已经出来。
	 */
	public Object getInfoMessage() {
		return this.infoMessage;
	}
	
	public void cleanInfoMessage() {
		this.infoMessage = StringPool.BLANK;
	}



	/**
	 * 事务执行队列<br>
	 * 执行事务方法:<br>
	 * supplier.get()执行事务业务<br>
	 * 终结事务的消息传入:<br>
	 * StringMeanPool.COMMIT提交事务<br>
	 * StringMeanPool.ROLLBACK回滚事务<br>
	 * @return
	 */
	public Queue<Supplier<?>> getExecuteQueue() {
		return this.executeQueue;
	}
	
	

	/**
	 * 事务执行队列<br>
	 * 执行事务方法消息传入:<br>
	 * supplier.get()执行事务业务<br>
	 * 终结事务的消息传入:<br>
	 * StringMeanPool.COMMIT提交事务<br>
	 * StringMeanPool.ROLLBACK回滚事务<br>
	 * @return 是否放入执行队列
	 */
	public boolean offer(Supplier<?> supplier) {
		return this.executeQueue.offer(supplier);
	}
	
	@Override
	public void run() {
		DefaultTransactionDefinition transDef = new DefaultTransactionDefinition(); // 定义事务属性
	    transDef.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED); // 设置传播行为属性
	    transDef.setReadOnly(this.readOnly);//是否只读事务
	    TransactionStatus transactionStatus = this.transactionManager.getTransaction(transDef); // 获得事务状态
		
		while (true){
			Supplier<?> executeSomeThing = this.executeQueue.poll();
			if (null == executeSomeThing) {
				
				if( this.lastEndTransactionTime < System.currentTimeMillis() ) {//检测事务超时
					this.transactionManager.rollback(transactionStatus);
					return;//结束本线程事务
				}
				
			} else if (executeSomeThing instanceof Supplier) {
				Object r = StringPool.BLANK;
				try {
					r = ((Supplier<?>) executeSomeThing).get();//执行事务业务
					log.debug("执行事务返回消息:" + r.toString());
					this.infoMessage = r;
				} catch (Exception e) {
					this.errorMessage = e.getMessage();
					log.error(e.getMessage());
					this.transactionManager.rollback(transactionStatus);
					return;//结束本线程事务
				}
				
				if (StringMeanPool.COMMIT.equals(r)) {
					try {
						this.transactionManager.commit(transactionStatus);
						//log.debug("支线事务提交");
					} catch (Exception e) {
						if (transactionStatus.isCompleted()) return;
						this.transactionManager.commit(transactionStatus);//重试一次
					}//TODO 是否有提交失败的可能,重试失败如何处理
					return;//结束本线程事务
				} else if (StringMeanPool.ROLLBACK.equals(r)) {
					this.transactionManager.rollback(transactionStatus);
					return;//结束本线程事务
				} 
				
			}
			
			try {
				Thread.sleep(0);//等一等，睡一睡，生活更健康
			} catch (Exception e) {
			}
			
		}
		
	}


	

	

}
