package com.wondersgroup.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.wondersgroup.commonutil.CommonUtilMap;
import com.wondersgroup.commonutil.CommonUtilValidation;
import com.wondersgroup.commonutil.transform.TransformPrivacy;
import com.wondersgroup.commonutil.type.ordinary.PrivacyType;

public class Snippet {
		@Test
	    public void test1() {
	//        System.out.println("isPhoneNum:" + isPhoneNum(0, "147352031901"));
	//        System.out.println("isPhoneNum:" + isPhoneNum(1, "0816-260886"));
	//        System.out.println("isPhoneNum:" + isPhoneNum(1, "028-7223971"));
	//        System.out.println("isPhoneNum:" + isPhoneNum(2, "17935203190"));
	//        System.out.println("isPhoneNum:" + isPhoneNum(2, "0816-2608866"));
	//        System.out.println("isEmail:" + isEmail("none@nones.com"));
	//        System.out.println("isQQ:" + isQQ("253581950"));
	//        System.out.println("IdentityCardVerification:" + IdentityCardVerification("511324198703070051"));
	        System.out.println("isIp:" + CommonUtilValidation.isIP("123.345.345.643"));
	        System.out.println("isIp:" + CommonUtilValidation.isIP("254.253.252.3"));
	    	System.out.println(CommonUtilValidation.constantIp("成绩都是快乐的IP： 254.192.254.3成绩都是快乐的发动机老师肯定就"));
	    	String tString = TransformPrivacy.constantTransform("成绩都是快乐的IP:154.253.254.3成绩都是快乐的发动机老师肯定就", CommonUtilValidation.ipRegex, PrivacyType.IPv4);
	    	System.out.println(tString);
	    	
	    }
		
		
		@Test
		public void test2() {
			Map<String, Object> map = new HashMap<>();
			map.put("a", new Float("0.618"));
			map.put("b", 0.618);
			map.put("c", null);
			map.put("d", 2);
			
			
			Float result = CommonUtilMap.getValueOfMap(Float.class, "d", map);
			System.out.println(result);
			String resultString = CommonUtilMap.getValueOfMap(String.class, "a", map);
			if (null == resultString) {
				System.out.println(resultString);
			}
			System.out.println(resultString);
			
			Integer resultInt = CommonUtilMap.getValueOfMap(Integer.class, "b", map);
			System.out.println(resultInt);
		}
		
		@Test
		public void test3() {
			Map<String, Object> map = new HashMap<>();
			map.put("a", new Float("0.618"));
			map.put("b", 0.618);
			map.put("c", null);
			map.put("d", 2);
			
			
			Integer a = CommonUtilMap.getValueOfMap(-1, "d", map);
			System.out.println(a);
			
		}
		
		
		
}

