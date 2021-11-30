
import org.junit.jupiter.api.Test;

import com.wondersgroup.commonutil.cipher.BASE64;

public class TestCipher {
	
	@Test
	public void TestBase64() {
		String str = "就是中国@Ab123";
		str = BASE64.encryptBASE64(str);
		System.out.println(str);
	}
	
	@Test
	public void TestBase64d() {
		String string = "5bCx5piv5Lit5Zu9QEFiMTIz";
		string = BASE64.decryptBASE64(string);
		System.out.println(string);
	}
	

	
	

}