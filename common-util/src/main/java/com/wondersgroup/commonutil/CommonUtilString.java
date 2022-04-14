package com.wondersgroup.commonutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.commonutil.type.CommonType;
import com.wondersgroup.commonutil.type.format.DateType;
import com.wondersgroup.commonutil.type.language.JSONType;
import com.wondersgroup.commonutil.type.language.LanguageType;
import com.wondersgroup.commonutil.type.language.XMLType;

/**
 * 字符串 通用方法
 */
public class CommonUtilString {
	
	
	/**
     * 去左空格
     * @param str
     * @return
     */
    public static String trimLeft(String str) {
        if (str == null || str.equals(StringPool.BLANK)) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+", StringPool.BLANK);
        }
    }
    
    /**
     * 去右空格
     * @param str
     * @return
     */
    public static String trimRight(String str) {
        if (str == null || str.equals(StringPool.BLANK)) {
            return str;
        } else {
            return str.replaceAll("[　 ]+$", StringPool.BLANK);
        }
    }
    
    /**
     * 去掉所有空格和制表符
     * @param str
     * @return
     */
    public static String trimAll(String str) {
    	if (str == null || str.equals(StringPool.BLANK)) {
            return str;
        } else {
        	return str.replaceAll("\\s*", StringPool.BLANK);
        }
	}
    

    
    
    
	/**
	 * 截取字符串，根据开始字符串和结束字符串获取。
	 * @param repString 源字符串
	 * @param beginString 开始字符串 (第一个)
	 * @param endString 结束字符串 (从开始字符串算起第一个)
	 * @return
	 */
	public static String subString(String repString, String beginString, String endString) {
		int begingIndex = repString.indexOf(beginString) + beginString.length();
		int endIndex = repString.indexOf(endString, begingIndex);
		return repString.substring(begingIndex, endIndex);
	}
	
	/**
	 * 截取字符串，开始游标和结束游标，结束大了不报错
	 * @param rep 源字符串
	 * @param begin 开始
	 * @param end 结束
	 * @return
	 */
	public static String subString(Object rep, int begin, int end) {
		String repStr = String.valueOf(rep);
		return repStr.substring(begin, end>repStr.length()?repStr.length():end);
	}
	
	
	
	/**
	 * 生成请求参数的字符串请求文
	 * 当oString为json格式时,参数变量中 日期和字符串格式需要用两头加双引号 "#{keyName}" 
	 * 日期格式化配置需要修改请在调用本方法前调用DateType修改默认样式. 
	 * @param oString 含有参数变量的请求文 参数变量命名key #{keyName}
	 * @param params 参数变量值
	 * @param types 原文语言 (没有特殊语言处理可不必传参)
	 * @return 最终的请求参数字符串
	 */
	public static String setVariableParams(String oString,Map<String, ?> params,CommonType... types) {
		LanguageType languageType = null;
		for(CommonType type : types) {
			if (type instanceof LanguageType) {
				if (languageType == null) {
					languageType = (LanguageType) type;
				} else {
					throw new IllegalArgumentException("LanguageType 只能为一种");
				}
			}
		}
		
		int cursor = 0;//游标
		int current = 0;//当前读
		StringBuffer sBuffer = new StringBuffer();
		while(cursor < oString.length()) {
			current = oString.indexOf("#{",cursor);
			if (current == -1) {
				sBuffer.append(oString.substring(cursor));
				cursor = oString.length();
			} else {
				sBuffer.append(oString.substring(cursor, current));
				cursor = current + 2;
				current = oString.indexOf("}",cursor);
				String keyName = oString.substring(cursor, current).trim();
				Object param = params.get(keyName);
				if ( param instanceof String) {
					if (languageType != null) {//json和xml 数据值中特殊字符的转义
						param = languageType.getEscapeString((String)param);
					}
				} else if (param instanceof Date) {//日期类型参数转换格式
					param = DateType.getInstance().getFormatValue(param);
				}
				if (null == param && languageType != null && languageType.getTypeName().equals(XMLType.typeName)) {
					//xml格式参数空值直接用空字符串
					param = StringPool.BLANK;
				} 
				sBuffer.append(param);
				if (languageType != null && languageType.getTypeName().equals(JSONType.typeName)) {
					String nullsign = sBuffer.substring(sBuffer.length()-5, sBuffer.length());
					if (nullsign.equals("\"null") && oString.substring(current+1, current+2).equals(StringPool.QUOTE) ) {
						sBuffer.delete(sBuffer.length()-4,sBuffer.length());
					}
				}
				cursor = current+1;
			}
		}
		return sBuffer.toString();
	}
	
	/**
	 * 生成请求批量参数的字符串请求文
	 * @param oString 含有参数变量的请求文 参数变量命名key #{keyName} , 需要批量循环的内容 #i[ ... ]#
	 * @param params 批量参数变量值
	 * @param types 原文语言 (没有特殊语言处理可不必传参)
	 * @return 最终的请求参数字符串
	 */
	public static String setVariableParams(String oString,List<Map<String, Object>> params,CommonType... types) {
		LanguageType languageType = null;
		for(CommonType type : types) {
			if (type instanceof LanguageType) {
				if (languageType == null) {
					languageType = (LanguageType) type;
				} else {
					throw new IllegalArgumentException("LanguageType 只能为一种");
				}
			}
		}
		
		int cursor = 0;//游标
		int current = 0;//当前读
		StringBuffer sBuffer = new StringBuffer();
		while(cursor < oString.length()) {
			current = oString.indexOf("#1[",cursor);
			if (current == -1) {
				sBuffer.append(oString.substring(cursor));
				cursor = oString.length();
			} else {
				sBuffer.append(oString.substring(cursor, current));
				if (JSONType.typeName.equals(languageType.getTypeName())) {
					sBuffer.append("[");//json形式，添加[括号。
				}
				
				cursor = current+3;// 注意 #1[ 位数
				current = oString.indexOf("]#",cursor);
				String listString = oString.substring(cursor,current);
				for (int j=0;j<params.size();j++) {
					if (JSONType.typeName.equals(languageType.getTypeName())) {
						if (j > 0) {//第一个数据不加,分割符
							sBuffer.append(",");//json形式，添加[括号。
						}
					}
					sBuffer.append(setVariableParams(listString,params.get(j),languageType));
				}
				
				cursor = current+2;
				if (JSONType.typeName.equals(languageType.getTypeName())) {
					sBuffer.append("]");//json形式，添加]括号。
				}
			}	
			
		}
		return sBuffer.toString();
	}
	
	/**
	 * 通过请求文获取需要查询的keyName的list
	 * @param oString 含有参数变量的请求文 参数变量命名key #{keyName}
	 * @return
	 */
	public static List<String> getParamsKeyNames(String oString){
		Set<String> attributeNames = new HashSet<String>();
		int cursor = 0;//游标
		int current = 0;//当前读
		while(cursor < oString.length()) {
			current = oString.indexOf("#{",cursor);
			if (current == -1) {
				cursor = oString.length();
			} else {
				cursor = current + 2;
				current = oString.indexOf("}",cursor);
				String keyName = oString.substring(cursor, current).trim();
				attributeNames.add(keyName);
				cursor = current+1;
			}
		}
		return  new ArrayList<String>(attributeNames);
	}

	

}
