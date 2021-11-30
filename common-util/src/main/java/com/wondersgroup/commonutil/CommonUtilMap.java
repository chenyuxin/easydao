package com.wondersgroup.commonutil;

import java.util.Map;

import javax.annotation.Nonnull;

import com.wondersgroup.commonutil.type.database.FieldType;

/**
 * map操作相关工具类
 *
 */
public class CommonUtilMap {
	
	/**
	 * 获取map里的value
	 * @param <T>
	 * @param clazz 取值类型
	 * @param key 获取值的key
	 * @param map 获取值的map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueOfMap(@Nonnull Class<T> clazz, String key, Map<String, Object> map ) {
		Object value =  map.get(key);
		if (null == value) {
			return null;
		} else {
			FieldType fieldType = FieldType.getFieldType(clazz);
			if (null == fieldType) {
				return (T) value;//非基本数据类型直接转对象
			} else {
				return (T) fieldType.parse(value);//转成对应基本数据类型
			}
		}
		
	}
	
	/**
	 * 获取map里的value,为null时或获取时异常则默认一个值
	 * @param <T>
	 * @param defaultResult 不能取正常的值时默认一个值
	 * @param key 获取值的key
	 * @param map 获取值的map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueOfMap(@Nonnull T defaultResult, String key, Map<String, Object> map) {
		try {
			Object value =  map.get(key);
			if (null == value) {
				return defaultResult;
			} else {
				FieldType fieldType = FieldType.getFieldType(defaultResult.getClass());
				if (null == fieldType) {
					return (T) value;//非基本数据类型直接转对象
				} else {
					return (T) fieldType.parse(value);//转成对应基本数据类型
				}
			}
		} catch (Exception e){
			return defaultResult;
		}
	}
	
	

}
