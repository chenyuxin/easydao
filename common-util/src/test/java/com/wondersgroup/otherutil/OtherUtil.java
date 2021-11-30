package com.wondersgroup.otherutil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.wondersgroup.commonutil.CommonUtilString;
import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.commonutil.CommonUtilValidation;
import com.wondersgroup.commonutil.type.language.JSONType;
import com.wondersgroup.commonutil.type.language.XMLType;

public class OtherUtil {
	
	@Test
	public void testUUID(){
		System.out.println(CommonUtilUUID.getUUID());
		
	}

	  
    @Test
	public void test1() {
    	String oString = "<a>#</a><b>#{ key1}</b><c>#{key2 }</c><d>#{ key3  }</d><e>#{key4}</e>";
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("key1", "第一个");
    	params.put("key2", 2);
    	params.put("key3", 3.3);
    	params.put("key4", new Date());
    	
    	System.out.println(CommonUtilString.setVariableParams(oString, params));
    }
    
    @Test
    public void test2(){
    	String oString  = "data\\\":{\\\"code\\\":\\\"";
    	System.out.println(oString.length());
    }
    
    @Test
    public void test3(){
    	String oString  = "<a>#</a><b>#{ key1}</b><c>#{key2 }</c><d>#{ key3  }</d><e>#{key4}</e>";
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("key1", "第一个");
    	params.put("key2", null);
    	params.put("key3", 3.3);
    	params.put("key4", new Date());
    	
    	String nstring = CommonUtilString.setVariableParams(oString,params,XMLType.getInstance());
    	
    	System.out.println(nstring);
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void test4(){
    	String oString  = "{\"baseInfo\":{" +
        "\"code\":\"#{code}\"," +
        "\"reCode\":\"#{ reCode }\"," +
        "\"applyCode\":#{applyCode},"+
        "\"bizStatus\":\"#{bizStatus}\","+
        "\"sysCode\":\"#{sysCode}\"}}";
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("code", "第\\一\"个");
    	params.put("reCode", null);
    	params.put("applyCode", null);
    	params.put("bizStatus", new Date());
    	
    	String nstring = CommonUtilString.setVariableParams(oString,params,JSONType.getInstance());
    	System.out.println(nstring);
    	
		Map<String, Object> nMap = JSON.parseObject(nstring, Map.class);
    	System.out.println(nMap);
    	System.out.println(((Map<String, Object>)nMap.get("baseInfo")).get("code"));
    }
    
    @Test
    public void isUUID32(){
    	System.out.println("28c6284ed98841719a8d099ef69efdcb:" + CommonUtilValidation.isUUID32("28c6284ed98841719a8d099ef69efdcb"));
    	System.out.println(CommonUtilValidation.isUUID32("28C6284ED98841719A8D099EF69EFDD0"));
    	System.out.println(CommonUtilValidation.isUUID32("28c6284ed98841719a8d099ef69efdd0"));
    	System.out.println(CommonUtilValidation.isUUID32("系统.."));
    }

}
