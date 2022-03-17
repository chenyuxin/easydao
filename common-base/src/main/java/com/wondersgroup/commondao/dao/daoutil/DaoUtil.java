package com.wondersgroup.commondao.dao.daoutil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.serial.SerialClob;

import com.wondersgroup.commonutil.constant.StringPool;

/**
 * Dao工具
 */
public class DaoUtil {
	
	public static final String delDropTable_SUCCESS_MESSAGE = "drop table complates 移除表已完成";
	public static final String delDropTable_FAILED_MESSAGE = "drop table failed 移除表已失败";
	public static final String useTable_SUCCESS_MESSAGE = "use table complates 表操作已完成";
	public static final String useTable_FAILED_MESSAGE = "use table failed 表操作已失败";
	public static final String saveObj_SUCCESS_MESSAGE = "save complates 数据保存完成";
	public static final String saveObj_FAILED_MESSAGE = "save error 数据保存出错";
	public static final String updateObj_SUCCESS_MESSAGE = "update complates 数据更新完成";
	public static final String updateObj_FAILED_MESSAGE = "update error 数据更新出错";
	public static final String mergeObj_SUCCESS_MESSAGE = "merge complates 数据新增或更新完成";
	public static final String mergeObj_FAILED_MESSAGE = "merge error 数据新增或更新出错";
	public static final String delObj_SUCCESS_MESSAGE = "delete complates 删除完成";
	public static final String delObj_FAILED_MESSAGE = "delete error 删除出错";
	public static final String truncateTable_SUCCESS_MESSAGE = "truncate table complates 清空表完成";
	public static final String truncateTable_FAILED_MESSAGE = "truncate table error 清空表出错";
	
	/**
	 * 获取默认数据源name
	 */
	public static final String defaultDataSourceName = "dataSource";
	
	public static final String OracleJdbcDriverClassName = " oracle.jdbc.driver.OracleDriver oracle.jdbc.OracleDriver ";
	public static final String MysqlJdbcDriverClassName = " com.mysql.jdbc.Driver ";
	public static final String PostgreJdbcDriverClassName = " org.postgresql.Driver ";
	
	
	
	 /**
	  * 下划线转驼峰法
	  * @param line 源字符串
	  * @param smallCamel 大小驼峰,是否为小驼峰
	  * @return 转换后的字符串
	  */
	public static String underlineToCamel(String line,boolean smallCamel){
		if(line==null||StringPool.BLANK.equals(line)){
			return StringPool.BLANK;
		}
		StringBuffer sb=new StringBuffer();
		Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher=pattern.matcher(line);
		while(matcher.find()){
			String word=matcher.group();
			sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
			int index=word.lastIndexOf('_');
			if(index>0){
				sb.append(word.substring(1, index).toLowerCase());
			}else{
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}
	
	 /**
	  * 驼峰法转下划线
	  * @param line 源字符串
	  * @return 转换后的字符串
	  */
	public static String camelToUnderline(String line){
		if(line==null||StringPool.BLANK.equals(line)){
			return StringPool.BLANK;
		}
		line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
		StringBuffer sb=new StringBuffer();
		Pattern pattern=Pattern.compile("[A-Z]([a-z_\\d]+)?");//"([A-Za-z\\d]+)(_)?"
		Matcher matcher=pattern.matcher(line);
		while(matcher.find()){
			String word=matcher.group();
			sb.append(word.toUpperCase());
			sb.append(matcher.end()==line.length()?StringPool.BLANK:"_");
		}
		return sb.toString();
	}
	
	/**
	 * 替换对象数据的空值 为默认值
	 * @param <T>
	 * @param obj javaBean对象
	 * @param isNotNullValues 不同数据类型具有的不同替换值
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	public static <T> void setNullObject(T obj,Object... isNotNullValues) throws Exception {
		if (null == isNotNullValues || Array.getLength(isNotNullValues) == 0) {
			return;
		}
		Map<Class<?>, Object> notNullMap = new HashMap<Class<?>, Object>();
		for (Object isNotNullValue : isNotNullValues) {
			notNullMap.put(isNotNullValue.getClass(), isNotNullValue);
		}
		
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);//不检查 直接取值
			Type genericType = field.getGenericType();
			if ( genericType.equals(String.class) ) {
				String value = (String) field.get(obj);
				if (null == value || StringPool.BLANK.equals(value)) {//将空值替换
					field.set(obj, notNullMap.get(genericType));
	  			}
			} else if (null == field.get(obj)) {//如果是时间类型为空则返回一个默认日期，避免返回空字符串格式报错
				field.set(obj,notNullMap.get(genericType));
			}
		}
	}
	
	/**
     * Clob 转 String
     */
    public static String getString(Clob c) {
        try {
            return c.getSubString(1, (int) c.length());
        } catch (Exception e) {
            return null;
        }
    }
 
    /**
     * String 转 Clob
     */
    public static Clob getClob(String s) {
        try {
            return new SerialClob(s.toCharArray());
        } catch (Exception e) {
            return null;
        }
    }
	
	
}