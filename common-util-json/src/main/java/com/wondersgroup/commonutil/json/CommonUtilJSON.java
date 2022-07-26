package com.wondersgroup.commonutil.json;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter.Feature;

/**
 * JSON工具类
 *
 */
public class CommonUtilJSON {
	
	/**
	 * 使用此方法配置日期格式等设置
	 * 再调用fastjson的toJSONString方法
	 * 调用此方法等同全局配置
	 */
	public static String toJSONString(Object obj){
		//TypeUtils.compatibleWithFieldName =true;
		//return JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteDateUseDateFormat);
		return JSON.toJSONString(obj,"yyyy-MM-dd HH:mm:ss",Feature.WriteMapNullValue,Feature.WriteNonStringValueAsString);
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
     * json的Key名称替换
     * @param json 原json
     * @param renameMap 需要替换的key名称对照Map,其中key为原名称,value为重命名名称
     * @return
     */
    public static String renameKey(String json,Map<String, String> renameMap) {
    	String regex = "[\\\"' ]*[^:\\\"' ]*[\\\"' ]*:";

        Pattern pattern = Pattern.compile(regex);
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(json);
        while (m.find()) {
        	String oldKeyName = m.group().substring(1,m.group().length()-2);
        	String newName = renameMap.get(oldKeyName);
        	if(newName != null && !"".equals(newName)) {
        		m.appendReplacement(sb, m.group().substring(0,1).concat(newName).concat(m.group().substring(m.group().length()-2)));
        	}
        }
        m.appendTail(sb);
        return sb.toString();
	}

}
