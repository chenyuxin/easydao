import org.junit.jupiter.api.Test;
import com.wondersgroup.commonutil.pinyin.PinyinUtil;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class TestPinyin {
	
	@Test
	public void test1() {
		try {
			System.out.println(PinyinUtil.getPinYin("基督18i"));
			System.out.println(PinyinUtil.getFirstSpell("陈 薛	涌"));
			System.out.println(PinyinUtil.getFirstSpell("风湿免疫科"));
			System.out.println(PinyinUtil.getFirstSpell("司马 懿			1A18i"));
			System.out.println(PinyinUtil.getFullSpell("A诸葛亮 "));
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}

}
