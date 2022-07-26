import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader.Feature;
import com.wondersgroup.commonutil.CommonUtil;

public class TestFastJson2 {
	
	@Test
	public void test1() {
		Map<String, Object> map = new HashMap<>();
		map.put("TestJson", "a123");
		map.put("testJson2", new Date());
		map.put("test_Json", null);
		
		String json = CommonUtil.toJSONString(map);
		System.out.println(json);
		
		Map<String, Object> map0 = JSON.parseObject(json,Feature.UseNativeObject);
		System.out.println(map0);
	}
	
	@Test
	public void test2() {
		TestUser user = new TestUser();
//		user.setName("陈");
		user.setName(null);
		user.setAge(5);
		user.setBrithDate(new Date());
//		user.setBrithDate(null);
		user.setMoney(new BigDecimal("99999999.99"));
		user.setId(888888888888888888L);
		
		String json = CommonUtil.toJSONString(user);
		System.out.println(json);
		
		TestUser testUser = JSON.parseObject(json, TestUser.class,Feature.UseNativeObject,Feature.FieldBased);
		System.out.println(testUser);
		
	}
	
	
	public class TestUser {
		
		String name;
		int age;
		Date brithDate;
		BigDecimal money;
		Long id;
		
		@Override
		public String toString() {
			return "TestUser [name=" + name + ", age=" + age + ", brithDate=" + brithDate + ", money=" + money + ", id="
					+ id + "]";
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public Date getBrithDate() {
			return brithDate;
		}
		public void setBrithDate(Date brithDate) {
			this.brithDate = brithDate;
		}
		public BigDecimal getMoney() {
			return money;
		}
		public void setMoney(BigDecimal money) {
			this.money = money;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		
	}

}
