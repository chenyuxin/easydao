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

}
