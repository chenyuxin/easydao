package com.wondersgroup.commonutil.type;

import java.util.HashMap;
import java.util.Map;

import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.commonutil.type.database.FieldType;
import com.wondersgroup.commonutil.type.format.DateType;
import com.wondersgroup.commonutil.type.language.JSONType;
import com.wondersgroup.commonutil.type.language.XMLType;

/**
 * 基本类型的缓存和初始化
 * 程序启动的时候需要初始化CommonTypeCaches处的初始化方法 init并传入默认数据库类型
 */
public class CommonTypeCaches {
	
	/**
	 * 执行初始化方法
	 */
	private static Boolean isInit = false;
	
	/**
	 * 执行初始化方法
	 */
	public static void init(DataBaseType defaultDataBaseType) {
		synchronized (isInit) {
			if (isInit) {
				return;
			}
			
			//第一步  设置默认数据源的数据库类型
			DataBaseType.setCurrentDataBaseType(defaultDataBaseType);
			
			
			//第二步  单例子类对象放入工厂map,方便通过类型名获取 通用类型
			putInstanceMap();

			
			isInit = true;
		}
	}		
	
	/**
	 * 将单例子类对象放入工厂map,方便通过类型名获取 通用类型
	 */
	protected static Map<String, CommonType> instanceMap = new HashMap<String, CommonType>();
	
	/**
	 * 单例子类对象放入工厂map,方便通过类型名获取 通用类型
	 */
	protected static void putInstanceMap(){
		if (CommonTypeCaches.instanceMap.isEmpty()){
			synchronized (CommonTypeCaches.instanceMap) {
				if(CommonTypeCaches.instanceMap.isEmpty()) {
					for (CommonType dataBaseType : DataBaseType.values()){
						instanceMap.put(dataBaseType.getTypeName(), dataBaseType);
					}
					for (CommonType fieldType : FieldType.values()) {
						instanceMap.put(fieldType.getTypeName(), fieldType);
					}
					instanceMap.put(JSONType.typeName, JSONType.getInstance());
					instanceMap.put(XMLType.typeName, XMLType.getInstance());
					instanceMap.put(DateType.typeName, DateType.getInstance());
					//后续有增加需要自行放入
				}
			}
		}
	}

}
