package com.wondersgroup.commondao.dao.daofactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.sql.DataSource;

import java.util.Queue;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.wondersgroup.commondao.dao.daoutil.DaoConfResource;
import com.wondersgroup.commondao.dao.daoutil.DaoUtil;
import com.wondersgroup.commondao.dao.daoutil.toolentity.QureyData4Cache;
import com.wondersgroup.commonutil.type.CommonTypeCaches;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.commonutil.type.database.TableType;

@Component
public class DaoFactory implements InitializingBean {
	
	@Resource(name = "NamedParameterJdbcTemplate")//加载默认数据源
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired 
	private DaoConfResource daoConfResource;
	
	

	
	/**
	 * 执行初始化方法
	 */
	private static Boolean isInit = false;

	/**
	 * 初始化CommonType的内容
	 *
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		//System.out.println("init方法");
		synchronized (isInit) {
			if (isInit) {
				return;
			}
			
			CommonTypeCaches.init(this.getDataBaseType(DaoUtil.defaultDataSourceName));
			
			isInit = true;
		}
		
	}

	
	
	/**
	 * 查询结果缓存
	 * 数据
	 */
	private static final Map<String, QureyData4Cache<?>> QureyDataCacheMap = new HashMap<String, QureyData4Cache<?>>();
	
	/**
	 * 查询结果缓存key的队列
	 */
	private static final Queue<String> cacheMapKeyQueue = new LinkedList<String>();
	
	/**
	 * 最后一次检测 查询结果缓存
	 * 数据的时间
	 */
	private static long lastCheckQureyDataCacheMapTime = new Date().getTime();
	
	/**
	 *  查询结果缓存map 操作锁,安全放入和删除节点
	 */
	private final static byte[] lockCheckQureyDataCacheMap = new byte[0];

	/**
	 * 缓存map查看有无超时，移除超时缓存map
	 * @return
	 */
	private void checkQureyDataCacheMap() {
		long currentTime = new Date().getTime();
		if (lastCheckQureyDataCacheMapTime + 5000 > currentTime) {//5秒内检查过
			return;
		}
		int cacheTime = daoConfResource.getQueryDateCacheTime() * 1000;
		synchronized (lockCheckQureyDataCacheMap) {
			Iterator<Entry<String, QureyData4Cache<?>>> e = QureyDataCacheMap.entrySet().iterator();
			while (e.hasNext()) {
				Entry<String, QureyData4Cache<?>> cache = e.next();
				long firstQureyTime = cache.getValue().getQureyTime();
				if (currentTime > firstQureyTime + cacheTime ) {
					cacheMapKeyQueue.remove(cache.getKey());
					e.remove();//移除超时的缓存
					continue;
				} 
			}
		}
		lastCheckQureyDataCacheMapTime = currentTime;//完成处理后设置最后检查缓存时间
	}
	
	/**
	 * 获取查询结果缓存
	 * 数据
	 * @return
	 */
	public Map<String, QureyData4Cache<?>> getQureydatacachemap() {
		return QureyDataCacheMap;
	}
	
