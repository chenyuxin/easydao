package com.wondersgroup.commondao.dao.daoutil;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class DaoConfResource {
	
	/**
	 * commonDao是否输出sql
	 */
	private static boolean showSql;
	
	/**
	 * commonDao是否输出sql
	 */
	public boolean isShowSql() {
		return showSql;
	}
	@Value("${dao.showSql:false}")
	public void setShowSql(boolean showSql) {
		DaoConfResource.showSql = showSql;
	}
	
	/**
	 * qureyDataCache是否开启commonDao的查询结果缓存
	 */
	private static boolean qureyDataCache;
	
	/**
	 * qureyDataCache是否开启commonDao的查询结果缓存
	 */
	public boolean isQureyDataCache() {
		return qureyDataCache;
	}
	@Value("${dao.qureyDataCache:false}")
	public void setQureyDataCache(boolean qureyDataCache) {
		DaoConfResource.qureyDataCache = qureyDataCache;
	}

	/**
	 * 查询结果缓存时间(单位秒s),默认5分钟
	 * 数据缓存期内数据库存在数据变更时可能存在脏读，请合理设置数据缓存和缓存时间
	 */
	private static int queryDateCacheTime;
	/**
	 * 查询结果缓存时间
	 * 数据缓存期内数据库存在数据变更时可能存在脏读，请合理设置数据缓存和缓存时间
	 */
	public int getQueryDateCacheTime() {
		return queryDateCacheTime;
	}
	@Value("${dao.queryDateCacheTime:300}")
	public void setQueryDateCacheTime(int queryDateCacheTime) {
		DaoConfResource.queryDateCacheTime = queryDateCacheTime;
	} 
	/**
	 * 查询结果缓存最大记录数,默认500条
	 */
	private static int queryDateCacheMaxSize = 500;
	/**
	 * 查询结果缓存最大记录数,默认500条
	 */
	public int getQueryDateCacheMaxSize() {
		return queryDateCacheMaxSize;
	}
	@Value("${dao.queryDateCacheMaxSize:500}")
	public void setQueryDateCacheMaxSize(int queryDateCacheMaxSize) {
		DaoConfResource.queryDateCacheMaxSize = queryDateCacheMaxSize;
	}
	
	
	
	
	
	
	
	
	
	
	

}


