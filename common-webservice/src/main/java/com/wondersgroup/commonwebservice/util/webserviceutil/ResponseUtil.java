package com.wondersgroup.commonwebservice.util.webserviceutil;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

	public static Map<String, Object> createResponse(ResponseHead responseHead,Object data){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", responseHead.getIndex());
		map.put("message", responseHead.getName());
		map.put("data", data);
		return map;
	}
	
}

