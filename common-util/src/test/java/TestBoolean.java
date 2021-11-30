import org.junit.jupiter.api.Test;

public class TestBoolean {
	
	private int inta = 0;
	private boolean bo = true;
	
	@Test
	public void test1() {
		boolean a = 0==inta;
		System.out.println("a:" + a);
		
		if(bo ^ a ) {
			System.out.println("进来了");
		} else {
			System.out.println("else出来");
		}
		
		
	}
	

}
