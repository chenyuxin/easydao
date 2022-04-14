package com.wondersgroup.common.spring.util.feature;

import com.wondersgroup.common.spring.aop.util.CommonAopFeatures;

/**
 * 多源事务的特性配置标记
 * @author chenyuxin
 */
public interface CommonTransactionFeature extends CommonAopFeatures {
	
	/**
	 * 只读,totalTransactionManager对象识别
	 */
	public static class ReadOnly implements CommonAopFeatures{}

}
