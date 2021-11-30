package com.wondersgroup.commondao.dao.custom;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加XGBZ修改标志   筛选的查询条件
 */
public class QueryConditionXgbz extends QueryCondition {
	
	public String fieldName;
	
	public Object fieldValue;
	
	public String paramName;
	
	/**
	 * 默认查询修改标志为0的数据
	 */
	public QueryConditionXgbz () {
		super();
		this.fieldName = "xgbz";
		this.fieldValue = 0;//默认查询修改标志为0的数据
		this.paramName = "xgbz_custom";
	}
	
	/**
	 * 初始化修改传入的查询xgbz修改标志位自定义值。 0正常 1撤销
	 * @param fieldValue 修改标志的值
	 */
	public QueryConditionXgbz (int fieldValue) {
		super();
		this.fieldName = "xgbz";
		this.fieldValue = fieldValue;
		this.paramName = "xgbz_custom";
	}
	

	@Override
	public String getSqlString(String where) {
		StringBuffer sqlBuffer = new StringBuffer();
		if (StringUtils.isBlank(where) || ( !"where".equals(where) && !"on".equals(where) ) ) {
			where = "and";//筛选条件默认and开头
		}
		sqlBuffer.append(" ").append(where).append(" ");
		sqlBuffer.append(this.fieldName);
		sqlBuffer.append(" = :").append(this.paramName).append(" ");
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

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	/*不允许修改字段名
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	*/

}
