package com.wondersgroup.commonutil;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.wondersgroup.commonutil.baseutil.BaseUtil;

public class CommonUtilUUID {
	/**
	 * 随机生成唯一id<br>
	 * 无`-`的UUID字符串
	 * @return
	 */
    public static String getUUID(){
    	UUID uuid = UUID.randomUUID();
    	return BaseUtil.toStringUUID(uuid);
    }
    
    /**
     * 根据字符串生成唯一id<br>
     * 无`-`的UUID字符串
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getUUIDC(String str) {
    	return BaseUtil.getUUIDC(str);
    }
    


}