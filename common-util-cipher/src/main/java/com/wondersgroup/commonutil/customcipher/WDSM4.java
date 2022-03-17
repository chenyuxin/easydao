package com.wondersgroup.commonutil.customcipher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.wondersgroup.commonutil.cipher.CustomCipher;

public class WdSM4 implements CustomCipher {
	
	private static WdSM4 singleInstance = null;
	private WdSM4(){}
	private static synchronized void syncInit() {  
        if (singleInstance == null) {  
        	singleInstance = new WdSM4();  
        }  
    }
	public static WdSM4 getSingleInstance() {  
        if (singleInstance == null) {  
            syncInit();  
        }  
        return singleInstance;  
    }
	
	@Override
	public byte[] encrypt(byte[] plainText,byte[] key,byte[] iv) throws Exception {
		SM4_Context ctx = new SM4_Context();
		ctx.isPadding = true;
		ctx.mode = 1;
		SM4Base.sm4_setkey_enc(ctx, key);
		return SM4Base.sm4_crypt_ecb(ctx, plainText);
	}
	
	@Override
	public byte[] decrypt(byte[] cipherText,byte[] key,byte[] iv) throws Exception  {
		SM4_Context ctx = new SM4_Context();
		ctx.isPadding = true;
		ctx.mode = 0;
		SM4Base.sm4_setkey_dec(ctx, key);
		return SM4Base.sm4_crypt_ecb(ctx, cipherText);
	}
	
	public class SM4_Context {
		public int mode = 1;
		public int[] sk = new int[32];
		public boolean isPadding = true;
	}
	
	private static class SM4Base {
		
		private static void sm4_setkey_enc(SM4_Context ctx, byte[] key) throws Exception {
			if (ctx == null) {
				throw new Exception("ctx is null!");
			} else if (key != null && key.length == 16) {
				ctx.mode = 1;
				SM4Base.sm4_setkey(ctx.sk, key);
			} else {
				throw new Exception("key error!");
			}
		}
		
		private static byte[] sm4_crypt_ecb(SM4_Context ctx, byte[] input) throws Exception {
			if (input == null) {
				throw new Exception("input is null!");
			} else {
				if (ctx.isPadding && ctx.mode == 1) {
					input = SM4Base.padding(input, 1);
				}

				int length = input.length;
				ByteArrayInputStream bins = new ByteArrayInputStream(input);

				ByteArrayOutputStream bous;
				byte[] output;
				for (bous = new ByteArrayOutputStream(); length > 0; length -= 16) {
					output = new byte[16];
					byte[] out = new byte[16];
					bins.read(output);
					SM4Base.sm4_one_round(ctx.sk, output, out);
					bous.write(out);
				}

				output = bous.toByteArray();
				if (ctx.isPadding && ctx.mode == 0) {
					output = SM4Base.padding(output, 0);
				}

				bins.close();
				bous.close();
				return output;
			}
		}
		
		
		
		
		
		
		
		
		
		private static void sm4_setkey(int[] SK, byte[] key) {
			int[] MK = new int[4];
			int[] k = new int[36];
			int i = 0;
			MK[0] = SM4Base.GET_ULONG_BE(key, 0);
			MK[1] = SM4Base.GET_ULONG_BE(key, 4);
			MK[2] = SM4Base.GET_ULONG_BE(key, 8);
			MK[3] = SM4Base.GET_ULONG_BE(key, 12);
			k[0] = MK[0] ^ FK[0];
			k[1] = MK[1] ^ FK[1];
			k[2] = MK[2] ^ FK[2];

			for (k[3] = MK[3] ^ FK[3]; i < 32; ++i) {
				k[i + 4] = k[i] ^ SM4Base.sm4CalciRK(k[i + 1] ^ k[i + 2] ^ k[i + 3] ^ CK[i]);
				SK[i] = k[i + 4];
			}
		}
		
		private static int GET_ULONG_BE(byte[] b, int i) {
			int n = (b[i] & 255) << 24 | (b[i + 1] & 255) << 16 | (b[i + 2] & 255) << 8 | b[i + 3] & 255 & -1;
			return n;
		}
		
		private static int sm4CalciRK(int ka) {
			//boolean bb = false;
			//boolean rk = false;
			byte[] a = new byte[4];
			byte[] b = new byte[4];
			SM4Base.PUT_ULONG_BE(ka, a, 0);
			b[0] = SM4Base.sm4Sbox(a[0]);
			b[1] = SM4Base.sm4Sbox(a[1]);
			b[2] = SM4Base.sm4Sbox(a[2]);
			b[3] = SM4Base.sm4Sbox(a[3]);
			int bb1 = SM4Base.GET_ULONG_BE(b, 0);
			int rk1 = bb1 ^ SM4Base.ROTL(bb1, 13) ^ SM4Base.ROTL(bb1, 23);
			return rk1;
		}
		
		private static void PUT_ULONG_BE(int n, byte[] b, int i) {
			b[i] = (byte) (255 & n >> 24);
			b[i + 1] = (byte) (255 & n >> 16);
			b[i + 2] = (byte) (255 & n >> 8);
			b[i + 3] = (byte) (255 & n);
		}
		
		private static byte sm4Sbox(byte inch) {
			int i = inch & 255;
			byte retVal = SboxTable[i];
			return retVal;
		}
		
		private static int ROTL(int x, int n) {
			return SM4Base.SHL(x, n) | x >> 32 - n;
		}
		
		private static int SHL(int x, int n) {
			return (x & -1) << n;
		}
		
