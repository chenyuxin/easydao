package com.wondersgroup.commonutil.type.otherintf;

/**
 *提供解析方法 
 *
 */
public interface TypeParse<T> {
	
	/**
	 * 解析为对应的类型
	 * @param value
	 * @return
	 */
	T parse(Object value);
	
	/**
	 * 解析为对应的类型
	 * @param value
	 * @param formatString 自定义解析格式
	 * @return
	T parse(Object value,String formatString);
	 */

}
