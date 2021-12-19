package com.wondersgroup.commonutil.baseutil;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.wondersgroup.commonutil.constant.RegexConst;

/**
 * 基本的工具类，供内部程序包使用<br>
 * 不提供API，外部使用请找CommonUtil等API
 */
public class BaseUtil {
	
	
	/**
	 * 替换字符串的空值为指定值
	 * @param string 待判断的字符串
	 * @param isNotNullString 替换空值的默认字符串
	 * @return
	 * @throws Exception
	 */
	public static String setNullString(String string, String isNotNullString) {
		if (null == string || "".equals(string) ) {
			string = isNotNullString;
		}
		return string;
	}
	
	/**
	 * 替换某类型的空值为指定值
	 * @param <T>
	 * @param obj 待判断的值
	 * @param isNotNullValue 替换空值的默认值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T setNull(T obj,T isNotNullValue) {
		if (null == obj) {
			obj = isNotNullValue;
		} else if (obj.getClass().equals(String.class) ) {
			if ("".equals(obj) ) {
				obj = (T) String.valueOf(isNotNullValue);
			}
		}
		return obj;
	}
	
	/**
	 * 是否为Base64字符串
	 * @param str
	 * @return
	 */
	public static boolean isBase64(String str) {
		return str.matches(RegexConst.BASE64);
	}
	
	
	/**
     * 根据字符串生成唯一id<br>
     * 无`-`的UUID字符串
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getUUIDC(String str) {
    	UUID uuid = UUID.nameUUIDFromBytes(str.getBytes(CharsetUtil.CHARSET_UTF_8));
    	return toStringUUID(uuid);
    }
    
    /**
     * 返回无`-`的UUID字符串
     * @param uuid
     * @return
     */
    public static String toStringUUID(UUID uuid) {
    	long mostSigBits = uuid.getMostSignificantBits();
    	long leastSigBits = uuid.getLeastSignificantBits();
    	return digits(mostSigBits >> 32, 8) + 
               digits(mostSigBits >> 16, 4) + 
               digits(mostSigBits, 4) + 
               digits(leastSigBits >> 48, 4) + 
               digits(leastSigBits, 12);
    }
    
    /** Returns val represented by the specified number of hex digits. */
    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

}
