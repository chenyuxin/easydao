package com.wondersgroup.commonutil;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * 基本工具类
 */
public class CommonUtil {
	
	/**
	 * 使用此方法配置日期格式等设置
	 * 再调用fastjson的toJSONString方法
	 * 调用此方法等同全局配置
	 */
	public static String toJSONString(Object obj){
		TypeUtils.compatibleWithFieldName =true;
		return JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
     * json的Key值转化为小写
     * @param json
     * @return
     */
    public static String transformLowerCase(String json){
        String regex = "[\\\"' ]*[^:\\\"' ]*[\\\"' ]*:";// (\{|\,)[a-zA-Z0-9_]+:

        Pattern pattern = Pattern.compile(regex);
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(json);
        while (m.find()) {
            m.appendReplacement(sb, m.group().toLowerCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }
	
    
    /**
	 * 执行某个对象的方法
	 * @param obj 执行对象
	 * @param methodName 执行对象的方法名
	 * @param args 执行方法的参数
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static Object invokeMethod(Object obj, String methodName, Object... args) throws Exception {
		return MyObj.invokeMethod(obj, methodName, args);
	}
	
	
}
