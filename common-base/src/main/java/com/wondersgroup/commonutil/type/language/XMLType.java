package com.wondersgroup.commonutil.type.language;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;


/**
 * xml语言 相关格式的配置
 * 调用getInstance()方法获取实例
 * 传入或修改本实例配置,用以告知通用工具类的方法如何操作
 */
public class XMLType implements LanguageType {
	
	private static XMLType instance = null;
	
	private XMLType() {
	}
	
	private static synchronized void syncInit() {  
        if (instance == null) {  
            instance = new XMLType();  
        }  
    }
	
	public static XMLType getInstance() {  
        if (instance == null) {  
            syncInit();  
        }  
        return instance;  
    }

	public final static String typeName = "xml";
	
	public static final String XML_CONTENT_TYPE = "text/xml;charset=utf-8";
	
	@Override
	public String getTypeName() {
		return typeName;
	}
	
	@Override
	public Map<String, String> getWebContentType() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", XML_CONTENT_TYPE);
		return map;
	}

	@Override
	public String getEscapeString(String value) {
		return  StringEscapeUtils.escapeXml10(value);
	}

	

	

}
