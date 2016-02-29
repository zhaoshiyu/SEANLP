package cn.edu.kmust.seanlp.regex.recognition;

import cn.edu.kmust.seanlp.regex.AbstractRegex;

/**
 * 正则表达式数字、英语单词等特殊情况识别工具
 * 
 * @author Zhao Shiyu
 *
 */
public class RegexRecog extends AbstractRegex {
	
	/**
	 * 识别文本数字、整数、小数、英语单词等
	 * @param text
	 * @return
	 */
	public static boolean recog(String text){
        return recog(text, 0, text.length());
    }
	
	/**
	 * 识别文本数字、整数、小数、英语单词等
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean recog(String text, int offset, int count){
        return isNumber(text, offset, count)
        		|| isEnglish(text, offset, count)
        		|| isThaiDigit(text, offset, count)
        		|| isLaoDigit(text, offset, count)
        		|| isKhmerDigit(text, offset, count)
        		|| isMyanmarDigit(text, offset, count);
    }
	
	/**
	 * 数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isDigit(String text, int offset, int count) {
        return text.substring(offset, offset + count).matches(digit);
    }
	
	
	/**
	 * 数字识别
	 * @param text
	 * @return
	 */
	public static boolean isDigit(String text) {
		return isDigit(text, 0, text.length());
	}
	
	/**
	 * 识别整数
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isInteger(String text, int offset, int count) {
		return text.substring(offset, offset + count).matches(integer);
	}
	
	/**
	 * 识别整数
	 * @param text
	 * @return
	 */
	public static boolean isInteger(String text) {
		return isInteger(text, 0, text.length());
	}
	
	/**
	 * 识别小数和分数
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isFraction(String text, int offset, int count) {
		return text.substring(offset, offset + count).matches(fraction);
	}
	
	/**
	 * 识别小数和分数
	 * @param text
	 * @return
	 */
	public static boolean isFraction(String text) {
		return isFraction(text, 0, text.length());
	}
	
	/**
	 * 识别所有数字，包括由数字字符串，整数，小数，分数
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isNumber(String text, int offset, int count) {
		return isDigit(text, offset, count) || isInteger(text, offset, count) || isFraction(text, offset, count);
	}
	
	/**
	 * 识别所有数字，包括由数字字符串，整数，小数，分数
	 * @param text
	 * @return
	 */
	public static boolean isNumber(String text) {
		return isNumber(text, 0, text.length());
	}
	
	/**
	 * 识别英文单词
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isEnglish(String text, int offset, int count) {
		return text.substring(offset, offset + count).matches(english);
    }
	
	/**
	 * 识别英文单词
	 * @param text
	 * @return
	 */
	public static boolean isEnglish(String text) {
		return isEnglish(text, 0, text.length());
	}
	
	/**
	 *泰语数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isThaiDigit(String text, int offset, int count) {
		return text.substring(offset, offset + count).matches(digitThai);
    }
	
	
	/**
	 * 泰语数字识别
	 * @param text
	 * @return
	 */
	public static boolean isThaiDigit(String text) {
		return isThaiDigit(text, 0, text.length());
	}
	
	/**
	 *老挝语数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isLaoDigit(String text, int offset, int count) {
		return text.substring(offset, offset + count).matches(digitLao);
    }
	
	
	/**
	 * 老挝语数字识别
	 * @param text
	 * @return
	 */
	public static boolean isLaoDigit(String text) {
		return isLaoDigit(text, 0, text.length());
	}
	
	/**
	 *柬埔寨语数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isKhmerDigit(String text, int offset, int count) {
		return text.substring(offset, offset + count).matches(digitKhmer);
    }
	
	
	/**
	 * 柬埔寨语数字识别
	 * @param text
	 * @return
	 */
	public static boolean isKhmerDigit(String text) {
		return isKhmerDigit(text, 0, text.length());
	}
	
	/**
	 * 缅甸语数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isMyanmarDigit(String text, int offset, int count) {
		return text.substring(offset, offset + count).matches(digitMyanmar);
    }
	
	/**
	 *  缅甸语数字识别
	 * @param text
	 * @return
	 */
	public static boolean isMyanmarDigit(String text) {
		return isMyanmarDigit(text, 0, text.length());
	}

}
