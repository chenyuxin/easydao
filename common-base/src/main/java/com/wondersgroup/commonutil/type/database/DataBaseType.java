package com.wondersgroup.commonutil.type.database;

import java.util.Map;

import com.wondersgroup.commondao.dao.daoutil.DaoUtil;
import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.commonutil.type.CommonType;

/**
 * 数据库类型
 */
public enum DataBaseType implements CommonType {
	ORACLE {
		@Override
		public String getJdbcDriverClassName() {
			return DaoUtil.OracleJdbcDriverClassName1;
		}

		@Override
		public String getJdbcUrl(String ip, int port, String instanceName) {
			StringBuffer jdbcUrl = new StringBuffer("jdbc:oracle:thin:@");
			jdbcUrl.append(ip).append(StringPool.COLON).append(port).append(StringPool.FORWARD_SLASH).append(instanceName);
			return jdbcUrl.toString();
		}
	},
	
	MYSQL {
		@Override
		public String getJdbcDriverClassName() {
			return DaoUtil.MysqlJdbcDriverClassName1;
		}
		
		@Override
		public String getJdbcUrl(String ip, int port, String instanceName) {
			StringBuffer jdbcUrl = new StringBuffer("jdbc:mysql://");
			jdbcUrl.append(ip).append(StringPool.COLON).append(port).append(StringPool.FORWARD_SLASH).append(instanceName);
			jdbcUrl.append("?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8");
			return jdbcUrl.toString();
		}
	},
	
	POSTGREPSQL {
		@Override
		public String getJdbcDriverClassName() {
			return DaoUtil.PostgreJdbcDriverClassName1;
		}
		
		@Override
		public String getJdbcUrl(String ip, int port, String instanceName) {
			StringBuffer jdbcUrl = new StringBuffer("jdbc:postgresql://");
			jdbcUrl.append(ip).append(StringPool.COLON).append(port).append(StringPool.FORWARD_SLASH).append(instanceName);
			return jdbcUrl.toString();
		}
	},
	
	;
	
	//private static Logger logger = LoggerFactory.getLogger(DataBaseType.class);
	
	DataBaseType() {
		DataBaseTypeTemp dataBaseTypeTemp = getTempEnum();
		for (FieldType fieldType : FieldType.values()){//数据库类型确定时，初始化 字段类型
			fieldType.setFieldTypes(dataBaseTypeTemp);
		}
		this.dbColType = dataBaseTypeTemp.dbColType;
	}
	
	/**
	 * 获取 DataBaseTypeTemp枚举类型
	 * @return
	 */
	public final DataBaseTypeTemp getTempEnum(){
		return DataBaseTypeTemp.valueOf(this.name().concat("t"));
	}
	
	/**
	 * 数据库字段类型
	 */
	public Map<String, String> dbColType;
	
	@Override
	public String getTypeName() {
		return this.name();
	}
	
	/**
	 * 获取数据库驱动
	 * @return
	 */
	public abstract String getJdbcDriverClassName();
	
	/**
	 * 生成数据库的jdbcUrl
	 * @param ip 数据库ip 或 域名
	 * @param port 数据库端口
	 * @param instanceName 数据库实例名
	 * @return
	 */
	public abstract String getJdbcUrl(String ip,int port,String instanceName);
	
	/**
	 * 默认数据库的全局配置
	 * 
	 * 程序中不适合变更数据库类型
	 */
	private static DataBaseTypeTemp currentDataBaseType = null;

	/**
	 * 设置默认数据源的数据库类型
	 * 
	 * !!!需要程序启动的时候初始化CommonTypeCaches处的初始化方法，传入正确的数据库类型。
	 * 本框架的commonDao在程序启动时 InitCommonType 会自动初始化本方法
	 * 
	 * 建议避免本方法重复执行
	 */
	public static void setCurrentDataBaseType(DataBaseType dataBaseType) {
		if (null == currentDataBaseType) {
			currentDataBaseType = dataBaseType.getTempEnum();
		} else {
			//logger.warn("currentDataBaseType当前数据库类型 已经被设置");
			System.out.println("currentDataBaseType当前数据库类型 已经被设置");
		}
	}

	/**
	 * 获取当前使用的数据库类型
	 */
	public static DataBaseType getCurrentDataBaseType() {
		if (null == currentDataBaseType) {
			return DataBaseType.ORACLE;//返回一个默认数据库类型
		} else {
			return currentDataBaseType.getEnum();
		}
	}
	
	/**
	 * 通过数据库驱动名获取数据库类型
	 * 从数据源获取信息
	 * @param driverClassName 数据库驱动名
	 * @return
	 */
	public static DataBaseType getDataBaseType(String driverClassName){
		for (DataBaseType dataBaseType : DataBaseType.values()) {
			if (dataBaseType.getJdbcDriverClassName().contains(driverClassName)) {
				return dataBaseType;
			}
		}
		return null;
	}
	

	
}
