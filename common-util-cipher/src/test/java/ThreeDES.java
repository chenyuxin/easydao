

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;


public class ThreeDES {
	
	private static final byte[] iv = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
	private static final String Algorithm = "desede";
	private static final byte[] keyBytes = new byte[]{17, 34, 79, 88, -120, 16, 64, 56, 40, 37, 121, 81, -53, -35, 85,
			102, 119, 41, 116, -104, 48, 64, 54, -30};
	public static byte[] getKey(){
		return keyBytes;
	}

	public static byte[] encode(byte[] keybyte, byte[] src) {
		try {
			SecretKeySpec e3 = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv);
			c1.init(1, e3, ips);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException arg4) {
			arg4.printStackTrace();
		} catch (NoSuchPaddingException arg5) {
			arg5.printStackTrace();
		} catch (Exception arg6) {
			arg6.printStackTrace();
		}

		return new byte[0];
	}
	
	public static byte[] decrypt(byte[] keybyte, byte[] src) {
		try {
			IvParameterSpec ips = new IvParameterSpec(iv);
			SecretKeySpec e3 = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance("desede/CBC/PKCS5Padding");
			c1.init(2, e3, ips);
			return c1.doFinal(src);
		} catch (Exception e) {
			return new byte[0];
		}
	}

	
	public static String bytes2HexString(byte[] b) {
		StringBuffer stringBuffer = new StringBuffer("");   
	    if (b == null || b.length <= 0) {   
	        return "";   
	    } 
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[ i ] & 0xFF);
			if (hex.length() == 1) {
				stringBuffer.append(0);
			}
			stringBuffer.append(hex.toUpperCase());
		}
		return stringBuffer.toString();
	}
	
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return new byte[0];  
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}
	
	private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param enString ?????????????????????
	 * @return
	 */
	public static String decrypt(String enString){
		return new String(decrypt(keyBytes, hexStringToBytes(enString)));
	}
	
	/**
	 * ????????????????????????????????????
	 * @param cSrc ??????????????????
	 * @return
	 */
	public static String encode(String cSrc){
		return ThreeDES.bytes2HexString(encode(keyBytes, cSrc.getBytes()));
	}
	
	/**
	 * ????????????????????????????????? ????????????
	 * @param obj ???????????????????????????
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void decryptAllField(Object obj) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if ( field.getType() == String.class ){//???????????????????????????????????????
				String value = (String) field.get(obj);
				if (StringUtils.isNotBlank(value)){//?????????????????????????????????
					value = decrypt(value);
					if (StringUtils.isNotBlank(value)){//??????????????????????????????????????????
						field.set(obj, value);
					}
				}
			}
		}
	}
	
	@Test
	public void test() {
		// ?????????????????????  
	    String cSrc = "?????????@123";  
	    System.out.println(cSrc + "  ?????????" + cSrc.length());  
	    // ??????  
	    long lStart = System.currentTimeMillis();  
	    String enString = ThreeDES.bytes2HexString(ThreeDES.encode(ThreeDES.getKey(), cSrc.getBytes()));
	    System.out.println("????????????????????????" + enString + "?????????" + enString.length());  
	      
	    long lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("???????????????" + lUseTime + "??????");  
	    // ??????  
	    lStart = System.currentTimeMillis();  
	    //enString = "79FF260EF3667BA1994F4B41BB126EE6C6587F078A7C2F015D945D10DF05011D81DD555419D6B717C910E9D71DB0BCB33CE737543B0D0479DE82F97A092F38A06C02D7AEF5871DFAD788ECF6B8A02079";
	    String DeString = new String(ThreeDES.decrypt(ThreeDES.getKey(), ThreeDES.hexStringToBytes(enString)));
	    System.out.println("????????????????????????" + DeString);  
	    lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("???????????????" + lUseTime + "??????");  
		
	}

/*	
http://10.99.0.13:8081/ehrview/doctorRead?medicalOrgId=21BB42E5B2CD5E923FA2638A874CA4E5&deptId=11CE196A4A63F31C&jobNo=E3FE2D50C009AF41&idCard=62ECEE1B2E84497D82A3DAA54E2484A4BEEF6A8F1E18640F&passWord=0389CA1EA11FC0AA&cardType=721AD0BD8AF27029
*/	
	
}
