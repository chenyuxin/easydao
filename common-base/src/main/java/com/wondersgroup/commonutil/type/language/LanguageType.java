package com.wondersgroup.commonutil.type.language;

import java.util.Map;

import com.wondersgroup.commonutil.type.CommonType;

/**
 * 语言格式类
 * 传入配置好的本实例告知通用工具类的方法如何操作
 */
public interface LanguageType extends CommonType {
	
	/**
	 * 类型名
	 */
	public String getTypeName();
	
	/**
	 * 获取该类型的web请求头
	 * Content-Type类型
	 */
	public Map<String, String> getWebContentType();
	
	/**
	 * 获取该类型的转义
	 * 在不使用工具，自行拼接该语言时会遇到，数据值需要转义才能正确被识别
	 * 如 " 或者 \ 符号的转义
	 */
	public String getEscapeString(String value);
	
	

}
