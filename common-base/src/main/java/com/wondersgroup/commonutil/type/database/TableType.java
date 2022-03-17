package com.wondersgroup.commonutil.type.database;

import java.util.HashMap;
import java.util.Map;

import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.commonutil.type.CommonType;

/**
 * 操作数据库表类型
 * 表名
 * 
 * 产生实例可以通过下面方式
 * TableType.getTableType("表名") 
 */
public class TableType implements CommonType{
	
	private String schema;//数据库用户
	
	private String tableName;//对应数据库表名,可以使用 schema.tableName 的形式

	@Override
	public String getTypeName() {
		return "Table";
	}
	
	private TableType (String tableName,String schema) {
		this.tableName = tableName;
		this.schema = schema;
	}

	/**
	 * 获取表名
	 */
	public String getTableName() {
		if (null != schema && !StringPool.BLANK.equals(schema) && !tableName.contains(".")) {
			return schema.concat(".").concat(tableName);
		} else {
			return tableName;
		}
	}
	
	/**
	 * 通过表名返回 TableType 
	 * @param tableName 如果带有schema， tableName可以是 schema.tableName的形式
	 * @return
	 */
	public static TableType getTableType(String tableName){
		tableName = tableName.toUpperCase();
		
		TableType tableType = tableTypeMap.get(tableName);
		if(null == tableType){
			//TODO 验证表名合法性   tableName
			
			int schemaSplit = tableName.indexOf(".");
			String schema = StringPool.BLANK;
			if (schemaSplit > 0) {
				schema = tableName.substring(0,tableName.indexOf("."));
			} 
			tableType = new TableType(tableName,schema);
		}
		return tableType;
	}
	
	/**
	 * 据库表类型 池
	 * 线程安全map
	 * 将数据库操作成功的表的 TableType 放入缓存表池
	 * commonDao使用时只保存默认数据源的TableType
	 */
	private static final Map<String, TableType> tableTypeMap = new HashMap<String, TableType>();
	
	/**
	 * 移除表类型池中的 表
	 * @param tableType
	 */
	public static void delTableType(TableType tableType){
		tableTypeMap.remove(tableType.getTableName());
	}
	
	/**
	 * commonDao调用，执行成功的表类型 放入表类型池
	 * @param tableType
	 */
	public static void putTableType(TableType tableType){
		if (tableTypeMap.containsKey(tableType.getTableName())){
			return;
		}
		tableTypeMap.put(tableType.getTableName(), tableType);
	}
	
}
