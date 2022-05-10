package com.wondersgroup.common.spring.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.wondersgroup.common.spring.aop.CommonAop;
import com.wondersgroup.common.spring.aop.CommonAopDoSomeThing;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;

/**
 * 多数据源事务开启<br>
 * 多事务处理，业务逻辑:(全部执行成功才提交事务，有一个报错全部回滚)<br>
 * 另补充增强Spring的@Transaction注解的方法，（原因是@Transaction只能处理单个事务）<br>
 * 多线程处理,切入的方法必须要添加 new TotalTransactionManager(String... dataSourceBeanNames) 作为入参
 * @author chenyuxin
 */
@Component
public class MultipleManagerAsyncTransaction implements CommonAopDoSomeThing<TotalTransactionManager> {
	
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MultipleManagerAsyncTransaction.class);


	@Autowired ApplicationContext applicationContext;
	
	@Override
	public TotalTransactionManager before(ProceedingJoinPoint point, CommonAop commonAop) {
		TotalTransactionManager totalTransactionManager = null;
		for(Object object : point.getArgs()) {//获取切点方法的传入参数
			if ( object instanceof TotalTransactionManager ) {
				totalTransactionManager = (TotalTransactionManager) object;
				break;
			}
		}
		if (null == totalTransactionManager) {
			throw new RuntimeException("CommonAop中定义的MultipleManagerTransaction管理的方法必须传有TotalTransactionManager总事务管理和包含他的数据源String数组参数");
		}
		
		totalTransactionManager.startTransaction(applicationContext,commonAop.commonAopFeatures());
	    
		//System.out.println("MultipleManagerTransaction before");
		return totalTransactionManager;
	}

	@Override
	public void after(ProceedingJoinPoint point, CommonAop commonAop, TotalTransactionManager ttm) {
		//总事务管理检测各个支线事务是否有异常,有就回滚
		if(ttm.checkBranchError().length() == 0) {
			ttm.commit();//全部提交,阻塞等待提交完成事务终结
		} else {
			ttm.rollback();//全部回滚
		}
		
		
	}

	@Override
	public void throwable(ProceedingJoinPoint point, CommonAop commonAop, TotalTransactionManager ttm, Throwable e) {
		log.error(e.getMessage());
		//e.printStackTrace();
		ttm.rollback();//全部回滚
	}
	
}

/**
//开启多线程
Stream<String> dataSourceBeanNamesStream = Arrays.stream(dataSourceBeanNames);
dataSourceBeanNamesStream = dataSourceBeanNamesStream.parallel();
Map<String, AsyncTransactionThread> transactionMap = dataSourceBeanNamesStream.collect(Collectors.toConcurrentMap(
		dataSourceBeanName -> dataSourceBeanName, 
		dataSourceBeanName -> {
			DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) applicationContext.getBean(dataSourceBeanName + DaoUtil.TransactionManager);
	    	AsyncTransactionThread asyncTransactionThread = new AsyncTransactionThread(transactionManager, startTransactionTime, sessionId);
	    	asyncTransactionThread.start();//开启总事务的分支线程
			return asyncTransactionThread;
		}
));
*/
