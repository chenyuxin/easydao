package com.wondersgroup.commonutil.type.language;

import java.util.HashMap;
import java.util.Map;

/**
 * json语言 相关格式的配置
 * 调用getInstance()方法获取实例
 * 传入或修改本实例配置,用以告知通用工具类的方法如何操作
 */
public class JSONType implements LanguageType {

	private static JSONType instance = null;
	
	private JSONType() {
	}
	
	private static synchronized void syncInit() {  
        if (instance == null) {  
            instance = new JSONType();  
        }  
    }
	
	public static JSONType getInstance() {  
        if (instance == null) {  
            syncInit();  
        }  
        return instance;  
    }
	
	public final static String typeName = "json";
	
	public final static String JSON_CONTENT_TYPE = "application/json;charset=utf-8";
	
	@Override
	public String getTypeName() {
		return typeName;
	}
	
	@Override
	public Map<String, String> getWebContentType() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", JSON_CONTENT_TYPE);
		return map;
	}

	@Override
	public String getEscapeString(String value) {
		String jsonValue = value.replaceAll("\\\\", "\\\\\\\\");//将数据值中含有 \ 符号转义能识别
		jsonValue = jsonValue.replaceAll("\"", "\\\\\\\"");// 将数据值中含有   " 符号转义能识别
		return jsonValue;
	}

	


}
