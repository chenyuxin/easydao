package com.wondersgroup.commondao.dao.custom;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加自定义为小于筛选的查询条件
 */
public class QueryConditionCommonLess extends QueryCondition {
	
	public String fieldName;
	
	public Object fieldValue;
	
	public String paramName;
	
	/**
	 * 判断是否为小于等于,默认只有 '<'
	 */
	private boolean equalFlag = false;
	
	/**
	 * 添加自定义为等于筛选的查询条件初始化
	 * @param fieldName 数据库字段名
	 * @param fieldValue 参数值
	 * @param paramName 参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 * @param equalFlag 判断是否为小于等于,false为'<',true为'<='
	 */
	public QueryConditionCommonLess (String fieldName,Object fieldValue,String paramName,boolean equalFlag) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.paramName = paramName;
		this.equalFlag = equalFlag;
	}
	

	@Override
	public String getSqlString(String where) {
		StringBuffer sqlBuffer = new StringBuffer();
		if (StringUtils.isBlank(where) || ( !"where".equals(where) && !"on".equals(where) ) ) {
			where = "and";//筛选条件默认and开头
		}
		sqlBuffer.append(" ").append(where).append(" ");
		sqlBuffer.append(this.fieldName);
		if (equalFlag) {
			sqlBuffer.append(" <= ");
		} else {
			sqlBuffer.append(" < ");
		}
		sqlBuffer.append(":").append(this.paramName).append(" ");
		return sqlBuffer.toString();
	}

	@Override
	public Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(this.paramName, this.fieldValue);
		return paramMap;
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
