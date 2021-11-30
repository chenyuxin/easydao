package com.wondersgroup.commonwebservice.util.webserviceutil;

import java.util.HashMap;
import java.util.Map;

/**
 * WebserviceUtils请求接口添加自定义请求头消息
 * 配置contentType等
 */
public class Header {
	
	private String contentType;
	
	private Map<String,String> reqHeader = new HashMap<String,String>();
	
	Header(){}
	
    /**
     * 设置ContentType
     * @param ContentType
     */
	public Header(String ContentType) {
		this.contentType = ContentType;
	}
	
	public String put(String key,String value) {
		return this.reqHeader.put(key, value);
	}
	
	public Map<String, String> getReqHeader() {
		return reqHeader;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	

}
