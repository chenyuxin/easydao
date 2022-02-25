
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.commonutil.baseutil.BaseUtil;
import com.wondersgroup.commonutil.baseutil.CharsetUtil;


public class TestUUID {
	
	@Test
	public void uuidTest0() {
		String uuid64 = BaseUtil.toStringUUID64(UUID.nameUUIDFromBytes("中国@123Ab".getBytes(CharsetUtil.CHARSET_UTF_8)));
		System.out.println(uuid64);
		
		String uuid64a = CommonUtilUUID.getUUID64();
		System.out.println(uuid64a);
	}
	
	
	@Test
	public void uuidTest() {
//		SecureRandom ng = new SecureRandom();;
//
//        byte[] randomBytes = new byte[16];
//        ng.nextBytes(randomBytes);
//        randomBytes[6]  &= 0x0f;  /* clear version        */
//        randomBytes[6]  |= 0x40;  /* set to version 4     */
//        randomBytes[8]  &= 0x3f;  /* clear variant        */
//        randomBytes[8]  |= 0x80;  /* set to IETF variant  */
//        TestUUIDObj randomUUID = new TestUUIDObj(randomBytes);
//        long mostSigBits = randomUUID.getMostSigBits();
//        long leastSigBits = randomUUID.getLeastSigBits();
        long mostSigBits = -7182045288110405954L;
        long leastSigBits = -8899417136605244499L;
        
        String randomString = TestUUIDObj.digits(mostSigBits >> 32, 8);
        System.out.println(randomString);
        String randomString2 = TestUUIDObj.digits(mostSigBits >> 16, 4); 
        System.out.println(randomString2);
        String randomString3 = TestUUIDObj.digits(mostSigBits, 4);
        System.out.println(randomString3);
        String randomString4 = TestUUIDObj.digits(leastSigBits >> 48, 4);
        System.out.println(randomString4);
        String randomString5 = TestUUIDObj.digits(leastSigBits, 12);
        System.out.println(randomString5);
        
        System.out.println(randomString+'-'+randomString2+'-'+randomString3+'-'+randomString4+'-'+randomString5);
	}
	
	@Test
	public void uuidTest2() {
		
		
		UUID uuid = UUID.randomUUID();
		long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        //long mostSigBits = -7182045288110405954L;
        //long leastSigBits = -8899417136605244499L;
        /**
         * D5g_Td5FhHW4vKJ3Y9XjHg
         * 
         * D5g_Td5FhH
         * D5g_Td5FhH  vKJ3Y9XjHg
         * 1856965178706793579
         * 9C543FDCD16946B
         * 9C543FDCD16946b 
         * 
         * E84
         * W4
         * 
         * e 847EEB43F09ED3AD
           e84 7EEB43F09ED3AD0
             4 7EEB43F09ED3AD0
               
	        6917734364
			9c543fdc
			119145
			d169
			83646
			46be
			99454
			847e
			540152008987565
			eb43f09ed3ad
			9c543fdc-d169-46be-847e-eb43f09ed3ad
         */
        char[] buf = new char[22];
        digits64(mostSigBits>>4,buf,0, 10);
        
        long most4 = (mostSigBits & 15) << 8;
        long least8 = leastSigBits >>> 56;
        digits64(most4|least8 ,buf,10, 2);
        
        digits64(leastSigBits<<4,buf,12,10);
        
        System.out.println(new String(buf));
        
	}
	
	
	/**
	 * 转换成64进制来表示
	 * @param val
	 * @param buf
	 * @param offset
	 * @param digits
	 * @return
	 */
	private static void digits64(long val,char[] buf,int offset, int digits) {
        long hi = 1L << (digits * 6);
        long binaryNum  = hi | (val & (hi - 1));
        formatUnsignedLong(binaryNum,6,buf,offset,digits);
    }
	
	
	
	public static final String idBase64="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
									 //	 123456789  12 15 18 21 24 27 30 33 36 39 42 45 48 51 54 57 60 63   
									 //           10 13 16 19 22 25 28 31 34 37 40 43 46 49 52 55 58 61 64
									 //            11 14 17 20 23 26 29 32 35 38 41 44 47 50 53 56 59 62 
	/**
	 * Format a long (treated as unsigned) into a character buffer.
     * @param val the unsigned long to format
     * @param shift the log2 of the base to format in (6 for idbase64, 4 for hex, 3 for octal, 1 for binary)
     * @param buf the character buffer to write to
     * @param offset the offset in the destination buffer to start at
     * @param len the number of characters to write
     * @return the lowest character location used
	 */
	public static int formatUnsignedLong(long val,int shift, char[] buf, int offset, int len) {
		int charPos = len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[offset + --charPos] = idBase64.charAt((int) val & mask);
            val >>>= shift;
        } while (val != 0 && charPos > 0);
        
        return charPos;
    }
	
	
	
	
	@Test
	public void testUUID() {
//		String a = BaseUtil.getUUIDC("中国@Ab123");
//		System.out.println(a);//49aac8f261ed3b8e96a9f86c56380ff3
//		
//		String b = BaseUtil.getUUIDC64("中国@Ab123");
//		System.out.println(b);//iqH8YC7JeUWmGvxIlzwfYM
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString().replace("-", ""));
		
		String a = BaseUtil.toStringUUID(uuid);
		System.out.println(a);
		String b = BaseUtil.toStringUUID64(uuid);
		System.out.println(b);
		
		String c = CommonUtilUUID.idbase64ToHex(b);
		System.out.println(c);
		
		String d = CommonUtilUUID.hexToIdbase64(c);
		System.out.println(d);
		
	}
	
	
	
	
	@Test
	public void testChar() {
		char a = 0;
		for (int i=0;i<65535;i++) {
			System.out.println(a);
			a = (char) (a + 1);
			//System.out.print(" " + a);
		}
		
//		byte b = (byte) -128;
//		for (int i=0;i<256;i++) {
//			System.out.println(String.valueOf(b));
//			b = (byte) (b + 1);
//		}
		
		
		
	}

	@Test
	public void testToStringUUID() {
		List<UUID> uuids = new ArrayList<UUID>();
		for (int i=0;i<90001;i++) {
			UUID uuid = UUID.nameUUIDFromBytes(String.valueOf(i).getBytes());
			uuids.add(uuid);
		}
//		UUID uuid = UUID.nameUUIDFromBytes("123".getBytes());
//		System.out.println("mostSigBits:" + uuid.getMostSignificantBits());
//		System.out.println("leastSigBits:" + uuid.getLeastSignificantBits());
		
		String a = null;
		long startTime = System.currentTimeMillis();
		for (int i=0;i<uuids.size();i++) {
			a = BaseUtil.toStringUUID(uuids.get(i));
		}
		long endTime = System.currentTimeMillis();
		System.out.println(a + " 1用时" + (endTime-startTime) + "毫秒" );
		
		String c = null;
		startTime = System.currentTimeMillis();
		for (int i=0;i<uuids.size();i++) {
			c = BaseUtil.toStringUUID64(uuids.get(i));
		}
		endTime = System.currentTimeMillis();
		System.out.println(c + " 3用时" + (endTime-startTime) + "毫秒");
		
		String d = null;
		startTime = System.currentTimeMillis();
		for (int i=0;i<uuids.size();i++) {
			d = uuids.get(i).toString();
		}
		endTime = System.currentTimeMillis();
		System.out.println(d + " 4用时" + (endTime-startTime) + "毫秒");
		
		
		UUID uuid = CommonUtilUUID.fromString(a);
		System.out.println(BaseUtil.toStringUUID(uuid));
	}
	
	

}
