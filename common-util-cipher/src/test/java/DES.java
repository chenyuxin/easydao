

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.junit.jupiter.api.Test;

import com.wondersgroup.commonutil.baseutil.BASE64Util;

public class DES {
	/**
	 * 采用DES加密的，使用 passKey+username 拼接字符串直接为key
	 * 向量使用 DES.getsIv()
	 */
	private static String sKey = "012345678wonders1qaz2wsEfgdf4235hx1wdgcg2dg#@13Cd";
	private static String sIv = "%Td(8@3b";//Wrong IV length: must be 8 bytes long
	public static String getsKey() {
		return sKey;
	}
	public static String getsIv() {
		return sIv;
	}
	
	
	/**
	 * DES变化key，需要保持系统日期时间准确。
	 * 每天(24点)后变化key，加点盐。
	 * @param s 使用接口的passKey字符串
	 * @return
	 */
	public static String getKey1(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyddMM");
		Date date =new Date();
		String keyString = s.concat(sdf.format(date)).concat("Ha%6({$de");
		return MD5.EncoderByMd5(keyString);
	}
	
	/**
	 * 解密出错时尝试使用前一天的变化key
	 * DES变化key，需要保持系统日期时间准确。
	 * 每天(24点)后变化key，加点盐。
	 * @param s 使用接口的passKey字符串
	 * @return
	 */
	public static String getKey1yes(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyddMM");
		Date date =new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);//昨天
		date = calendar.getTime();
		String keyString = s.concat(sdf.format(date)).concat("Ha%6({$de");
		return MD5.EncoderByMd5(keyString);
	}
	
	public static String encrypt(String str, String sKey,String sIv){        
		String result = "";
		byte[] bKey;
	    byte[] bIv;
		try {
			bKey = sKey.getBytes("utf-8");
			bIv = sIv.getBytes("utf-8");
			DESedeKeySpec keySpec=new DESedeKeySpec(bKey);  
			SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("desede");              
			SecretKey key=keyFactory.generateSecret(keySpec);         
			Cipher cipher=Cipher.getInstance("desede/CBC/PKCS5Padding");  
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(bIv));  
			byte[] encrypted = cipher.doFinal(str.getBytes("utf-8"));  
			result = BASE64Util.encryptBASE64(encrypted);
       } catch (Exception e) {  
           e.printStackTrace();
       }  
       return result;  
	} 
	 
	public static String decrypt(String src, String sKey ,String sIv) {
       byte[] key;
       byte[] bIv;
       String result = "";
		try {
			key = sKey.getBytes("utf-8");
			bIv = sIv.getBytes("utf-8");
			IvParameterSpec iv = new IvParameterSpec(bIv);
			DESedeKeySpec desKey = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
			byte[] encrypted1 = BASE64Util.decryptBASE64B(src);
			result = new String(cipher.doFinal(encrypted1),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Test
	public void test() {
		/*
		String a = DES.encrypt( "1231中国","a03b6fe1ajikongshujuchuanshu",getsIv());
		System.out.println(a);
				System.out.println("解密："+DES.decrypt(a
				,"a03b6fe1ajikongshujuchuanshu"//getsKey()
				,getsIv()));
		 */
		String strEncode = "DkvizFkypfcg5HZJC0+auWBELnOMzOFfOQ8gEYXFoJF0Bq8XgmW0UfYzLXohllQUGY0G9IOjjoESCq38StziavrmYRXWSelSCmzdDTEqCtdwQVQnm5cftsmNImU5pBz/DrOOO6LVU8wuuuvfGSF88tx/yL6cVcN25Xq+0bQJTcCCBA0l5TFiYH3/BpEl8pZZQ/j19WiHPXjTvYfeRHmJjrVFx47Sk912t2IdGRhUELK/nTL9CRjpaUnGEn5Bc2npZCXunnrm86VWLAWh8JRBWKBa4x+zCHMRxUfdgxwTnTWcw73h3fPMZyETEnWjyleGe5vVY1Es01ICWaAZmlsxMXPacEQKKlKNS0RIgMcoYWE952WYLHmvGCy6K1RoUCRNrgOH/tcKlBAw9EUNHAgOVU92Xo652lG01Z2otkaAmIgV5KDvFpyeK1UT/61EDnEx0x1pm3we4YXRKfh5TV6isS22hYO1bl5bW24VLKrKNKX1appUKk28ruaC20vxNZAoJtrgn/TqKDye1cs+GpbfkMsjL/g+7+nPL1i+EpYk9dh6tQCmtPk31gfbbo51XnHijMIMMtb4SSuR5xoE8yG3+m4Tk5DdA80/Ts8mKsMD9ZQqVu/zKSwwLZpQac/taUuKLZeKYHvjnYA4l0KAKT7nGbpb+YSD4IXwZCLjwFasLNBt72PvmzrtBPbIvaqVlXMoXZjX7FRzCAeCYWu7O2/g67rwyAo8Oywl20A3tq+zLJQyDg53d0Sj2vAs0Z5+sXq/MdtFMUeq3OJOM9l0ffL3Qrbnc2kO2v01oCu35S16mWgPYDMwbMDBEiVZFyiW/iAT+PBs3mcSqLPhCYRPG58En1IwVSpTvsQZC1bl7Adxv83Wx/WwsMSBqW7bT7XOLePPfwawfvFQsIMnZtJhDYupa77lVLCBI7ozbLpQgaOqolpxp8H/KpuOhYS4ONMO9eJHtEqcvjNMjMdgWSRB3TXL/kEzvFJc5BegXxG50O2+mIGDPhi4Qdh8E1xyfBdFdlLQLdHBOyor/Q2iBYGZkaxYHoIqBwTKcuntuw6VZuGT4Cg=";
		System.out.println("解密："+DES.decrypt(strEncode
				,"a03b6fe1ajikongshujuchuanshu"//getsKey()
				,getsIv()));
	}
		
		

}

