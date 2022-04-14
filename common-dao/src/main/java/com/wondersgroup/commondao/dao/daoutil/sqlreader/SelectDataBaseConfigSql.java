package com.wondersgroup.commondao.dao.daoutil.sqlreader;

import com.wondersgroup.commonutil.type.database.DataBaseType;

/**
 * 查询数据库基本信息的sql
 */
public class SelectDataBaseConfigSql {
	
	private static final String POSTGREPSQL_getPrimaryKeySql = 
			  "		select pg_attribute.attname as pk from \r\n"
			+ "		pg_constraint  inner join pg_class \r\n"
			+ "		on pg_constraint.conrelid = pg_class.oid \r\n"
			+ "		inner join pg_attribute on pg_attribute.attrelid = pg_class.oid \r\n"
			+ "		and  pg_attribute.attnum = pg_constraint.conkey[1] \r\n"
			+ "		where pg_class.relname = :tableName \r\n"
			+ "		and pg_constraint.contype='p' ";
	
	private static final String MYSQL_getPrimaryKeySql = "SELECT distinct column_name \r\n"
			+ "  FROM INFORMATION_SCHEMA.`KEY_COLUMN_USAGE` \r\n"
			+ " WHERE table_name= :tableName \r\n"
			//+ "AND CONSTRAINT_SCHEMA='partdb1' \r\n"
			+ " AND constraint_name='PRIMARY'" ;
	
	/**
	 * sql语句用法获取当前表的主键，查询数据库的配置。
	 * @param tableName 表名
	 * @param dataBaseType 数据库类型
	 * @return sql语句
	 */
	public static String getPrimaryKeySql(String tableName, DataBaseType dataBaseType) {
		if (dataBaseType == DataBaseType.POSTGREPSQL) {
			return POSTGREPSQL_getPrimaryKeySql;
		} else if (dataBaseType == DataBaseType.MYSQL) {
			return MYSQL_getPrimaryKeySql;
		}
		
		return null;
	}

}
