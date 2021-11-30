package com.wondersgroup.commondao.dao.custom;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加行政区划筛选查询条件
 */
public class QueryConditionXzqh extends QueryCondition {

	public String fieldName;
	
	public Object fieldValue;
	
	public String paramName = "xzqh_custom";
	
	public QueryConditionXzqh () {
		super();
	}
	
	/**
	 * 添加行政区划筛选查询条件
	 * @param fieldName 数据库字段名
	 * @param fieldValue 行政区划值
	 */
	public QueryConditionXzqh (String fieldName, Object fieldValue) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	@Override
	public String getSqlString(String where) {
		StringBuffer sqlBuffer = new StringBuffer();
		if (StringUtils.isBlank(where) || ( !"where".equals(where) && !"on".equals(where) ) ) {
			where = "and";//筛选条件默认and开头
		}
		sqlBuffer.append(" ").append(where).append(" ");
		if ("0000".equals(String.valueOf(this.fieldValue).substring(2, 6)) ) {//省，自治区，直辖市，特别行政区 属查询
			sqlBuffer.append(" substr(").append(this.fieldName).append(",0,2) ");
			sqlBuffer.append(" = substr( :").append(this.paramName).append(" ,0,2) ");
		} else if ("00".equals(String.valueOf(this.fieldValue).substring(4, 6)) ) {//地级市，州 属查询
			sqlBuffer.append(" substr(").append(this.fieldName).append(",0,4) ");
			sqlBuffer.append(" = substr( :").append(this.paramName).append(" ,0,4) ");
		} else { //区县，县级市 属查询
			sqlBuffer.append(" substr(").append(this.fieldName).append(",0,6) ");
			sqlBuffer.append(" = substr( :").append(this.paramName).append(" ,0,6) ");
		}
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

	/**
	 * 如果paramName变量名称与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	

}
