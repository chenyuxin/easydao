package com.wondersgroup.commonutil.type.database;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库类型 与 数据库字段类型 关系的中间替身, 放入dbColType
 * 
 * 枚举名后缀加上‘t’
 */
enum DataBaseTypeTemp {
	
	ORACLEt,MYSQLt,POSTGREPSQLt;
	
	/**
	 * 数据库字段类型
	 */
	public final Map<String, String> dbColType = new HashMap<String, String>();
	
	/**
	 * 获取对应 DataBaseType枚举类
	 * @return
	 */
	public final DataBaseType getEnum(){
		int endIndex = this.name().length()-1;
		return DataBaseType.valueOf(this.name().substring(0, endIndex));
	}
	
	
	
	
	
}
