package com.wondersgroup.commonutil.cipher;
import java.io.UnsupportedEncodingException;

/*
BASE64的加密解密是双向的，可以求反解.
BASE64Encoder和BASE64Decoder是非官方JDK实现类。虽然可以在JDK里能找到并使用，但是在API里查不到。
(现在修改为自己算的BASE64)JRE 中 sun 和 com.sun 开头包的类都是未被文档化的，他们属于 java, javax 类库的基础，其中的实现大多数与底层平台有关，一般来说是不推荐使用的。 
BASE64 严格地说，属于编码格式，而非加密算法 
主要就是BASE64Encoder、BASE64Decoder两个类，我们只需要知道使用对应的方法即可。
另，BASE加密后产生的字节位数是8的倍数，如果不够位数以=符号填充。 
BASE64 
按照RFC2045的定义，Base64被定义为：Base64内容传送编码被设计用来把任意序列的8位字节描述为一种不易被人直接识别的形式。
（The Base64 Content-Transfer-Encoding is designed to represent arbitrary sequences of octets in a form that need not be humanly readable.） 
常见于邮件、http加密，截取http信息，你就会发现登录操作的用户名、密码字段通过BASE64加密的。
*/
public class BASE64 {
	
	public static byte[] decryptBASE64B(String str) {
		try {
			return decode(str);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
	
	/**  
     * BASE64解密  
     *   
     * @param str
     * @param charset 指定字符集  
     * @return  
     * @throws Exception 
     */  
    public static String decryptBASE64(String str,String charset) {   
		try {
			byte[] a = decode(str);
			if (null == charset) {
				return new String(a);
			} else {
				return new String(a,charset);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
    }
    
    /**
     * BASE64解密
     * @param str
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String str) {
		return decryptBASE64(str,null);
	}  

    /**  
     * BASE64加密  
     *   
     * @param key
     * @return String
     */  
    public static String encryptBASE64(byte[] key) {   
        return encode(key);   
    }
    
    /**
     * BASE64加密
     * @param str
     * @param charset
     * @return
     */
    public static String encryptBASE64(String str, String charset) {
    	byte[] key;
    	if (null == charset) {
    		key = str.getBytes();
    	} else {
    		try {
				key = str.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
    	}
    	return encryptBASE64(key);
    }
    
    /**
     * BASE64加密
     * @param str
     * @return
     */
    public static String encryptBASE64(String str) {
    	return encryptBASE64(str,null);
    } 

    public static void main(String[] args) {
    	String  str="235加的咖啡";//MjM15Yqg55qE5ZKW5ZWh

		String  result1 = BASE64.encryptBASE64(str.getBytes());
		System.out.println("result1=====加密数据=========="+result1);
		
		String  str2 = BASE64.decryptBASE64(result1);
		System.out.println("str2========解密数据========"+str2);

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private static char[] base64EncodeChars = new char[] {
	        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
	        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
	        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
	        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
	        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
	        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
	        'w', 'x', 'y', 'z', '0', '1', '2', '3',
	        '4', '5', '6', '7', '8', '9', '+', '/' };
	 
	    private static byte[] base64DecodeChars = new byte[] {
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
	    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
	    -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
	    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
	    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
	    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
	 
	    private static String encode(byte[] data) {
	        StringBuffer sb = new StringBuffer();
	        int len = data.length;
	        int i = 0;
	        int b1, b2, b3;
	        while (i < len) {
	            b1 = data[i++] & 0xff;
	            if (i == len)
	            {
	                sb.append(base64EncodeChars[b1 >>> 2]);
	                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
	                sb.append("==");
	                break;
	            }
	            b2 = data[i++] & 0xff;
	            if (i == len)
	            {
	                sb.append(base64EncodeChars[b1 >>> 2]);
	                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
	                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
	                sb.append("=");
	                break;
	            }
	            b3 = data[i++] & 0xff;
	            sb.append(base64EncodeChars[b1 >>> 2]);
	            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
	            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
	            sb.append(base64EncodeChars[b3 & 0x3f]);
	        }
	        return sb.toString();
	    }
	 
	    private static byte[] decode(String str) throws UnsupportedEncodingException {
	        StringBuffer sb = new StringBuffer();
	        byte[] data = str.getBytes("US-ASCII");
	        int len = data.length;
	        int i = 0;
	        int b1, b2, b3, b4;
	        while (i < len) {
	            /* b1 */
	            do {
	                b1 = base64DecodeChars[data[i++]];
	            } while (i < len && b1 == -1);
	            if (b1 == -1) break;
	            /* b2 */
	            do {
	                b2 = base64DecodeChars[data[i++]];
	            } while (i < len && b2 == -1);
	            if (b2 == -1) break;
	            sb.append((char)((b1 << 2) | ((b2 & 0x30) >>> 4)));
	            /* b3 */
	            do {
	                b3 = data[i++];
	                if (b3 == 61) return sb.toString().getBytes("ISO-8859-1");
	                b3 = base64DecodeChars[b3];
	            } while (i < len && b3 == -1);
	            if (b3 == -1) break;
	            sb.append((char)(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
	            /* b4 */
	            do {
	                b4 = data[i++];
	                if (b4 == 61) return sb.toString().getBytes("ISO-8859-1");
	                b4 = base64DecodeChars[b4];
	            } while (i < len && b4 == -1);
	            if (b4 == -1) break;
	            sb.append((char)(((b3 & 0x03) << 6) | b4));
	        }
	        return sb.toString().getBytes("ISO-8859-1");
	    }
    
    
    
    
    
    
    
    
    

}