package com.wondersgroup.commonutil.pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 *
 */
public class PinyinUtil {
	
	static {
		HanyuPinyinOutputFormat formatT = new HanyuPinyinOutputFormat();
		formatT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        formatT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        formatT.setVCharType(HanyuPinyinVCharType.WITH_V);
        format = formatT;
		
        HanyuPinyinOutputFormat defaultFormatT = new HanyuPinyinOutputFormat();
        defaultFormatT.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormatT.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        defaultFormatT.setVCharType(HanyuPinyinVCharType.WITH_V);
        defaultFormat = defaultFormatT;
	}
	
	private static final HanyuPinyinOutputFormat format;
	
	private static final HanyuPinyinOutputFormat defaultFormat;
	
	
	/**
     * 将字符串中的中文转化为拼音,其他字符不变
     * @param inputString
     * @return
	 * @throws BadHanyuPinyinOutputFormatCombination 
     */
    public static String getPinYin(String inputString) throws BadHanyuPinyinOutputFormatCombination {
    	StringBuffer output = new StringBuffer();
    	char[] input = inputString.replaceAll("\\s*", "").toCharArray();
		for (int i = 0; i < input.length; i++) {
			if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
		        String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
		        output.append(temp[0]);
			} else {
		        output.append(input[i]);
			}
		}
        return output.toString();
    }
	
    
    /**
     * 获取汉字串拼音首字母，英文字符不变
     * @param chineseString  汉字串
     * @return 汉语拼音首字母
     * @throws BadHanyuPinyinOutputFormatCombination 
     */
    public static String getFirstSpell(String chineseString) throws BadHanyuPinyinOutputFormatCombination {
    	StringBuffer pybf = new StringBuffer();
        char[] arr = chineseString.replaceAll("\\s*", "").toCharArray();
        for (int i = 0; i < arr.length; i++) {
	        if (arr[i] > 128) {
	            String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
	            if (temp != null) {
                    pybf.append(temp[0].charAt(0));
	            } else {
	            	pybf.append(arr[i]);
	            }
	        } else {
	        	pybf.append(arr[i]);
	        }
        }
        return pybf.toString().replaceAll("\\W", "");
    }
	
    /**
     * 获取汉字串拼音，英文字符不变 
     * @param chineseString  汉字串 
     * @return 汉语拼音
     * @throws BadHanyuPinyinOutputFormatCombination 
     */
    public static String getFullSpell(String chineseString) throws BadHanyuPinyinOutputFormatCombination {
    	StringBuffer pybf = new StringBuffer();
        char[] arr = chineseString.replaceAll("\\s*", "").toCharArray();
        for (int i = 0; i < arr.length; i++) {
	        if (arr[i] > 128) {
	        	pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], format)[0]);
	        } else {
	        	pybf.append(arr[i]);
	        }

        }
        return pybf.toString();
    }
	
	
	
	
	
	
	
	
}