	/**
	 * 获取查询结果缓存key
	 * @param sql 
	 * @param paramMap
	 * @param dataSrouceName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getQueryKey(String sql,Map<String,Object> paramMap,String dataSrouceName) throws UnsupportedEncodingException {
		StringBuffer sBuffer = new StringBuffer(sql);
		if (null != paramMap ) {
			if (!paramMap.isEmpty()) {
				sBuffer.append(paramMap);
			}
		}
		if (null != dataSrouceName && !"".equals(dataSrouceName) ) {
			sBuffer.append(dataSrouceName);
		}
		return UUID.nameUUIDFromBytes(sBuffer.toString().getBytes("utf-8")).toString().replace("-","");//根据字符串生成固定id
	}
	
	/**
	 * 验证是否需要查询结果缓存，并放入查询结果缓存
	 * @param <T>
	 * @param qureyData4Cache
	 */
	public <T> void putQureyDataCache(T QureyData,String sql,Map<String,Object> paramMap,String dataSrouceName) {
		if (!daoConfResource.isQureyDataCache()) {
			return;//不开启查询结果缓存,直接返回
		}
		QureyData4Cache<T> qureyData4Cache = new QureyData4Cache<T>(QureyData,sql,paramMap,dataSrouceName);
		synchronized (lockCheckQureyDataCacheMap) {
			QureyDataCacheMap.put(qureyData4Cache.getKey(), qureyData4Cache);
			cacheMapKeyQueue.offer(qureyData4Cache.getKey());
		}	
		
		if (QureyDataCacheMap.size() > Integer.valueOf(daoConfResource.getQueryDateCacheMaxSize())) {
			checkQureyDataCacheMap();//缓存map查看有无超时，移除超时缓存map，返回最旧时间的一个缓存的key。
		}
		if (QureyDataCacheMap.size() > Integer.valueOf(daoConfResource.getQueryDateCacheMaxSize())) {
			synchronized (lockCheckQureyDataCacheMap) {
				String minTimeKey = cacheMapKeyQueue.poll();
				QureyDataCacheMap.remove(minTimeKey);//超过最大缓存数，移除最旧时间的缓存
			}
		}
		
		
	}
	
