package com.wondersgroup.commondao.dao.custom;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加自定义为小于筛选的查询条件
 */
public class QueryConditionCommonBetween extends QueryCondition {
	
	public String fieldName;
	
	public Object fieldValue;
	
	public String paramName;
	
	/**
	 * 判断
	 * 0 :  valueMin < fieldName < valueMax
	 * 1 :  valueMin <= fieldName < valueMax
	 * 2 :  valueMin < fieldName <= valueMax
	 * 3 :  valueMin <= fieldName <= valueMax
	 */
	private int equalFlag = 0;
	
	/**
	 * 添加之间为筛选的查询条件初始化(0 :  valueMin < fieldName < valueMax, 1 :  valueMin <= fieldName < valueMax, 2 :  valueMin < fieldName <= valueMax, 3 :  valueMin <= fieldName <= valueMax)
	 * @param fieldName 数据库字段名
	 * @param valueMin 参数值 小值
	 * @param valueMax 参数值 大值
	 * @param paramName 参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 * @param equalFlag 判断是否加上等于和形式。(0 :  valueMin < fieldName < valueMax, 1 :  valueMin <= fieldName < valueMax, 2 :  valueMin < fieldName <= valueMax, 3 :  valueMin <= fieldName <= valueMax)
	 */
	public QueryConditionCommonBetween (String fieldName,Object valueMin,Object valueMax,String paramName,int equalFlag) {
		super();
		Object[] o = {valueMin,valueMax};
		this.fieldName = fieldName;
		this.fieldValue = o;
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
		if (0==equalFlag || 2==equalFlag) {
			sqlBuffer.append(" > ");
		} else {
			sqlBuffer.append(" >= ");
		}
		sqlBuffer.append(":").append(this.paramName).append("Min ");
		sqlBuffer.append(" and ").append(this.fieldName);
		if (0==equalFlag || 1==equalFlag) {
			sqlBuffer.append(" < ");
		} else {
			sqlBuffer.append(" <= ");
		}
		sqlBuffer.append(":").append(this.paramName).append("Max ");
		return sqlBuffer.toString();
	}

	@Override
	public Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(this.paramName.concat("Min"), ((Object[])this.fieldValue)[0]);
		paramMap.put(this.paramName.concat("Max"), ((Object[])this.fieldValue)[1]);
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
