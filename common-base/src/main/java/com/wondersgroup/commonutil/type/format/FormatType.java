package com.wondersgroup.commonutil.type.format;

import com.wondersgroup.commonutil.type.CommonType;

/**
 * 格式化操作类
 * 传入配置好的本实例告知通用工具类的方法如何操作
 */
public interface FormatType extends CommonType {

	/**
	 * 类型名
	 */
	public String getTypeName();
	
	
	
	/**
	 * 格式化原值
	 * @param oValue
	 * @return
	 */
	public String getFormatValue(Object oValue);
	
	/**
	 * 解析为原值
	 * @param str
	 * @return
	 */
	public Object getParseValue(String str);

}
