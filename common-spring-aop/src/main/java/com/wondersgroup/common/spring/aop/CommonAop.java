package com.wondersgroup.common.spring.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wondersgroup.common.spring.aop.util.CommonAopFeatures;

/**
 * 通用AOP
 * @author chenyuxin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommonAop {
	
	/**
	 * 切入者类型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends CommonAopDoSomeThing> cuterClass();
	
	/**
	 * 特性配置CommonAopFeatures
	 * @return
	 */
	Class<? extends CommonAopFeatures>[] commonAopFeatures() default {};

}
