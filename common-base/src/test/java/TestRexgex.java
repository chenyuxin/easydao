

import org.junit.jupiter.api.Test;

import com.wondersgroup.commonutil.constant.RegexConst;

public class TestRexgex {
	
	@Test
	public void test1() {
		//String date = "2000-02-01 17:23:23";
		String date = "20200131";
		
		boolean a = date.matches(RegexConst.yyyyMMdd);
		System.out.println("yyyyMMdd:" + a);
		
		boolean b = date.matches(RegexConst.DATE);
		System.out.println("DATE:" + b);
		
		boolean IDCARD = "51110219880411203x3".matches(RegexConst.IDCARD);
		System.out.println("IDCARD:" + IDCARD);
		
	}
	
	@Test
	public void testTel() {
		String tel = "0833-83510263";
		
		boolean a = tel.matches(RegexConst.TEL);
		System.out.println("tel:" + a);
	}
	
	@Test
	public void testPHONE() {
		String PHONE = "18908017419";
		
		boolean a = PHONE.matches(RegexConst.PHONE);
		System.out.println("PHONE:" + a);
	}
	
	
	@Test
	public void testEmail() {
		String EMAIL = "none@none.com";//
		boolean a = EMAIL.matches(RegexConst.EMAIL);
		System.out.println("EMAIL:" + a);
	}
	
	@Test
	public void testIP() {
		String iPv4 = "192.168.1.103";//
		boolean a = iPv4.matches(RegexConst.IPV4);
		System.out.println("iPv4:" + a);
	}

}
