package com.wondersgroup.commondao.dao.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加自定义为in语句筛选的查询条件
 */
public class QueryConditionCommonIn extends QueryCondition {
	
	private String fieldName;
	
	private Object fieldValue;
	
	private String paramName;
	
	private boolean not = false;
	
	/**
	 * 添加自定义为in语句筛选的查询条件初始化
	 * @param fieldName 数据库字段名
	 * @param fieldValue 参数值,这里传入List表示多个值的筛选
	 * @param paramName 参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 * @param not 使用 true为not in语句;false为in语句
	 */
	public QueryConditionCommonIn (String fieldName,Object fieldValue,String paramName,boolean not) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.paramName = paramName;
		this.not = not;
	}
	

	@Override
	public String getSqlString(String where) {
		StringBuffer sqlBuffer = new StringBuffer();
		if (StringUtils.isBlank(where) || ( !"where".equals(where) && !"on".equals(where) ) ) {
			where = "and";//筛选条件默认and开头
		}
		sqlBuffer.append(" ").append(where).append(" ");
		sqlBuffer.append(this.fieldName);
		if ( this.fieldValue instanceof List ) {
			if (this.not) {//使用not in语句
				sqlBuffer.append(" not");
			}
			sqlBuffer.append(" in ( ");
			@SuppressWarnings("unchecked")
			List<Object> fieldValues = (List<Object>)this.fieldValue;
			for(int i=0;i<fieldValues.size();i++) {
				sqlBuffer.append(':').append(this.paramName).append(i);
				if (i+1 < fieldValues.size()) {
					sqlBuffer.append(',');
				}
			}
			sqlBuffer.append(" ) ");
		} else {
			if (this.not) {//使用not in语句
				sqlBuffer.append(" <>");
			} else {
				sqlBuffer.append(" =");
			}
			sqlBuffer.append(" :").append(this.paramName).append(" ");
		}
		return sqlBuffer.toString();
	}

	@Override
	public Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if ( this.fieldValue instanceof List ) {
			@SuppressWarnings("unchecked")
			List<Object> fieldValues = (List<Object>)this.fieldValue;
			for(int i=0;i<fieldValues.size();i++) {
				paramMap.put(this.paramName.concat(String.valueOf(i)), fieldValues.get(i));
			}
		} else {
			paramMap.put(this.paramName, this.fieldValue);
		}
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

	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}
	
	


}