		private static final byte[] SboxTable = new byte[]{-42, -112, -23, -2, -52, -31, 61, -73, 22, -74, 20, -62, 40, -5,
				44, 5, 43, 103, -102, 118, 42, -66, 4, -61, -86, 68, 19, 38, 73, -122, 6, -103, -100, 66, 80, -12, -111,
				-17, -104, 122, 51, 84, 11, 67, -19, -49, -84, 98, -28, -77, 28, -87, -55, 8, -24, -107, -128, -33, -108,
				-6, 117, -113, 63, -90, 71, 7, -89, -4, -13, 115, 23, -70, -125, 89, 60, 25, -26, -123, 79, -88, 104, 107,
				-127, -78, 113, 100, -38, -117, -8, -21, 15, 75, 112, 86, -99, 53, 30, 36, 14, 94, 99, 88, -47, -94, 37, 34,
				124, 59, 1, 33, 120, -121, -44, 0, 70, 87, -97, -45, 39, 82, 76, 54, 2, -25, -96, -60, -56, -98, -22, -65,
				-118, -46, 64, -57, 56, -75, -93, -9, -14, -50, -7, 97, 21, -95, -32, -82, 93, -92, -101, 52, 26, 85, -83,
				-109, 50, 48, -11, -116, -79, -29, 29, -10, -30, 46, -126, 102, -54, 96, -64, 41, 35, -85, 13, 83, 78, 111,
				-43, -37, 55, 69, -34, -3, -114, 47, 3, -1, 106, 114, 109, 108, 91, 81, -115, 27, -81, -110, -69, -35, -68,
				127, 17, -39, 92, 65, 31, 16, 90, -40, 10, -63, 49, -120, -91, -51, 123, -67, 45, 116, -48, 18, -72, -27,
				-76, -80, -119, 105, -105, 74, 12, -106, 119, 126, 101, -71, -15, 9, -59, 110, -58, -124, 24, -16, 125, -20,
				58, -36, 77, 32, 121, -18, 95, 62, -41, -53, 57, 72};
		private static final int[] FK = new int[]{-1548633402, 1453994832, 1736282519, -1301273892};
		private static final int[] CK = new int[]{462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679,
				-1464879427, -993275175, -521670923, -66909679, 404694573, 876298825, 1347903077, 1819507329, -2003855715,
				-1532251463, -1060647211, -589042959, -117504499, 337322537, 808926789, 1280531041, 1752135293, -2071227751,
				-1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 1213159005, 1684763257};
		
		
		private static byte[] padding(byte[] input, int mode) {
			if (input == null) {
				return null;
			} else {
				byte[] ret = (byte[]) null;
				if (mode == 1) {
					int p = 16 - input.length % 16;
					ret = new byte[input.length + p];
					System.arraycopy(input, 0, ret, 0, input.length);

					for (int i = 0; i < p; ++i) {
						ret[input.length + i] = (byte) p;
					}
				} else {
					byte arg5 = input[input.length - 1];
					ret = new byte[input.length - arg5];
					System.arraycopy(input, 0, ret, 0, input.length - arg5);
				}

				return ret;
			}
		}
		
		private static void sm4_one_round(int[] sk, byte[] input, byte[] output) {
			int i = 0;
			int[] ulbuf = new int[36];
			ulbuf[0] = SM4Base.GET_ULONG_BE(input, 0);
			ulbuf[1] = SM4Base.GET_ULONG_BE(input, 4);
			ulbuf[2] = SM4Base.GET_ULONG_BE(input, 8);

			for (ulbuf[3] = SM4Base.GET_ULONG_BE(input, 12); i < 32; ++i) {
				ulbuf[i + 4] = SM4Base.sm4F(ulbuf[i], ulbuf[i + 1], ulbuf[i + 2], ulbuf[i + 3], sk[i]);
			}

			SM4Base.PUT_ULONG_BE(ulbuf[35], output, 0);
			SM4Base.PUT_ULONG_BE(ulbuf[34], output, 4);
			SM4Base.PUT_ULONG_BE(ulbuf[33], output, 8);
			SM4Base.PUT_ULONG_BE(ulbuf[32], output, 12);
		}
		
		private static int sm4F(int x0, int x1, int x2, int x3, int rk) {
			return x0 ^ SM4Base.sm4Lt(x1 ^ x2 ^ x3 ^ rk);
		}
		
		private static int sm4Lt(int ka) {
			//boolean bb = false;
			//boolean c = false;
			byte[] a = new byte[4];
			byte[] b = new byte[4];
			SM4Base.PUT_ULONG_BE(ka, a, 0);
			b[0] = SM4Base.sm4Sbox(a[0]);
			b[1] = SM4Base.sm4Sbox(a[1]);
			b[2] = SM4Base.sm4Sbox(a[2]);
			b[3] = SM4Base.sm4Sbox(a[3]);
			int bb1 = SM4Base.GET_ULONG_BE(b, 0);
			int c1 = bb1 ^ SM4Base.ROTL(bb1, 2) ^ SM4Base.ROTL(bb1, 10) ^ SM4Base.ROTL(bb1, 18) ^ SM4Base.ROTL(bb1, 24);
			return c1;
		}
		
		
		private static void sm4_setkey_dec(SM4_Context ctx, byte[] key) throws Exception {
			if (ctx == null) {
				throw new Exception("ctx is null!");
			} else if (key != null && key.length == 16) {
				//boolean i = false;
				ctx.mode = 0;
				SM4Base.sm4_setkey(ctx.sk, key);

				for (int arg3 = 0; arg3 < 16; ++arg3) {
					SM4Base.SWAP(ctx.sk, arg3);
				}

			} else {
				throw new Exception("key error!");
			}
		}
		
		private static void SWAP(int[] sk, int i) {
			int t = sk[i];
			sk[i] = sk[31 - i];
			sk[31 - i] = t;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
