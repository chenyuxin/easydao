package com.wondersgroup.commondao.dao.custom;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加自定义为模糊查询筛选的查询条件
 */
public class QueryConditionCommonLike extends QueryCondition {
	
	public String fieldName;
	
	public Object fieldValue;
	
	/**
	 * 添加自定义为等于筛选的查询条件初始化
	 * @param fieldName 数据库字段名
	 * @param fieldValue 参数值
	 * @param paramName 参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 */
	public QueryConditionCommonLike (String fieldName,Object fieldValue) {
		super();
		if (fieldValue.toString().contains("%") ||
			fieldValue.toString().contains("_") ||
			fieldValue.toString().contains("'") ||
			fieldValue.toString().length()>100  ||
			fieldValue.toString().length()==0
		){
			fieldValue = "-";//有问题的like防止注入
		}
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	/**
	sBuffer.append(" where p0.name like :name ");
	paramMapDao.put("name", "%".concat(paramMapLike.get("name").toString()).concat("%"));
	 */

	@Override
	public String getSqlString(String where) {
		StringBuffer sqlBuffer = new StringBuffer();
		if (StringUtils.isBlank(where) || ( !"where".equals(where) && !"on".equals(where) ) ) {
			where = "and";//筛选条件默认and开头
		}
		sqlBuffer.append(" ").append(where).append(" ");
		sqlBuffer.append(this.fieldName);
		sqlBuffer.append(" like '%");
		sqlBuffer.append(this.fieldValue).append("%' ");
		return sqlBuffer.toString();
	}

	@Override
	public Map<String, Object> getParamMap() {
		return new HashMap<String, Object>();
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}


}
