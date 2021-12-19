package com.wondersgroup.commondao.dao.daoutil.toolentity;

import java.util.Date;
import java.util.Map;

import com.wondersgroup.commondao.dao.daoutil.SqlReader;
import com.wondersgroup.commonutil.MyObj;
import com.wondersgroup.commonutil.baseutil.BaseUtil;

/**
 * 查询缓存对象
 * @param <T>
 */
public class QureyData4Cache<T> {
	

	/**
	 * 查询结果数据，放入缓存
	 */
	private T qureyData;
	
	/**
	 * 数据源
	 */
	private String dataSourceName;
	
	/**
	 * sql语句
	 */
	private String sql;
	
	/**
	 * 查询传参
	 */
	private Map<String,Object> paramMap;
	
	/**
	 * 查询时的时间
	 */
	private long qureyTime;
	
	/**
	 * 查询缓存里的key
	 */
	private String key;
	
	/**
	 * 查询里涉及的表名
	 */
	private String[] tableNames;
	
	/**
	 * 
	private int order;
	 */
	
	public QureyData4Cache(T qureyData,String sql,Map<String,Object> paramMap,String dataSourceName) {
		this.qureyData = MyObj.deepClone(qureyData);
		this.sql = sql;
		this.paramMap = paramMap;
		this.dataSourceName = dataSourceName;
		this.qureyTime = new Date().getTime();
		this.key = getQueryKey();
		this.tableNames = SqlReader.getTablesFromSql(sql);
	}

	/**
	 * 获取查询结果缓存key
	 * @return
	 */
	public String getQueryKey() {
		StringBuffer sBuffer = new StringBuffer(this.sql);
		if ( null != this.paramMap ) {
			if ( !this.paramMap.isEmpty() ){
				sBuffer.append(this.paramMap);
			}
		}
		if (null != this.dataSourceName && !"".equals(this.dataSourceName) ) {
			sBuffer.append(this.dataSourceName);
		}
		return BaseUtil.getUUIDC(sBuffer.toString());//根据字符串生成固定id
	}
	
	/**
	 * 放入缓存的查询结果数据
	 * @throws Exception 
	 */
	public T getQureyData() {
		return MyObj.deepClone(this.qureyData);
	}

	/**
	 * 查询时的时间
	 */
	public long getQureyTime() {
		return qureyTime;
	}

	/**
	 * 设置查询时的时间
	public void setQureyTime(long qureyTime) {
		this.qureyTime = qureyTime;
	}
	 */

	/**
	 * 查询缓存里的key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 查询里涉及的表名
	 */
	public String[] getTableNames() {
		return tableNames;
	}

	/**
	 * 获取查询缓存里的查询数据源
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}
	
	
	
	
	
	
}
