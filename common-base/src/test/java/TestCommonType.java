

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;

import com.wondersgroup.commonutil.type.language.LanguageType;
import com.wondersgroup.commonutil.type.language.XMLType;



public class TestCommonType implements InitializingBean {
	
	private static Integer a = 1;
	
	public final Double d = 2.2;
	
	public Map<String, Object> m = new HashMap<String, Object>();
	
	public List<LanguageType> l = new ArrayList<LanguageType>();
	
	public static String ss = "ss";
	
	public String aString = "aString";
	
	public Set<String> set = new HashSet<String>();
	
	public Object[] arrayO = {1,XMLType.getInstance(), new Date()};
	
	
	TestCommonType(String str){
		this.set.add(str);
	}
	TestCommonType(){
		
	}
	
	@Test
	public void test1(){


	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	public static Integer getA() {
		return a;
	}

	public static void setA(Integer a) {
		TestCommonType.a = a;
	}

	public Map<String, Object> getM() {
		return m;
	}

	public void setM(Map<String, Object> m) {
		this.m = m;
	}

	public List<LanguageType> getL() {
		return l;
	}

	public void setL(List<LanguageType> l) {
		this.l = l;
	}

	public static String getSs() {
		return ss;
	}

	public static void setSs(String ss) {
		TestCommonType.ss = ss;
	}

	public String getaString() {
		return aString;
	}

	public void setaString(String aString) {
		this.aString = aString;
	}

	public Set<String> getSet() {
		return set;
	}

	public void setSet(Set<String> set) {
		this.set = set;
	}

	public Object[] getArrayO() {
		return arrayO;
	}

	public void setArrayO(Object[] arrayO) {
		this.arrayO = arrayO;
	}

	public Double getD() {
		return d;
	}
	
	
	
}
