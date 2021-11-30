import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.wondersgroup.commondao.dao.custom.QueryConditionCommonOrder;
import com.wondersgroup.commondao.dao.daofactory.CommonSql;
import com.wondersgroup.commondao.dao.daoutil.anotation.Id;
import com.wondersgroup.commondao.dao.daoutil.anotation.Table;
import com.wondersgroup.commondao.dao.daoutil.anotation.TableUtil;
import com.wondersgroup.commonutil.type.database.DataBaseType;

@Table(name="TEST_TABLE")
public class TestSql {
	
	@Id
	private String id;
	
	@Id
	private String id2;
	
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId2() {
		return id2;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Test
	public void test0() {
		TestSql testSql = new TestSql();
		Map<String, Object> paramMap = null;
		paramMap = TableUtil.object2Map(testSql);
		String[] fieldNameByIds =TableUtil.getIdsbyObj(testSql.getClass());
		String sql = CommonSql.saveOrUpdateSql(paramMap, testSql.getClass().getAnnotation(Table.class).name(),DataBaseType.MYSQL, fieldNameByIds);
		//String sql = CommonSql.selectSql4Map(new ArrayList<String>(Arrays.asList(fieldNameByIds)), testSql.getClass().getAnnotation(Table.class).name(), 5, 10, "oracle.jdbc.driver.OracleDriver", "JLGXSJ");
		System.out.println(sql);
	}
	
	@Test
	public void test1(){
		StringBuffer sql = new StringBuffer();

		sql.append("select ");
		sql.append("a.origin_system_id as \"id\", ");
		sql.append("a.origin_system_cname as \"orgname\", ");
		sql.append("count(distinct mb.model_id)  as \"intfNum\", ");
		sql.append("(select sum(data_num) from cen_brmp.brmp_conf_origin_system_mdbase where origin_system_id = a.origin_system_id group by origin_system_id) as \"intfDataNum\", ");
		sql.append("max(mb.model_updete_time) as \"modelUpdeteTime\", ");
		sql.append("count(distinct ap.apply_id) as \"applyNum\", ");
		sql.append("count(aplog.apply_id) as \"applyDataNum\", ");
		sql.append("max(aplog.req_date) as \"applyReqDate\", ");
		sql.append("'四川省' as \"xzqh\" ");
		
		
		sql.append("from (");
		sql.append(" select osys.origin_system_id, osys.origin_system_cname	from cen_brmp.brmp_conf_origin_system_info osys where osys.origin_system_id <> '1' ");
		sql.append(" orDer by osys.origin_system_id dEsc ");
		sql.append(") a");
		sql.append(" inner join ");
		sql.append(" cen_brmp.brmp_conf_origin_system_mdbase mb ");
		sql.append(" on mb.origin_system_id = a.origin_system_id ");
		sql.append(" left join ");
		sql.append(" cen_brmp.brmp_apply_base ap ");
		sql.append(" on mb.model_id = ap.model_id ");
		sql.append(" left join ");
		sql.append(" cen_brmp.brmp_log_req_apply aplog ");
		sql.append(" on ap.apply_id = aplog.apply_id ");
//		if (StringUtils.isNotBlank(orgname)) {
//			sql.append("  where a.origin_system_cname= :orgname ");
//			paramMap.put("orgname", orgname);
//		}
		sql.append(" group by a.origin_system_cname,a.origin_system_id ");
		sql.append(" order by a.origin_system_cname, a.origin_system_id");
		
		System.out.println(CommonSql.countSql(sql.toString()));
	}
	
	@Test
	public void test2(){
		String sql = CommonSql.selectSql4Obj(TestSql.class, 1, 20, DataBaseType.ORACLE, new HashMap<>(), new QueryConditionCommonOrder(true, "YYMC"));
		System.out.println(sql);
	}

}
