package com.wondersgroup.commonutil.type.otherintf;

/**
 * 与Parse相反，反解析
 * @param <T>
 * @param <E>
 */
public interface EscapeType<T,E> {
	
	/**
	 * 反解析为对应的类型
	 * @param <E>
	 * @param value
	 * @return
	 */
	T escape(E value);

}