	/**
	 * 验证是否需要查询结果缓存，返回查询结果缓存数据
	 * @param <T>
	 * @param sql
	 * @param paramMap
	 * @param dataSrouceName
	 * @return
	 */
	public Object getQureyDataCache(String sql,Map<String,Object> paramMap,String dataSourceName) {
		if (!daoConfResource.isQureyDataCache()) {
			return null;//不开启查询结果缓存
		}
		checkQureyDataCacheMap();//缓存map查看有无超时，移除超时缓存map
		String key = "";
		try {
			key = getQueryKey(sql,paramMap,dataSourceName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		QureyData4Cache<?> QureyData4Cache = QureyDataCacheMap.get(key);
		if (null != QureyData4Cache ) {
			try {
				return QureyData4Cache.getQureyData();
			} catch (Exception e) {
				return null;
			}
		} 
		return null;
	}
	
	/**
	 * 本应用内操作表成功后，将清除对应表的查询缓存，避免脏读。
	 * 但是如果有非本应用频繁操作数据库中的表时，将无法避免缓存的脏读，请合理设置是否开启查询缓存，和缓存时间。
	 * @param tableNames
	 */
	public void checkQureyDataCacheMap(String dataSourceName,String... tableNames) {
		if (!daoConfResource.isQureyDataCache()) {
			return;//不开启查询结果缓存，不需要检查
		}
		long currentTime = new Date().getTime();
		int cacheTime = daoConfResource.getQueryDateCacheTime() * 1000;
		synchronized (lockCheckQureyDataCacheMap) {
			for (int i = 0; i<tableNames.length; i++ ) {
				String tableName = tableNames[i];
				//System.out.println("检查操作的表名开始:".concat(tableName).concat("-----------------------------------------------------"));
				Iterator<Entry<String, QureyData4Cache<?>>> e = QureyDataCacheMap.entrySet().iterator();
				while (e.hasNext()) {
					Entry<String, QureyData4Cache<?>> cache = e.next();
					long firstQureyTime = cache.getValue().getQureyTime();
					if (currentTime > firstQureyTime + cacheTime ) {
						cacheMapKeyQueue.remove(cache.getKey());
						e.remove();//移除超时的缓存
						continue;
					}
					
					//TODO 除了添加 数据源一致的判断，还要添加schema一致的判断
					if (!dataSourceName.equals(cache.getValue().getDataSourceName())) {
						continue;
					}
					String[] queryTableNames = cache.getValue().getTableNames();
					for (String queryTableName : queryTableNames) {
						//System.out.println("检查查询表名:".concat(queryTableName));
						if( tableName.substring(tableName.lastIndexOf('.')==-1?0:tableName.lastIndexOf('.')).trim().toUpperCase()
								.equals(queryTableName.substring(queryTableName.lastIndexOf('.')==-1?0:queryTableName.lastIndexOf('.')).trim().toUpperCase() ) ) {
							cacheMapKeyQueue.remove(cache.getKey());
							e.remove();//移除操作过表的缓存
							//System.out.println("移出:".concat(queryTableName));
							continue;
						}
					}
					
				}
				//System.out.println("检查操作的表名结束:".concat(tableName).concat("-----------------------------------------------------"));
			}
		}
		lastCheckQureyDataCacheMapTime = currentTime;//完成处理后设置最后检查缓存时间
		
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 数据源缓存 
	 */
	private final static Map<String,NamedParameterJdbcTemplate> cacheJdbcTemplates = new HashMap<String,NamedParameterJdbcTemplate>();
	/**
	 * 使用自定义数据源，不传值使用默认的数据源
	 * @param dataSourceName 数据源在spring配置文件bean的id
	 */
	public NamedParameterJdbcTemplate moreJdbcTemplate(String dataSourceName) {
		if ("".equals(dataSourceName) || null == dataSourceName) {
			return jdbcTemplate;//dataSourceName为空时,返回默认的jdbcTemplate
		}
		NamedParameterJdbcTemplate currentJdbcTemplate = cacheJdbcTemplates.get(dataSourceName);
		if (null != currentJdbcTemplate) {
			return currentJdbcTemplate;
		} else {
			DataSource dataSource = (DataSource)applicationContext.getBean(dataSourceName); 
			cacheJdbcTemplates.put(dataSourceName, new NamedParameterJdbcTemplate(dataSource));
			
			return cacheJdbcTemplates.get(dataSourceName);
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 上一次打印的sql
	 */
	private static String lastSql = "";
	/**
	 * 根据配置是否,打印sql
	 * @param sql
	 * @param showSql
	 */
	public void printSql(String sql){
		if (daoConfResource.isShowSql()){
			if (lastSql.equals(sql)) {
				System.out.println("CommonDao: 同上same with last sql ↑ ");
			} else {
				System.out.println("CommonDao: ".concat(sql));
				lastSql=sql;
			}	
			//日志输出
		}
	}
	
	
	/**
	 * 通过注册的数据源，获取数据库类型
	 * @param dataSourceName
	 * @return
	 */
	public DataBaseType getDataBaseType(String dataSourceName) {
		DruidDataSource dds = (DruidDataSource)applicationContext.getBean(dataSourceName);
		//TODO 其它种类连接池，判断获取数据库驱动名
		String driverClassName = dds.getDriverClassName();
		return DataBaseType.getDataBaseType(driverClassName);
	}
	
	
	/**
	 * 设置默认数据库用户或者实例，schema
	 * 从数据源获取信息
	 * @param jdbcUsername
	 * @param jdbcUrl
	 */
	public String getDataBaseSchema(String dataSourceName){
		DruidDataSource dds = (DruidDataSource)applicationContext.getBean(dataSourceName);
		String jdbcUsername = dds.getUsername();
		String jdbcUrl = dds.getUrl();
		String schema = "";
		if (getDataBaseType(dataSourceName).equals(DataBaseType.ORACLE)) {
			schema = jdbcUsername;
		} else if (getDataBaseType(dataSourceName).equals(DataBaseType.MYSQL)) {
			//TODO MYSQL设置默认数据库用户或者实例，schema
			schema = jdbcUrl; 
		} else if (getDataBaseType(dataSourceName).equals(DataBaseType.POSTGREPSQL)) {
			schema = jdbcUsername;
		}
		return schema.toUpperCase();
	}
	
	//schema 如果在 tableName没有，添加当前默认的;
	
	/**
	 * 缓存默认数据源的 tableType
	 * @param tableType
	 * @param dataSourceName
	 */
	public void putTableTypeCache(TableType tableType, String dataSourceName) {
		if (dataSourceName.equals(DaoUtil.defaultDataSourceName)) {//缓存默认数据源的 tableType
			TableType.putTableType(tableType);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
