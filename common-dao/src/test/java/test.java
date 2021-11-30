import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wondersgroup.commondao.dao.custom.QueryCondition;
import com.wondersgroup.commondao.dao.custom.QueryConditionCommonIn;
import com.wondersgroup.commondao.dao.custom.QueryConditionCommonLess;
import com.wondersgroup.commondao.dao.custom.QueryConditionXzqh;
import com.wondersgroup.commondao.dao.daofactory.CommonSql;
import com.wondersgroup.commonutil.type.database.DataBaseType;

public class test {

	public static void main(String[] args) {
		QueryCondition[] queryConditions = new QueryCondition[3];
		queryConditions[0] = new QueryConditionCommonLess("JLGXSJ", new Date(), "JLGXSJ_custom", true);
		queryConditions[1] = new QueryConditionXzqh("XZQH", "510100");
		
		List<String> names = new ArrayList<String>();
		names.add("xiaoming");
		names.add("honghong");
		names.add("dajun");
		queryConditions[2] = new QueryConditionCommonIn("name", names ,"NAME_custom",false);
		
		
		List<String> attributeNames = new ArrayList<String>();
		attributeNames.add("test1");
		attributeNames.add("test2");
		
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("test1", 1);
		paramMap.put("test2", "t2");
		
		String sql = CommonSql.selectSql4Map(attributeNames,"tableName",1,10,DataBaseType.ORACLE,paramMap,queryConditions);
		System.out.println(sql);
		
		System.out.println(paramMap);
		
	}

}
