package com.wondersgroup.core.utils;

import org.apache.commons.lang3.StringUtils;

import com.wondersgroup.commonutil.constant.StringPool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
public class DateUtil extends org.apache.commons.lang3.time.DateUtils{
	
	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyyMMdd", "yyyyMMddHHmmss" };
	
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 将日期格式化输出为yyyy/MM/dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return df.format(date);
	}

	/**
	 * 输出当前时刻格式为 yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String customFormat() {	        
		Date date = new Date();
		return df2.format(date);
	}
	
	/**
         * 输出当前时刻格式为yyyy/MM/dd HH:mm:ss
         * 
         * @return
         */
        public static String format() {
                Date date = new Date();
                return df.format(date);
        }

	/**
	 * 将时间字符串格式化yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datestr
	 * @return
	 */
	public static String format(String datestr) {
		Date date;
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			date = new Date();
		}
		return df.format(date);
	}

	/**
	 * 根据出生日期获得年龄
	 * 
	 * @param birthday
	 * @return
	 */
	public static int getAge(Long birthday) {
		int age = 0;
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar now = (GregorianCalendar) gc.clone();
		gc.setTimeInMillis(birthday.longValue());
		age = now.get(Calendar.YEAR) - gc.get(Calendar.YEAR);
		if (age < 0) {
			age = 0;
		}
		return age;
	}

	/**
	 * 根据出生日期获得年龄
	 * 
	 * @param birthday
	 * @return
	 */
	public static int getAge(String birthday) {
		int age = 0;
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar now = (GregorianCalendar) gc.clone();
		long longtime = 0;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date time = formatter.parse(birthday);
			longtime = getMillis(time);
		} catch (Exception e) {

		}
		gc.setTimeInMillis(longtime);
		age = now.get(Calendar.YEAR) - gc.get(Calendar.YEAR);
		if (age < 0) {
			age = 0;
		}
		return age;
	}

	/**
	 * 返回毫秒
	 * 
	 * @param date
	 *         日期
	 * @return 返回毫秒
	 */
	public static long getMillis(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 日期相加
	 * 
	 * @param date
	 *         日期
	 * @param day
	 *         天数
	 * @return 返回相加后的日期
	 */
	public static java.util.Date addDate(java.util.Date date, int day) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 在日期中取出指定部分的字符串值
	 * 
	 * @param date
	 * @param part
	 *         Calendar.MONTH;
	 * @return
	 */
	public static String GetDatePart(String date, int part) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date time = formatter.parse(date);
			formatter.format(time);
			if (Calendar.MONTH == part)
				return String.valueOf(formatter.getCalendar().get(part) + 1);
			else
				return String.valueOf(formatter.getCalendar().get(part));
		} catch (Exception ex) {
			return StringPool.BLANK;
		}
	}

	/**
	 * 输出fomatStyle时间格式字符串
	 * 
	 * @param datestr
	 * @param fomatStyle
	 * @return
	 */
	public static String format(String datestr, String fomatStyle) {
		return format(datestr, "yyyy-MM-dd HH:mm:ss", fomatStyle);
	}

	/**
	 * 将时间字符串格式化yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datestr
	 * @return
	 */
	private static String format(String datestr, String dateStyle,
			String fomatStyle) {

		DateFormat df1 = new SimpleDateFormat(dateStyle);
		DateFormat df2 = new SimpleDateFormat(fomatStyle);
		Date date = null;
		try {
			date = df1.parse(datestr);
		} catch (ParseException e) {
			date = new Date();
		}
		return df2.format(date);
	}

	/**
	 * 将时间字符串格式化yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @param fomatStyle
	 * @return
	 */
	public static String format(Date date, String fomatStyle) {
		DateFormat df = new SimpleDateFormat(fomatStyle);
		return df.format(date);
	}

	/**
	 * 获取当前日期 格式 yyyy-MM-dd
	 */
	public static String getCurrentDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}
	
	public static Date getDate(String time) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.parse(time);
	}
	
	/**
	 * 取上月第一天日期
	 */
	public static String getPreviousMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DATE, 1);
		
		return format(calendar.getTime(), "yyyy-MM-dd");
	}
	

	
	/**
	 * 取昨天日期
	 */
	  public static String getYesterday()
	  {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.DATE, -1);
	    return format(calendar.getTime(),"yyyy-MM-dd");
	  }

	/**
	 * 取本月月初日期
	 */
	public static String getCurrentMonthFirstDay() {
		String beginMonth = StringPool.BLANK;
		Calendar calendar = Calendar.getInstance();
		String startMonth = StringPool.BLANK + (calendar.get(Calendar.MONTH));
		if ("12".equals(startMonth)) {
			calendar.add(Calendar.YEAR, 1);
			beginMonth = calendar.get(java.util.Calendar.YEAR) + "-" + "01";
		} else if ("11".equals(startMonth)) {
			beginMonth = calendar.get(java.util.Calendar.YEAR) + "-" + "12";
		} else {
			calendar.add(Calendar.MONTH, 1);
			String startMonth1 = StringPool.BLANK + (calendar.get(java.util.Calendar.MONTH));
			beginMonth = calendar.get(java.util.Calendar.YEAR) + "-" + startMonth1;
			if (startMonth1.length() == 1) {
				startMonth1 = "0" + startMonth1;
				beginMonth = calendar.get(Calendar.YEAR) + "-" + startMonth1;
			}
		}
		return beginMonth + "-01";
	}
	
	public static String getLastYearDay() {
		return "2010-01-01";
	}
	
	public static String getCurrentYearFristMonthFristDay() {
		Calendar calendar = Calendar.getInstance();
		int year=(int)calendar.get(Calendar.YEAR);
		return year + "-01-01";
	}
	
//	public static void main(String[] args) {
//		System.out.println(format(convertToDate("201708", "yyyyMM"), "yyyy-MM"));
//		System.out.println(convertToDate("2017/08", "yyyy/MM"));
//		System.out.println(parseDate("2017/08", "yyyy/MM"));
//	}
	
	public static Date getDateTime(String s) {
		if ((s == null) || (s.length() == 0)) {
			return null;
		}
		return parseDate(s, "yyyy-MM-dd");
	}
	//转日期
	protected static Date parseDate(String s, String s1) {
		if ((s == null) || (s1 == null)) {
			return null;
		}
		SimpleDateFormat simpledateformat = new SimpleDateFormat(s1);
		ParsePosition parseposition = new ParsePosition(0);
		String s2 = s.replace('/', '-');
		return simpledateformat.parse(s2, parseposition);
	}
	
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static boolean isValidDate(String str,String pattern) {
		boolean convertSuccess = true;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 字符串按照指定格式转日期
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date convertToDate(String dateStr, String format){
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
