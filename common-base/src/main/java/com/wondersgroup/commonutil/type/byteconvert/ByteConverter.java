package com.wondersgroup.commonutil.type.byteconvert;

import com.wondersgroup.commonutil.baseutil.BASE64Util;
import com.wondersgroup.commonutil.baseutil.HexUtil;
import com.wondersgroup.commonutil.type.CommonType;
import com.wondersgroup.commonutil.type.otherintf.EscapeType;
import com.wondersgroup.commonutil.type.otherintf.ParseType;

/**
 * 转换类型
 */
public enum ByteConverter implements CommonType, ParseType<String,byte[]>,EscapeType<byte[], String> {
	
	BASE64{
		
		@Override
		public String getTypeName() {
			return "BASE64Type";
		}	
		
		@Override
		public String parse(byte[] value) {
			return BASE64Util.encryptBASE64(value);
		}

		@Override
		public byte[] escape(String value) {
			return BASE64Util.decryptBASE64B(value);
		}
		
	},
	
	/**
	 * 16进制大写
	 */
	HEX{

		@Override
		public String getTypeName() {
			return "HEXType";
		}

		@Override
		public String parse(byte[] value) {
			return HexUtil.toHexString(value);
		}

		@Override
		public byte[] escape(String value) {
			return HexUtil.hexStringToBytes(value);
		}
		
	},
	
	/**
	 * 16进制小写
	 */
	Hex{

		@Override
		public String getTypeName() {
			return "HexType";
		}

		@Override
		public String parse(byte[] value) {
			return HexUtil.toHexString(value,false);
		}

		@Override
		public byte[] escape(String value) {
			return HexUtil.hexStringToBytes(value);
		}
		
	},
	
	;

	/**
	 * 将类型字符串结果互转
	 * @param byteConverter 原类型
	 * @param value 原类型字符串
	 * @return 本类型字符串
	 */
	public String getConverter(ByteConverter byteConverter,String value) {
		return this.parse(byteConverter.escape(value));
	}
 	
 	
}
