package com.wondersgroup.commonutil.type.format;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wondersgroup.commonutil.constant.StringPool;

/**
 * 日期类型的操作类
 * 传入或修改本实例配置,用以告知通用工具类的方法如何操作
 */
public class DateType implements FormatType {
	
	private static DateType instance = null;
	
	private DateType() {
	}
	
	private static synchronized void syncInit() {  
        if (instance == null) {  
            instance = new DateType();  
        }  
    }
	
	public static DateType getInstance() {  
        if (instance == null) {  
            syncInit();  
        }  
        return instance;  
    }
	
	public final static String typeName = "dateType";
	
	public static final String DEFAULT_FORMAT_TYPE = "yyyy-MM-dd HH:mm:ss";
	
	@Override
	public String getTypeName() {
		return typeName;
	}

	@Override
	public String getFormatValue(Object oValue) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_TYPE);
		String format = sdf.format(oValue);
		format = format.replaceAll(" 00:00:00", StringPool.BLANK);
		return format;
//		return DateFormatUtils.format((Date) oValue, DEFAULT_FORMAT_TYPE);
	}
	
	/**
	 * 只在本次转换格式
	 */
	public String getFomatValue(Object oValue,String pattern) {
		if (null == oValue) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(oValue);
	}

	@Override
	public Date getParseValue(String str) {
		if (str == null) {
            return null;
        }
        
        SimpleDateFormat parser = new SimpleDateFormat();
        final ParsePosition pos = new ParsePosition(0);
        final String[] parsePatterns = {DEFAULT_FORMAT_TYPE,"yyyy-MM-dd","yyyyMMdd"};//默认转换日期格式
        
        for (final String parsePattern : parsePatterns) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }
            
            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePattern.endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2"); 
            }

            final Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        return null;
	}
	
	/**
	 * 自定义转换格式 解析字符串为Date
	 * @param str 
	 * @param parsePatterns
	 * @return
	 */
	public Date getParseValue(String str,String... parsePatterns) {
		if (str == null) {
            return null;
        }
        
        SimpleDateFormat parser = new SimpleDateFormat();
        final ParsePosition pos = new ParsePosition(0);
        
        for (final String parsePattern : parsePatterns) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }
            
            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePattern.endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2"); 
            }

            final Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        return null;
	}

}
