package com.wondersgroup.commonutil.type;

/**
 * 通用类型传参,在本工具框架中的全局配置类型
 * 传入配置好的本实例告知通用工具类的方法如何操作
 */
public interface CommonType {
	
	/**
	 * 类型名
	 */
	public String getTypeName();
	
	/**
	 * 通过类型名获取 通用类型
	 * @param typeName 类型名
	 * @return
	 */
	public static CommonType getType(String typeName) {
		CommonTypeCaches.putInstanceMap();
		return CommonTypeCaches.instanceMap.get(typeName);
	}
	
}
