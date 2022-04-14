package com.wondersgroup.commonutil.type.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Base64Utils;

import com.wondersgroup.commonutil.baseutil.BaseUtil;
import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.commonutil.type.CommonType;
import com.wondersgroup.commonutil.type.format.DateType;
import com.wondersgroup.commonutil.type.otherintf.ParseType;

/**
 * 字段类型用于同数据库交互
 *
 */
@SuppressWarnings("rawtypes")
public enum FieldType implements CommonType,ParseType {
	
	/**
	 * java程序的类型
	 * String
	 */
	STRING {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "varchar2");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "varchar");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "varchar");
				break;

			default:
				break;
			}
		}

		@Override
		public String parse(Object value) {
			if (value == null) {
				//value = "-";//替换null值
				return null;
			}
			return String.valueOf(value);
		}
		
		@Override
		public Class getJavaClass() {
			return String.class;
		}
		
	},
	
	/**
	 * java程序的类型
	 * Inter,int
	 */
	INT {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "number");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "int");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "int4");
				break;

			default:
				break;
			}
			
		}

		@Override
		public Integer parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = -1;//替换null值
				return null;
			} else {
				if (value instanceof Integer) {
					return (Integer) value;
				} else {
					return Integer.parseInt(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Integer.class;
		}
		
	},
	
	/**
	 * java程序的类型
	 * Float,float
	 */
	FLOAT {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "number");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "float");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "float4");
				break;

			default:
				break;
			}
			
		}

		@Override
		public Float parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = 0.0;
				return null;
			} else {
				if (value instanceof Float) {
					return (Float) value;
				} else {
					return Float.parseFloat(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Float.class;
		}
	},
	
	/**
	 * java程序的类型
	 * Date
	 */
	DATE {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "date");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "datetime");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "timestamp");
				break;

			default:
				break;
			}
			
		}

		@Override
		public Date parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = DateType.getInstance().getParseValue("1900-01-01");//替换null值
				return null;
			} else {
				if (value instanceof Date) {
					return (Date) value;
				} else {
					return DateType.getInstance().getParseValue(String.valueOf(value));
				}
			}
		}
		
		
		@Override
		public Class getJavaClass() {
			return Date.class;
		}
		
	},
	
	/**
	 * java程序的类型
	 * Long，long
	 */
	LONG {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "number");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "bigint");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "int8");
				break;

			default:
				break;
			}
			
		}

		@Override
		public Long parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = -1L;
				return null;
			} else {
				if (value instanceof Long) {
					return (Long) value;
				} else {
					return Long.parseLong(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Long.class;
		}
	},
	
	/**
	 * java程序的类型
	 * Double,double
	 */
	DOUBLE {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "number");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "double");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "float8");
				break;

			default:
				break;
			}
			
		}

		@Override
		public Double parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = 0.0;
				return null;
			} else {
				if (value instanceof Double) {
					return (Double) value;
				} else {
					return Double.parseDouble(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Double.class;
		}
	},
	
	/**
	 * java程序的类型
	 * String
	 * 
	 * 对应数据库nvarchar2
	 */
	STRINGN {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "nvarchar2");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "nvarchar");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "nvarchar");
				break;

			default:
				break;
			}
		}
		
		@Override
		public String parse(Object value) {
			if (value == null) {
				//value = "-";//替换null值
				return null;
			}
			return String.valueOf(value);
		}

		@Override
		public Class getJavaClass() {
			return null;
		}
		
	},
	
	/**
	 * java程序的类型
	 * String
	 * 
	 * 对应数据库clob
	 */
	STRINGC {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "clob");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "text");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "text");
				break;

			default:
				break;
			}
		}
		
		@Override
		public String parse(Object value) {
			if (value == null) {
				//value = "-";//替换null值
				return null;
			}
			return String.valueOf(value);
		}

		@Override
		public Class getJavaClass() {
			return null;
		}
		
	},
	
	/**
	 * java程序的类型
	 * byte[]
	 * 
	 * 对应数据库blob
	 */
	BYTE {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "blob");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "blob");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "bytea");
				break;

			default:
				break;
			}
			
		}

		@Override
		public byte[] parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = [];
				return null;
			} else {
				if (value instanceof byte[]) {
					return (byte[]) value;
				} else if (value instanceof String) {
					String str = String.valueOf(value);
					if (BaseUtil.isBase64(str)) {
						//传输过来的byte[]转json时默认转为 Base64的字符串,
						return Base64Utils.decodeFromString(str);
					} else {
						return str.getBytes();
					}
				} 
			}
			return null;
		}
		
		@Override
		public Class getJavaClass() {
			return byte[].class;
		}
	},
	
	BIGDECIMAL {
		@Override
		protected void setFieldTypes(DataBaseTypeTemp dataBaseType) {
			switch (dataBaseType) {
			case ORACLEt:
				dataBaseType.dbColType.put(this.name(), "number");
				break;
			case MYSQLt:
				dataBaseType.dbColType.put(this.name(), "decimal");
				break;
			case POSTGREPSQLt:
				dataBaseType.dbColType.put(this.name(), "numeric");
				break;

			default:
				break;
			}
			
		}

		@Override
		public BigDecimal parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = 0;
				return null;
			} else {
				if (value instanceof BigDecimal) {
					return (BigDecimal) value;
				} else {
					return new BigDecimal(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return BigDecimal.class;
		}
	}
	
	;
	
	
	/**
	 * 设置数据库字段类型， 数据库类型 枚举类初始化后调用方法， 并赋值 dbColType
	 */
	protected abstract void setFieldTypes(DataBaseTypeTemp dataBaseType);
	
	/**
	 * 获取
	 * 对应代表的Java类型
	 */
	public abstract Class getJavaClass();
	

	@Override
	public String getTypeName() {
		return this.name();
	}
	
	/**
	 * 字段类型编号
	 * 
	 * 字段类型编号会存入数据库中保存，请勿调换枚举顺序
	 * @return
	 */
	public int getModelColType(){
		return ordinal();
	}

	/**
	 * 获取对应数据库字段类型名称
	 * 使用全局默认的数据库类型
	 */
	public String getDbColType(){
		return DataBaseType.getCurrentDataBaseType().dbColType.get(this.name());
	}
	
	/**
	 * 获取对应数据库字段类型名称
	 * 需要特定数据库字段类型名称可 加入数据库类型传参。
	 */
	public String getDbColType(DataBaseType dataBaseType){
		return dataBaseType.dbColType.get(this.name());
	}
	
	
	/**
	 * 返回对应顺序的字段类型，
	 * 顺序可以由this.ordinal()获取
	 * @param order
	 * @return
	 */
	public static FieldType getFieldType(int order) {
		return FieldType.values()[order];
	}
	
	
	private static final Map<Integer, String> dataTypeMap = new HashMap<Integer, String>();
	private static final Map<Integer, String> dataTypeJavaMap = new HashMap<Integer, String>();
	private static final Map<Integer, String> dataTypeStringMap = new HashMap<Integer, String>();
	private static final List<Map<String, Object>> dataTypes = new ArrayList<Map<String, Object>>(FieldType.values().length);
	
	/**
	 * 模型的数据类型配置 Map
	 * @return
	 */
	public static Map<Integer, String> getDataTypeMap(){
		if (dataTypeMap.isEmpty()) {
			synchronized (dataTypeMap) {
				if (dataTypeMap.isEmpty()) {
					for (FieldType fieldType : FieldType.values()){
						dataTypeMap.put(fieldType.getModelColType(), fieldType.getDbColType());
					}
				}
			}
		}
		return dataTypeMap;
	}
	
	/**
	 * 模型的数据类型配置 Map 数据类型为java类型
	 * @return
	 */
	public static Map<Integer, String> getDataTypeJavaMap(){
		if (dataTypeJavaMap.isEmpty()) {
			synchronized (dataTypeJavaMap) {
				if (dataTypeJavaMap.isEmpty()) {
					for (FieldType fieldType : FieldType.values()){
						dataTypeJavaMap.put(fieldType.getModelColType(), fieldType.name());
					}
				}
			}
		}
		return dataTypeJavaMap;
	}

	/**
	 * 获取所有字符串类型的dataTypes
	 * @return
	 */
	public static Map<Integer, String> getDataTypeStringMap() {
		if (dataTypeStringMap.isEmpty()) {
			synchronized (dataTypeStringMap) {
				if (dataTypeStringMap.isEmpty()) {
					for (FieldType fieldType : FieldType.values()){
						if (fieldType.name().contains(STRING.name())) {//包含STRING开头的都是String类型
							dataTypeStringMap.put(fieldType.getModelColType(), fieldType.name());
						}
					}
				}
			}
		}
		return dataTypeStringMap;
	}
	
	/**
	 * 获取所有支持的数据类型List
	 * 
	 *  private int modelColType;//数据类型代码 int
		private String datatype;//对应数据库的使用的 数据类型名称
		private String javaDatatype;// 对应Java程序使用的数据类型名称
	 */
	public static List<Map<String,Object>> getDataTypes(){
		if ( dataTypes.isEmpty() ){
			synchronized ( dataTypes ) {
				if ( dataTypes.isEmpty() ) {
					for (FieldType fieldType : FieldType.values()) {
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("modelColType", fieldType.getModelColType());
						map.put("datatype", fieldType.getDbColType());
						map.put("javaDatatype", fieldType.name());
						dataTypes.add(map); 
					}
				}
				
			}
		}
		return dataTypes;
	}

	/**
	 * 如果是基本类型，返回字段类型
	 * @param clazz
	 * @return
	 */
	public static FieldType getFieldType(Class<?> clazz) {
		for(FieldType fieldType : FieldType.values()) {
			if( fieldType.getJavaClass() == clazz ) {
				return fieldType;
			}
		}
		return null;
	}
	

	
	
	
	

}
