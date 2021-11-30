 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import com.alibaba.druid.DbType;
import com.wondersgroup.commondao.dao.daoutil.sqlreader.DaoSqlReader;


/**
 * 从写定的SQL中解析表名
 *
 * @author chenjunxu
 * @date 2018/12/27
 */
public class SqlReaderTest {
	
	private static final String FROM_LOWER = "from";
	private static final String FROM_UPPER = "FROM";
	private static final String[] SPLIT_KEY = {"select", "SELECT", " where ", " WHERE "};
	private static final String[] JOIN_KEY = {",", " join ", " JOIN "};
 
	public static void main(String[] args) {
			String sql = "select a, b, c from user u,org o where u.id = o.uid and u.id in (select id from user_org uo)";
//			String sql = "select * FROM     u   order    by  b";
//			String sql = "select count(a) from `user`  u  ,   org  o  where u.a = o.b group by d";
//		String sql = "select a, b, c from user u,org o where u.id = o.uid and u.id in (select id from user_org uo LEFT JOIN table_o to " +
//				"where uo.id not exist (select id from org_user  ))";
//			String sql = "select a, b, c from user u INNER   JOIN  org o on u.id = o.id where user.a = 'aaa'";
		sql = formatSQL(sql);
		List<String> list = new ArrayList<>();
		if (getSplitKey(sql) != null) {
			splitSQL(sql, list);
		}
		Set<String> tables = new HashSet<>();
		for (String s : list) {
			getTables(s, tables);
		}
		for (String table : tables) {
			System.out.println(table);
		}
	}
 
	private static void getTables(String str, Set<String> tables) {
		if (str.contains(FROM_LOWER)) {
			str = str.substring(str.indexOf(FROM_LOWER) + 5, str.length());
		} else if (str.contains(FROM_UPPER)) {
			str = str.substring(str.indexOf(FROM_UPPER) + 5, str.length());
		} else {
			return;
		}
		for (String key : JOIN_KEY) {
			for (String s : str.split(key)) {
				tables.add(s.split(" ")[0]);
			}
		}
	}
 
	private static void splitSQL(String sql, List<String> temp) {
		// 根据from关键字切割SQL
		sql = sql.contains("from") ? sql.substring(sql.indexOf("from"), sql.length()) : sql.substring(sql.indexOf("FROM"), sql.length());
		// 是否需要二次切割
		if (getSplitKey(sql) != null) {
			for (String s : sql.split(getSplitKey(sql))) {
				if (getSplitKey(s) != null) {
					splitSQL(s, temp);
				} else {
					temp.add(s);
				}
			}
		} else {
			temp.add(sql);
		}
	}
 
	private static String getSplitKey(String str) {
		if (StringUtils.isNotBlank(str)) {
			for (String split : SPLIT_KEY) {
				if (str.contains(split)) {
					return split;
				}
			}
		}
		return null;
	}
 
	private static String formatSQL(String sql) {
		sql = sql.replaceAll("\\s+", " ");
		sql = sql.replaceAll("\\s+,\\s+", ",");
		return sql;
	}
	
	@Test
	public void test1(){
		StringBuffer sBuffer = new StringBuffer("select p.NAME, ");//姓名
		sBuffer.append("case p.gender_code when '1' then '男性' when '2' then '女性' else '未知' end as GENDER, ");//性别
		sBuffer.append("p.BIRTH_DATE, ");//出生日期
		sBuffer.append("(select d.name from cendic.d_dictionary_item d where d.code = p.nation_code and d.d_code = 'DIC_GBT3304_1991') as NATION, ");//民族
		sBuffer.append("p.NATIVE_PLACE, ");//籍贯
		sBuffer.append("(select d.name from cendic.d_dictionary_item d where d.code = p.EDUCATION_CODE and d.d_code='DIC_GB4658_2006') EDUCATION, ");//文化程度
		sBuffer.append("(");//所属科室名称
		sBuffer.append("select wm_concat(d.dept_name) from cen_reg.t_department d ");
		sBuffer.append("where exists ( select 1 from  cen_reg.t_department_employee de where de.dept_id = d.id and de.emp_id = e1.id )");
		sBuffer.append(") as DEPT_NAMES ");//oracle程序创建的方法wm_concat结果用','分隔
		sBuffer.append("from ");
		sBuffer.append("(select e.id,e.person_id from cen_reg.t_employee e where org_id=:orgId ");
		sBuffer.append(") e1 ");
		sBuffer.append("left join cen_reg.t_reg_person p ");
		sBuffer.append("on e1.person_id = p.id ");
		
		String sql = sBuffer.toString();
		System.out.println(sql);
		
		List<String> names = DaoSqlReader.getSelectColumns(sql, DbType.oracle.name());
		
		System.out.println(names);
		
	}
 
}