package com.wondersgroup.common.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnnotationAspect {
	
	@Autowired ApplicationContext applicationContext;
	
	/**
	 * 切点 通过@CommonAop注解的方法
	 */
	@Pointcut("@annotation(com.wondersgroup.common.spring.aop.CommonAop)")
	private void pointCutCommonAop(){}
	
	@SuppressWarnings("unchecked")
	@Around("@annotation(commonAop)")
	public <R> Object process(ProceedingJoinPoint point,CommonAop commonAop) throws Throwable {
		CommonAopDoSomeThing<R> commonAopDoSomeThing;
		R beforeResult;
		try {
			commonAopDoSomeThing = applicationContext.getBean(commonAop.cuterClass());//TODO 多个CommonAopDoSomeThing的功能添加
			beforeResult = commonAopDoSomeThing.before(point, commonAop);
		} catch (Throwable e) {
			throw e;
		}
		
//		System.out.println("CommonAop welcome");
//      System.out.println("CommonAop 切入者："+ commonAop.cuterClass().getSimpleName());
//      System.out.println("CommonAop 调用类：" + point.getSignature().getDeclaringTypeName());
//      System.out.println("CommonAop 调用类名" + point.getSignature().getDeclaringType().getSimpleName());
		Object obj = null;
    	try {
			obj = point.proceed();//调用目标方法
		} catch (Throwable e) {
			commonAopDoSomeThing.throwable(point, commonAop, beforeResult,e);
			//System.out.println("CommonAop error:" + e.getMessage());
			throw e;
		} 
    	
    	commonAopDoSomeThing.after(point, commonAop, beforeResult);
    	
    	return obj;
	}
	
	
	

}
