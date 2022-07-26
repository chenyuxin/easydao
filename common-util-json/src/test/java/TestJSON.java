import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public class TestJSON {

	@Test
	public void test1(){
		String json = "\\";
		json = json.replaceAll("\\\\", "\\\\\\\\");
		json = json.replaceAll("\"", "\\\\\\\"");
		System.out.println(json);
		String jsonO = "{\"a\":\"" + json +"\"}";
		
		@SuppressWarnings("rawtypes")
		Map aMap = JSONObject.parseObject(jsonO, Map.class);
		
		System.out.println(JSON.toJSONString(aMap));
		
		System.out.println(aMap.get("a"));
		
	}
	
	@Test
	public void test2(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", "大\\人\"");
		String json = JSON.toJSONString(map);
		System.out.println(json);
	}

}
