package com.wondersgroup.otherutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.wondersgroup.commonutil.CommonUtilString;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.commonutil.type.database.FieldType;
import com.wondersgroup.commonutil.type.language.JSONType;

public class TestType {
	
	public static void main(String[] args) {
		Map<String, String> aMap = DataBaseType.ORACLE.dbColType;
		System.out.println(aMap);
	}

	@Test
	public void test1(){
		
		String name = FieldType.STRING.getTypeName();
		int modelColType = FieldType.STRING.getModelColType();
		
		String name1 = FieldType.INT.getTypeName();
		int modelColType1 = FieldType.INT.getModelColType();
		
		String name2 = FieldType.FLOAT.getTypeName();
		int modelColType2 = FieldType.FLOAT.getModelColType();
		
		
		System.out.println("name: "+name+", modelColType:"+modelColType);
		System.out.println("name: "+name1+", modelColType:"+modelColType1);
		System.out.println("name: "+name2+", modelColType:"+modelColType2);
		
		Map<String, String> aMap = DataBaseType.ORACLE.dbColType;
		System.out.println(aMap);
		
		
		String aString  = FieldType.STRING.getDbColType();
		System.out.println(aString);
		String fString1  = FieldType.FLOAT.getDbColType();
		System.out.println(fString1);
		
		DataBaseType.setCurrentDataBaseType(DataBaseType.MYSQL);
		
		String mString  = FieldType.STRING.getDbColType();
		System.out.println(mString);
		String fString  = FieldType.FLOAT.getDbColType();
		System.out.println(fString);
		String fString2  = FieldType.FLOAT.getDbColType(DataBaseType.ORACLE);
		System.out.println(fString2);
		String fString3  = FieldType.FLOAT.getDbColType();
		System.out.println(fString3);
		
		
		System.out.println("DataTypeMap: " + FieldType.getDataTypeMap());
		System.out.println("DataTypeJavaMap: " + FieldType.getDataTypeJavaMap());
		
	}
	
	
	
	@Test
	public void test2(){
		String oString = "{\"test\":#1[{\"XingMing\":\"#{XingMing}\",\"QianFaRiQi\":\"#{QianFaRiQi}\"}]# }";
		List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
		Map<String, Object> param1 = new HashMap<String, Object>();
		param1.put("XingMing", "姓名1");
		param1.put("QianFaRiQi", new Date());
		Map<String, Object> param2 = new HashMap<String, Object>();
		param2.put("XingMing", "姓名2");
		param2.put("QianFaRiQi", new Date());
		
		params.add(param1);
		params.add(param2);
		
		String aString = CommonUtilString.setVariableParams(oString,params,JSONType.getInstance());
		System.out.println(aString);
	}

}
