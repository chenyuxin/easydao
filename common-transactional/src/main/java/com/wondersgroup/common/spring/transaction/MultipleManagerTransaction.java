package com.wondersgroup.common.spring.transaction;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.wondersgroup.common.spring.aop.CommonAop;
import com.wondersgroup.common.spring.aop.CommonAopDoSomeThing;
import com.wondersgroup.commondao.dao.daoutil.DaoUtil;

/**
 * 多事务处理，业务逻辑:(全部执行成功才提交事务，有一个报错全部回滚)
 * 替换Spring的@Transaction注解的方法，（原因是@Transaction只能处理单个事务）
 * 对应切点方法必须传入参数dataSourceBeanNames事务源的beanName数组</br>
 * 切点方法必须是单线程执行事务
 * @author chenyuxin
 * @deprecated 不能保证多数据源在同一线程启动事务管理从而导致不能正确提交回滚事务.<br>请移步使用MultipleManagerAsyncTransaction
 */
//@Component
@Deprecated
public class MultipleManagerTransaction implements CommonAopDoSomeThing<Map<DataSourceTransactionManager,TransactionStatus>> {
	
	@Autowired ApplicationContext applicationContext;

	@Override
	public Map<DataSourceTransactionManager,TransactionStatus> before(ProceedingJoinPoint point, CommonAop commonAop) {
		String[] dataSourceBeanNames = null;
		for(Object object : point.getArgs()) {//获取切点方法的传入参数dataSourceBeanNames
			if ( object instanceof String[] ) {
				dataSourceBeanNames = (String[]) object;
			}
 		}
		if (null == dataSourceBeanNames) {
			throw new RuntimeException("CommonAop中定义的MultipleManagerTransaction管理的方法必须传有数据源String数组参数");
		}
		
		Map<DataSourceTransactionManager,TransactionStatus> transactionStatusMap = new HashMap<DataSourceTransactionManager,TransactionStatus>(dataSourceBeanNames.length);
		
		for(String dataSourceBeanName : dataSourceBeanNames) {//串行执行，确保每个事务开启在本线程中
			//System.out.println(dataSourceBeanName + "TransactionManager");
			DefaultTransactionDefinition transDef = new DefaultTransactionDefinition(); // 定义事务属性
		    transDef.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED); // 设置传播行为属性
		    DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) applicationContext.getBean(dataSourceBeanName + DaoUtil.TransactionManager);
		    TransactionStatus status = transactionManager.getTransaction(transDef); // 获得事务状态,注意线程
			transactionStatusMap.put(transactionManager, status);
		}
		
		return transactionStatusMap;
	}

	@Override
	public void after(ProceedingJoinPoint point, CommonAop commonAop, Map<DataSourceTransactionManager,TransactionStatus> status) {
		//System.out.println("MultipleManagerTransaction after");
		status.forEach((k,s) -> k.commit(s));//全部提交
	}

	@Override
	public void throwable(ProceedingJoinPoint point, CommonAop commonAop, Map<DataSourceTransactionManager,TransactionStatus> status, Throwable e) {
		//System.out.println("MultipleManagerTransaction Throwable");
		status.forEach((k,s) -> k.rollback(s));//全部回滚
	}

}
