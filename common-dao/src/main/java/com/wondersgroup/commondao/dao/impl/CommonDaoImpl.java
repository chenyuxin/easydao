package com.wondersgroup.commondao.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wondersgroup.commondao.dao.daofactory.CommonSql;
import com.wondersgroup.commondao.dao.daofactory.DaoFactory;
import com.wondersgroup.commondao.dao.daoutil.DaoUtil;
import com.wondersgroup.commondao.dao.daoutil.SqlReader;
import com.wondersgroup.commondao.dao.daoutil.anotation.Table;
import com.wondersgroup.commondao.dao.daoutil.anotation.TableUtil;
import com.wondersgroup.commondao.dao.daoutil.toolentity.DaoOptions;
import com.wondersgroup.commondao.dao.daoutil.toolentity.JoinOptions;
import com.wondersgroup.commondao.dao.daoutil.toolentity.PageBean;
import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.commonutil.type.database.TableType;

@Repository
public class CommonDaoImpl implements CommonDao {
	
	@Autowired private DaoFactory daoFactory;
	
	@Override
	public boolean isTable(TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO,tableType);
		return isTable(daoOptions.getTableType(),daoOptions.getDataSourceName());
	}
	
	private boolean isTable(TableType tableType,String dataSourceName) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		String sql = "select count(1) from ".concat(tableType.getTableName()).concat(" where 1=0 ");
		daoFactory.printSql(sql);
		try{
			currentJdbcTemplate.queryForObject(sql, new HashMap<String,Object>(), Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			TableType.delTableType(tableType);
			return false;//表不存在
		}
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return true;
	}
	
	@Override
	public String delDropTable(TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO,tableType);
		return delDropTable(daoOptions.getTableType(),daoOptions.getDataSourceName());
	}

	private String delDropTable(TableType tableType,String dataSourceName) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		String tableName = tableType.getTableName();
		String sql = "drop table ".concat(tableName);
		daoFactory.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, new HashMap<String,Object>());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
			return tableName.concat(": ").concat(DaoUtil.delDropTable_FAILED_MESSAGE);
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableName);
		TableType.delTableType(tableType);//表被drop后将不再缓存此表类型
		return tableName.concat(": ").concat(DaoUtil.delDropTable_SUCCESS_MESSAGE);
	}
	
	@Override
	public String useTable(String ctrlSql, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		return useTable(daoOptions.getParamMap(),daoOptions.getDataSourceName(),ctrlSql,daoOptions.isThrowException());
	}
	
	private String useTable(Map<String, Object> paramMap, String dataSourceName, String ctrlSql,boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		daoFactory.printSql(ctrlSql);
		try {
			currentJdbcTemplate.update(ctrlSql, paramMap);
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				System.out.println(e.getMessage());
				//e.printStackTrace();
				return DaoUtil.useTable_FAILED_MESSAGE;
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,SqlReader.getTablesFromSql(ctrlSql));
		return DaoUtil.useTable_SUCCESS_MESSAGE;
	}
	
	@Override
	public String delCache(TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		//当调用了 useTable 时  和使用了查询缓存时，该表名的所有的 内存缓存将移除。
		return useTable(daoOptions.getParamMap(),daoOptions.getDataSourceName(),"select '移除该表内存缓存' from ".concat(tableType.getTableName()).concat(" where 1=0 "),false);
	}
	
	@Override
	public String saveObj(Object object, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		if (object instanceof List) {
			return saveObjList((List<?>) object, daoOptions.getTableType(), daoOptions.getDataSourceName(),daoOptions.isThrowException() );
		} else {
			return saveObjSingle(object, daoOptions.getTableType(),  daoOptions.getDataSourceName(),daoOptions.isThrowException());
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> String saveObjList(List<T> list, TableType tableType, String dataSourceName,boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		Class<T> clazz = (Class<T>) list.get(0).getClass();
		//System.out.println(clazz.getSimpleName());
		String sql;
		Map<String,Object>[] paramMaps;
		if ( list.get(0) instanceof Map) {//使用map的封装保存数据
			sql = CommonSql.insertSql4Map((Map<String, Object>)list.get(0), tableType);
			paramMaps = list.toArray(new Map[list.size()]);
		} else {//使用类的list保存数据
			String tableName = clazz.getAnnotation(Table.class).name();
			tableType = TableType.getTableType(tableName);
			sql = CommonSql.insertSql(clazz, tableType);
			paramMaps = new HashMap[list.size()];
			for (int i=0;i<list.size();i++) {
				//paramMap = DaoUtil.setNull(list.get(i);
				paramMaps[i] = TableUtil.object2Map(list.get(i));
			}
		}
		daoFactory.printSql("开始保存save_sql".concat(clazz.getName()).concat(":").concat(sql));
		try {
			currentJdbcTemplate.batchUpdate(sql, paramMaps);
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				//e.printStackTrace();
				System.out.println(e.getMessage());
				return DaoUtil.saveObj_FAILED_MESSAGE.concat(": ").concat(e.getMessage());
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return DaoUtil.saveObj_SUCCESS_MESSAGE.concat("[").concat(String.valueOf(list.size())).concat("]");//添加作业数据量的返回
	}
	
	@SuppressWarnings("unchecked")
	private String saveObjSingle(Object object, TableType tableType, String dataSourceName,boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		@SuppressWarnings("rawtypes")
		Class clazz = object.getClass();
		Map<String,Object> paramMap;
		String sql;
		if ( object instanceof Map) {//使用map的封装保存数据//使用map的封装保存数据
			sql = CommonSql.insertSql4Map((Map<String, Object>) object, tableType);
			paramMap = (Map<String, Object>) object;
		} else {//使用类的保存数据方式
			String tableName = ((Table) clazz.getAnnotation(Table.class)).name();
			tableType = TableType.getTableType(tableName);
			sql = CommonSql.insertSql(clazz, tableType);
			//paramMap = DaoUtil.setNull(Object);
			paramMap = TableUtil.object2Map(object);
		}
		daoFactory.printSql("开始保存save_sql".concat(clazz.getSimpleName()).concat(":").concat(sql));
		try {
			currentJdbcTemplate.update(sql, paramMap);
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				//e.printStackTrace();
				System.out.println(e.getMessage());
				return DaoUtil.saveObj_FAILED_MESSAGE.concat(": ").concat(e.getMessage());
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return DaoUtil.saveObj_SUCCESS_MESSAGE;
	}
	
	@Override
	public String updateObj(Object object, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		if (object instanceof List) {
			List<?> list = (List<?>)object;
			String[] fieldNameByIds = TableUtil.getIdsbyObj(list.get(0).getClass());
			return updateList(list, fieldNameByIds, null, daoOptions.getDataSourceName(),daoOptions.isThrowException() );
		} else {
			String[] fieldNameByIds = TableUtil.getIdsbyObj(object.getClass());
			return updateSingle(object, fieldNameByIds, null, daoOptions.getDataSourceName(),daoOptions.isThrowException());
		}
	}
	
	@Override
	public String updateMap(Object object, String[] fieldNameByIds,TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO,tableType);
		if (object instanceof List) {
			return updateList((List<?>) object, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),daoOptions.isThrowException() );
		} else {
			return updateSingle(object, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),daoOptions.isThrowException());
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> String updateList(List<T> list, String[] fieldNameByIds, TableType tableType, String dataSourceName,boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		Class<T> clazz = (Class<T>) list.get(0).getClass();
		String sql;
		Map<String,Object>[] paramMaps;
		
		if ( list.get(0) instanceof Map) {//使用map的封装保存数据
			sql = CommonSql.updateSql((Map<String, Object>)list.get(0), tableType.getTableName(), fieldNameByIds);
			paramMaps = list.toArray(new Map[list.size()]);
		} else {
			Map<String,Object> paramMap = TableUtil.object2Map(list.get(0));
			String tableName = clazz.getAnnotation(Table.class).name();
			tableType = TableType.getTableType(tableName);//将表名设置为本对象表名
			sql = CommonSql.updateSql(paramMap, tableName, fieldNameByIds);
			paramMaps = new HashMap[list.size()];
			for (int i=0;i<list.size();i++) {
				//paramMap = DaoUtil.setNull(list.get(i);
				paramMaps[i] = TableUtil.object2Map(list.get(i));
			}
		}
		daoFactory.printSql(sql);
		try {
			currentJdbcTemplate.batchUpdate(sql, paramMaps);
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				System.out.println(e.getMessage());
				//e.printStackTrace();
				return DaoUtil.updateObj_FAILED_MESSAGE.concat(": ").concat(e.getMessage());
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return DaoUtil.updateObj_SUCCESS_MESSAGE.concat("[").concat(String.valueOf(list.size())).concat("]");//更新作业数据量的返回
	}

	@SuppressWarnings("unchecked")
	private String updateSingle(Object object, String[] fieldNameByIds, TableType tableType, String dataSourceName, boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		Map<String,Object> paramMap;
		String sql;
		if ( object instanceof Map) {
			sql = CommonSql.updateSql((Map<String, Object>) object, tableType.getTableName(), fieldNameByIds);
			paramMap = (Map<String, Object>) object;
		} else {
			paramMap = TableUtil.object2Map(object);
			Table table = object.getClass().getAnnotation(Table.class);
			tableType = TableType.getTableType(table.name());
			sql = CommonSql.updateSql(paramMap,table.name(),fieldNameByIds);
		}
		daoFactory.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, paramMap);
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				System.out.println(e.getMessage());
				//e.printStackTrace();
				return DaoUtil.updateObj_FAILED_MESSAGE;
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return DaoUtil.updateObj_SUCCESS_MESSAGE;
	}
	
	@Override
	public String saveOrUpdateObj(Object object, Object... daoOptionsO){
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		if (object instanceof List) {
			List<?> list = (List<?>)object;
			String[] fieldNameByIds = TableUtil.getIdsbyObj(list.get(0).getClass());
			return saveOrUpdateList(list, fieldNameByIds, null, daoOptions.getDataSourceName(),daoOptions.isThrowException());
		} else {
			String[] fieldNameByIds = TableUtil.getIdsbyObj(object.getClass());
			return saveOrUpdateSingle(object, fieldNameByIds, null, daoOptions.getDataSourceName(),daoOptions.isThrowException());
		}
	}
	
	@Override
	public String saveOrUpdateMap(Object object, String[] fieldNameByIds,TableType tableType, Object... daoOptionsO){
		DaoOptions daoOptions = new DaoOptions(daoOptionsO,tableType);
		if (object instanceof List) {
			return saveOrUpdateList((List<?>) object, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(), daoOptions.isThrowException() );
		} else {
			return saveOrUpdateSingle(object, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),daoOptions.isThrowException() );
		}
	}
	
	@SuppressWarnings("unchecked")
	private String saveOrUpdateSingle(Object object, String[] fieldNameByIds, TableType tableType, String dataSourceName,boolean isThrowException){
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		DataBaseType dataBaseType = daoFactory.getDataBaseType(dataSourceName);//获取数据库类型
		Map<String,Object> paramMap;
		String sql;
		if ( object instanceof Map) {
			sql = CommonSql.saveOrUpdateSql((Map<String, Object>) object, tableType.getTableName(), dataBaseType, fieldNameByIds);
			paramMap = (Map<String, Object>) object;
		} else {
			paramMap = TableUtil.object2Map(object);
			Table table = object.getClass().getAnnotation(Table.class);
			tableType = TableType.getTableType(table.name());
			sql = CommonSql.saveOrUpdateSql(paramMap,tableType.getTableName(),dataBaseType,fieldNameByIds);
		}
		daoFactory.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, paramMap);
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				System.out.println(e.getMessage());
				//e.printStackTrace();
				return DaoUtil.mergeObj_FAILED_MESSAGE;
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return DaoUtil.mergeObj_SUCCESS_MESSAGE;
	}
	
	@SuppressWarnings("unchecked")
	private <T> String saveOrUpdateList(List<T> list, String[] fieldNameByIds, TableType tableType, String dataSourceName,boolean isThrowException){
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		DataBaseType dataBaseType = daoFactory.getDataBaseType(dataSourceName);//获取数据库类型
		
		Class<T> clazz = (Class<T>) list.get(0).getClass();
		String sql;
		Map<String,Object>[] paramMaps;
		
		if ( list.get(0) instanceof Map) {//使用map的封装保存数据
			sql = CommonSql.saveOrUpdateSql((Map<String, Object>)list.get(0), tableType.getTableName(), dataBaseType, fieldNameByIds);
			paramMaps = list.toArray(new Map[list.size()]);
		} else {
			Map<String,Object> paramMap = TableUtil.object2Map(list.get(0));
			String tableName = clazz.getAnnotation(Table.class).name();
			tableType = TableType.getTableType(tableName);//将表名设置为本对象表名
			sql = CommonSql.saveOrUpdateSql(paramMap, tableType.getTableName(), dataBaseType, fieldNameByIds);
			paramMaps = new HashMap[list.size()];
			for (int i=0;i<list.size();i++) {
				//paramMap = DaoUtil.setNull(list.get(i);
				paramMaps[i] = TableUtil.object2Map(list.get(i));
			}
		}
		daoFactory.printSql(sql);
		try {
			currentJdbcTemplate.batchUpdate(sql, paramMaps);
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				System.out.println(e.getMessage());
				//e.printStackTrace();
				return DaoUtil.mergeObj_FAILED_MESSAGE.concat(": ").concat(e.getMessage());
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return DaoUtil.mergeObj_SUCCESS_MESSAGE.concat("[").concat(String.valueOf(list.size())).concat("]");//更新作业数据量的返回
	}
	
	@Override
	public String deleteObj(Object object, Object... daoOptionsO){
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		return deleteObjSingle(object, daoOptions.getDataSourceName(),daoOptions.isThrowException());
	}
	
	private String deleteObjSingle(Object object, String dataSourceName,boolean isThrowException){
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		Map<String,Object> paramMap = TableUtil.object2Map(object);
		String[] fieldNameByIds = TableUtil.getIdsbyObj(object.getClass());
		Table table = object.getClass().getAnnotation(Table.class);
		TableType tableType = TableType.getTableType(table.name());
		String sql = CommonSql.deleteSql(tableType.getTableName(),fieldNameByIds);
		daoFactory.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, paramMap);
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				System.out.println(e.getMessage());
				//e.printStackTrace();
				return DaoUtil.delObj_FAILED_MESSAGE;
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return DaoUtil.delObj_SUCCESS_MESSAGE;
	}
	
	@Override
	public String delTruncateTable(TableType tableType, Object... daoOptionsO){
		DaoOptions daoOptions = new DaoOptions(daoOptionsO,tableType);
		return delTruncateTable(daoOptions.getTableType(),daoOptions.getDataSourceName(),daoOptions.isThrowException());
	}

	private String delTruncateTable(TableType tableType, String dataSourceName,boolean isThrowException){
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
		String tableName = tableType.getTableName();
		String sql = "truncate table ".concat(tableName);
		daoFactory.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, new HashMap<String,Object>());
		} catch (Exception e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				System.out.println(e.getMessage());
				//e.printStackTrace();
				return DaoUtil.truncateTable_FAILED_MESSAGE;
			}
		}
		daoFactory.checkQureyDataCacheMap(dataSourceName,tableName);
		daoFactory.putTableTypeCache(tableType,dataSourceName);
		return DaoUtil.truncateTable_SUCCESS_MESSAGE;
	}

	
	
	
	
	
	
	
	
	
	
	private List<Map<String, Object>> selectObjMap(Map<String, Object> paramMap, String dataSourceName, String sql,boolean isThrowException) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> objList = (List<Map<String, Object>>) daoFactory.getQureyDataCache(sql, paramMap, dataSourceName);
		if (null == objList) {
			NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(dataSourceName);
			daoFactory.printSql(sql);
			try {
				objList = currentJdbcTemplate.queryForList(sql, paramMap);
			} catch (Exception e) {
				if (isThrowException) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}
			daoFactory.putQureyDataCache(objList, sql, paramMap, dataSourceName);
		}
		return objList;
	}
	
	@Override
	public List<Map<String, Object>> selectObjMap(String sql, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		return selectObjMap(daoOptions.getParamMap(), daoOptions.getDataSourceName(), sql,daoOptions.isThrowException());
	}
	
	@Override
	public List<Map<String, Object>> selectObjMap(List<String> attributeNames, TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO,tableType);
		Map<String, Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql4Map(attributeNames, daoOptions.getTableType().getTableName(), paramMapDao, daoOptions.getQueryConditions());
		List<Map<String, Object>> maps = selectObjMap(paramMapDao, daoOptions.getDataSourceName(), sql,daoOptions.isThrowException());
		if (maps != null) {
			daoFactory.putTableTypeCache(tableType,daoOptions.getDataSourceName());
		}
		return maps;
	}
	
	@Override
	public PageBean<Map<String, Object>> selectObjMap(int currentPage , int pageSize, List<String> attributeNames, TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO,tableType);
		daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());//用于加载数据库驱动名缓存
		DataBaseType dataBaseType = daoFactory.getDataBaseType(daoOptions.getDataSourceName());//获取数据库类型
		Map<String, Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql4Map(attributeNames,daoOptions.getTableType().getTableName(),currentPage ,pageSize,dataBaseType,paramMapDao,daoOptions.getQueryConditions());
		List<Map<String, Object>> objList = selectObjMap(sql, paramMapDao, daoOptions.getDataSourceName(),daoOptions.isThrowException());
		int total = getRecords(tableType,daoOptionsO);//返回记录数并缓存默认数据源的tableType
		return new PageBean<Map<String, Object>>(total, pageSize, currentPage , objList);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> PageBean<T> selectObj(int currentPage , int pageSize, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		TableType tableType = TableType.getTableType(clazz.getAnnotation(Table.class).name());
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());
		DataBaseType dataBaseType = daoFactory.getDataBaseType(daoOptions.getDataSourceName());//获取数据库类型
		Map<String, Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql4Obj(clazz,currentPage ,pageSize,dataBaseType,paramMapDao,daoOptions.getQueryConditions());
		
		PageBean<T> pageBean = (PageBean<T>) daoFactory.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == pageBean) {
			daoFactory.printSql(sql);
			List<T> objList = null;
			try {
				objList = currentJdbcTemplate.query(sql, paramMapDao,new BeanPropertyRowMapper<T>(clazz));
			} catch (Exception e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}
			int total = getRecords(tableType,daoOptionsO);//返回记录数并缓存默认数据源的tableType
			pageBean = new PageBean<T>(total, pageSize, currentPage , objList);
			daoFactory.putQureyDataCache(pageBean, sql, paramMapDao, daoOptions.getDataSourceName());
		}
		return pageBean;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> PageBean<T> selectObj(String sql, int currentPage, int pageSize, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());
		DataBaseType dataBaseType = daoFactory.getDataBaseType(daoOptions.getDataSourceName());//获取数据库类型
		Map<String, Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String pagingSql = CommonSql.pageing(sql,currentPage,pageSize,dataBaseType);
		PageBean<T> pageBean = (PageBean<T>) daoFactory.getQureyDataCache(pagingSql, paramMapDao, daoOptions.getDataSourceName());
		if (null == pageBean) {
			daoFactory.printSql(pagingSql);
			List<T> objList = null;
			try {
				if (null == clazz || Map.class == clazz) {
					objList = (List<T>) currentJdbcTemplate.queryForList(pagingSql, paramMapDao);
				} else {
					objList = currentJdbcTemplate.query(pagingSql, paramMapDao,new BeanPropertyRowMapper<T>(clazz));
				}
			} catch (Exception e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}
			
			String countSql = CommonSql.countSql(sql);
			//自定义通用查询条件 已经转换为了sql,并已经将参数变量放入paramMapDao
			int total = selectBaseObj(countSql, Integer.class, paramMapDao);
			pageBean = new PageBean<T>(total, pageSize, currentPage , objList);
			daoFactory.putQureyDataCache(pageBean, pagingSql, paramMapDao, daoOptions.getDataSourceName());
		}
		return pageBean;
	}
	
	@Override
	public <T> List<T> selectObj(String sql, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String,Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		
		@SuppressWarnings("unchecked")
		List<T> objList = (List<T>) daoFactory.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if ( null == objList ) {
			daoFactory.printSql(sql);
			try {
				objList = currentJdbcTemplate.query(sql, paramMapDao,new BeanPropertyRowMapper<T>(clazz));
			} catch (Exception e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}
			daoFactory.putQureyDataCache(objList, sql, paramMapDao, daoOptions.getDataSourceName());
		}
		return objList;
	}

	
	@Override
	public <T> List<T> selectObj(Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String,Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql(clazz, paramMapDao, daoOptions.getQueryConditions());
		
		@SuppressWarnings("unchecked")
		List<T> objList = (List<T>) daoFactory.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if ( null == objList ) {
			daoFactory.printSql(sql);
			try {
				objList = currentJdbcTemplate.query(sql, paramMapDao,new BeanPropertyRowMapper<T>(clazz));
			} catch (Exception e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}
			daoFactory.putQureyDataCache(objList, sql, paramMapDao, daoOptions.getDataSourceName());
		}
		return objList;
	}
	
	@Override
	public <T> T selectObjSingle(Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String,Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql(clazz, paramMapDao, daoOptions.getQueryConditions());
		
		@SuppressWarnings("unchecked")
		T object = (T) daoFactory.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == object) {
			daoFactory.printSql(sql);
			try {
				object = currentJdbcTemplate.queryForObject(sql, paramMapDao,new BeanPropertyRowMapper<T>(clazz));
			} catch (Exception e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}
			daoFactory.putQureyDataCache(object, sql, paramMapDao, daoOptions.getDataSourceName());
		}
		return object;
	}
	
	@Override
	public <T> T selectObjSingle(String sql, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		@SuppressWarnings("unchecked")
		T object = (T) daoFactory.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == object) {
			daoFactory.printSql(sql);
			try {
				object = currentJdbcTemplate.queryForObject(sql, paramMapDao, new BeanPropertyRowMapper<T>(clazz));
			} catch (Exception e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}
			daoFactory.putQureyDataCache(object, sql, paramMapDao, daoOptions.getDataSourceName());
		}
		return object;
	}
	
	@Override
	public <T> T selectBaseObj(String sql, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		@SuppressWarnings("unchecked")
		T object = (T) daoFactory.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == object) {
			daoFactory.printSql(sql);
			try {
				object = currentJdbcTemplate.queryForObject(sql, paramMapDao, clazz);
			} catch (Exception e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println("selectBaseObj自定义sql查询获取基本类: ".concat(e.getMessage()));
					//e.printStackTrace();
				}
			}
			daoFactory.putQureyDataCache(object, sql, paramMapDao, daoOptions.getDataSourceName());
		}
		return object;
	}
	
	@Override
	public <T> List<T> selectBaseObjs(String sql, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoFactory.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		@SuppressWarnings("unchecked")
		List<T> objectList = (List<T>) daoFactory.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == objectList) {
			daoFactory.printSql(sql);
			try {
				objectList = currentJdbcTemplate.queryForList(sql, paramMapDao, clazz);
			} catch (Exception e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					System.out.println("selectBaseObj自定义sql查询获取基本类: ".concat(e.getMessage()));
					//e.printStackTrace();
				}
			}
			daoFactory.putQureyDataCache(objectList, sql, paramMapDao, daoOptions.getDataSourceName());
		}
		return objectList;
	}
	
	@Override
	public int getRecords(TableType tableType,Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO,tableType);
		Map<String, Object> paramMapDao = new HashMap<String,Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.getRecords(daoOptions.getTableType().getTableName(),paramMapDao,daoOptions.getQueryConditions());
		//自定义通用查询条件 已经转换为了sql,并已经将参数变量放入paramMapDao
		Integer records = selectBaseObj(sql, Integer.class, paramMapDao);
		if (records != null) {//返回记录数并缓存默认数据源的tableType
			daoFactory.putTableTypeCache(tableType,daoOptions.getDataSourceName());
		}
		return records;
	}

	
	
	@Override
	public List<Map<String, Object>> selectJoin(JoinOptions joinOptions) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String sql = CommonSql.joinSql(joinOptions, paramMap);
		return selectObjMap(paramMap, joinOptions.getCurrentDaoOptions().getDataSourceName(), sql,joinOptions.getCurrentDaoOptions().isThrowException());
	}

	


	
	
}
