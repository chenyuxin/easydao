import java.util.Map;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.wondersgourp.test.xml.City;
import com.wondersgourp.test.xml.CityList;
import com.wondersgroup.commonutil.xml.XmlBeanUtils;
import com.wondersgroup.commonutil.xml.XmlMapUtils;


public class TestXml {
	
	 @Test
	 public void test1() {
		 String xml1 = "<A1>大\\人\"</A1>";
		 Map<String, Object> A = XmlMapUtils.xmlToMap(xml1);
		 System.out.println(A);
	 }
	

    @Test
    public void TestXml1() {
    	//String xml1 = "<A1><B>1</B><B2>2</B2><B3><C1>1</C1><C2>2</C2></B3></A1><A2>也是</A2>";
    	String xml1 = "<A1><C1>1</C1><C1>2</C1><C1>3</C1><C1>4</C1></A1><A1><B>1</B><B2>2021-03-25 13:58:03</B2></A1><A1>是</A1>";
    	Map<String, Object> A = XmlMapUtils.xmlToMap(xml1);
    	//A.put("B", 2);
    	//A.put("C", 3);
    	
    	System.out.println(JSON.toJSONString(A));
    	String xml = XmlMapUtils.mapToXml(A);
    	System.out.println(xml);
    	
	}
    
    @Test
    public void TestXml2() {
    	City city = new City();
    	city.setCityCode("BEIJING");
    	city.setCityId("1");
    	city.setCityName("北京");
    	city.setProvince("北京");
    	
    	City city2 = new City();
    	city2.setCityCode("CHENGDU");
    	city2.setCityId("2");
    	city2.setCityName("成都");
    	city2.setProvince("四川");
    	
    	CityList cityList = new CityList();
    	cityList.addCity(city2);
    	cityList.addCity(city);
    	
    	cityList.setCapital(city);
    	
//    	String xml = XmlBeanUtils.beanToXml(city);
    	String xml = XmlBeanUtils.beanToXml(cityList);
    	System.out.println(xml);
    	
    	Map<String, Object> map = XmlMapUtils.xmlToMap(xml);
    	System.out.println("map: " + JSON.toJSONString(map));
    	
    	String xml2 = XmlMapUtils.mapToXml(map);
    	System.out.println("xml2: " + xml2);
    	
    	CityList cityListObj = XmlBeanUtils.xmlToBean(xml, CityList.class);
    	System.out.println("cityListObj: " + JSON.toJSONString(cityListObj));
    	
    }
    
}