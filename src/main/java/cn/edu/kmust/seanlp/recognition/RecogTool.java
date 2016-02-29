package cn.edu.kmust.seanlp.recognition;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.Log;

/**
 *数字(包括泰语、越南语、柬埔寨语、老挝语、缅甸语数字)、英语单词等特殊情况识别工具
 * @author Zhao Shiyu
 *
 */
public class RecogTool {
	
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
	 * 阿拉伯数字识别，包括全角和半角
	 * @param c
	 * @return
	 */
	public static boolean isDigit(char c) {
        if(c > '9' && c < '０') {
            return false;
        }
        if(c < '0') {
            return false;
        }
        if(c > '９') {
            return false;
        }
        return true;
    }
	
	/**
	 * 数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isDigit(String text, int offset, int count) {
        for(int i = offset; i < offset + count; i++) {
            if(!isDigit(text.charAt(i))) {
                return false;
            }
        }
        //指定的字符串已经识别为数字串
        //下面要判断数字串是否完整
        if(offset > 0){
            //判断前一个字符，如果为数字字符则识别失败
            if(isDigit(text.charAt(offset - 1))) {
                return false;
            }
        }
        if(offset + count < text.length()) {
            //判断后一个字符，如果为数字字符则识别失败
            if(isDigit(text.charAt(offset + count))) {
                return false;
            }
        }
        if (Config.DEBUG) {
        	Log.logger.severe("识别出数字：" + text.substring(offset, offset + count));
        }
        return true;
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
	 * 整数前缀
	 * @param c
	 * @return
	 */
	public static boolean isPlusAndMinus(char c) {
		if (c == '-' || c == '+') return true;
		return false;
	}
	
	/**
	 * 识别整数
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isInteger(String text, int offset, int count) {
		char c = text.charAt(offset);
		if (count - offset == 1 && !isDigit(c)) {
			return false;
		}
		//-0和+0也是整数
		if ((count - offset == 2) &&  (((!isDigit(c) && !isPlusAndMinus(c)) || c == '0') || !isDigit(text.charAt(offset + 1)))) {
			return false;
		}
		if (count - offset > 2) {
			if (!isDigit(c) && !isPlusAndMinus(c) || c == '0') {
				return false;
			}
			if (isPlusAndMinus(c) && (text.charAt(offset + 1) == '0')) {
				return false;
			}
			for(int i = (offset + 1); i < offset + count; i++) {
				if(!isDigit(text.charAt(i))) {
					return false;
				}
			}
		}
        //指定的字符串已经识别为数字串
        //下面要判断数字串是否完整
        if(offset > 0){
            //判断前一个字符，如果为数字字符则识别失败
            if(isDigit(text.charAt(offset - 1))) {
                return false;
            }
        }
        if(offset + count < text.length()) {
            //判断后一个字符，如果为数字字符则识别失败
            if(isDigit(text.charAt(offset + count))) {
                return false;
            }
        }
        if (Config.DEBUG) {
        	Log.logger.severe("识别出整数：" + text.substring(offset, offset + count));
        }
        return true;
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
		if (count < 3) {
            return false;
        }
        int index = -1;
        for (int i = offset; i < (offset + count); i++) {
            char c = text.charAt(i);
            if(c=='.' || c=='/' || c=='／' || c=='．' || c=='·') {
                index = i;
                break;
            }
        }
        if (index == -1 || index == offset || index == offset + count - 1) {
            return false;
        }
        //如果是分数，/后面第一位不能为0
        if ((text.charAt(index) == '/' || text.charAt(index) == '／') && text.charAt(index + 1) == '0') {
        	return false;
        }
        int beforeLen = index - offset;
        return isInteger(text, offset, beforeLen) && isDigit(text, index+1, count-(beforeLen+1));
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
	 * 识别英文字符，包括大小写，全角和半角
	 * @param c
	 * @return
	 */
	public static boolean isEnglish(char c){
        //大部分字符在这个范围
        if(c > 'z' && c < 'Ａ'){
            return false;
        }
        if(c < 'A'){
            return false;
        }
        if(c > 'Z' && c < 'a'){
            return false;
        }
        if(c > 'Ｚ' && c < 'ａ'){
            return false;
        }
        if(c > 'ｚ'){
            return false;
        }
        return true;
    }
	
	/**
	 * 识别英文单词
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isEnglish(String text, int offset, int count) {
        for (int i = offset; i < offset + count; i++) {
            char c = text.charAt(i);
            if (!isEnglish(c)) {
                return false;
            }
        }
        //指定的字符串已经识别为英文串
        //下面要判断英文串是否完整
        if (offset > 0) {
            //判断前一个字符，如果为英文字符则识别失败
            char c = text.charAt(offset - 1);
            if (isEnglish(c)) {
                return false;
            }
        }
        if (offset + count < text.length()) {
            //判断后一个字符，如果为英文字符则识别失败
            char c = text.charAt(offset + count);
            if (isEnglish(c)) {
                return false;
            }
        }
        if (Config.DEBUG) {
        	Log.logger.severe("识别出英文单词：" + text.substring(offset, offset + count));
        }
        return true;
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
	 * 泰语数字识别
	 * {'๐','๑','๒','๓','๔','๕','๖','๗','๘','๙'}
	 * @param c
	 * @return
	 */ 
	public static boolean isThaiDigit(char c) {
        if(c < '๐') {
            return false;
        }
        if(c > '๙') {
            return false;
        }
        return true;
    }
	
	/**
	 *泰语数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isThaiDigit(String text, int offset, int count) {
        for(int i = offset; i < offset + count; i++) {
            if(!isThaiDigit(text.charAt(i))) {
                return false;
            }
        }
        //指定的字符串已经识别为数字串
        //下面要判断数字串是否完整
        if(offset > 0){
            //判断前一个字符，如果为数字字符则识别失败
            if(isThaiDigit(text.charAt(offset - 1))) {
                return false;
            }
        }
        if(offset + count < text.length()) {
            //判断后一个字符，如果为数字字符则识别失败
            if(isThaiDigit(text.charAt(offset + count))) {
                return false;
            }
        }
        if (Config.DEBUG) {
        	Log.logger.severe("识别出泰语数字：" + text.substring(offset, offset + count));
        }
        return true;
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
	 * 老挝语数字识别
	 *{'໐','໑','໒','໓','໔','໕','໖','໗','໘','໙'}
	 * @param c
	 * @return
	 */ 
	public static boolean isLaoDigit(char c) {
        if(c < '໐') {
            return false;
        }
        if(c > '໙') {
            return false;
        }
        return true;
    }
	
	/**
	 *老挝语数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isLaoDigit(String text, int offset, int count) {
        for(int i = offset; i < offset + count; i++) {
            if(!isLaoDigit(text.charAt(i))) {
                return false;
            }
        }
        //指定的字符串已经识别为数字串
        //下面要判断数字串是否完整
        if(offset > 0){
            //判断前一个字符，如果为数字字符则识别失败
            if(isLaoDigit(text.charAt(offset - 1))) {
                return false;
            }
        }
        if(offset + count < text.length()) {
            //判断后一个字符，如果为数字字符则识别失败
            if(isLaoDigit(text.charAt(offset + count))) {
                return false;
            }
        }
        if (Config.DEBUG) {
        	Log.logger.severe("识别出老挝语数字：" + text.substring(offset, offset + count));
        }
        return true;
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
	 * 柬埔寨语数字识别
	 *[\u17E0-\u17E9\u17F0-\u17F9]
	 * @param c
	 * @return
	 */ 
	public static boolean isKhmerDigit(char c) {
		 if(c > '\u17E9' && c < '\u17F0') {
	            return false;
	        }
	        if(c < '\u17E0') {
	            return false;
	        }
	        if(c > '\u17F9') {
	            return false;
	        }
	        return true;
    }
	
	/**
	 *柬埔寨语数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isKhmerDigit(String text, int offset, int count) {
        for(int i = offset; i < offset + count; i++) {
            if(!isKhmerDigit(text.charAt(i))) {
                return false;
            }
        }
        //指定的字符串已经识别为数字串
        //下面要判断数字串是否完整
        if(offset > 0){
            //判断前一个字符，如果为数字字符则识别失败
            if(isKhmerDigit(text.charAt(offset - 1))) {
                return false;
            }
        }
        if(offset + count < text.length()) {
            //判断后一个字符，如果为数字字符则识别失败
            if(isKhmerDigit(text.charAt(offset + count))) {
                return false;
            }
        }
        if (Config.DEBUG) {
        	Log.logger.severe("识别出柬埔寨语数字：" + text.substring(offset, offset + count));
        }
        return true;
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
	 *[\u1040-\u1049\u1090-\u1099]
	 * @param c
	 * @return
	 */ 
	public static boolean isMyanmarDigit(char c) {
		 if(c > '\u1049' && c < '\u1090') {
	            return false;
	        }
	        if(c < '\u1040') {
	            return false;
	        }
	        if(c > '\u1099') {
	            return false;
	        }
	        return true;
    }
	
	/**
	 * 缅甸语数字识别
	 * @param text
	 * @param offset
	 * @param count
	 * @return
	 */
	public static boolean isMyanmarDigit(String text, int offset, int count) {
        for(int i = offset; i < offset + count; i++) {
            if(!isMyanmarDigit(text.charAt(i))) {
                return false;
            }
        }
        //指定的字符串已经识别为数字串
        //下面要判断数字串是否完整
        if(offset > 0){
            //判断前一个字符，如果为数字字符则识别失败
            if(isMyanmarDigit(text.charAt(offset - 1))) {
                return false;
            }
        }
        if(offset + count < text.length()) {
            //判断后一个字符，如果为数字字符则识别失败
            if(isMyanmarDigit(text.charAt(offset + count))) {
                return false;
            }
        }
        if (Config.DEBUG) {
        	Log.logger.severe("识别出柬埔寨语数字：" + text.substring(offset, offset + count));
        }
        return true;
    }
	
	/**
	 *  缅甸语数字识别
	 * @param text
	 * @return
	 */
	public static boolean isMyanmarDigit(String text) {
		return isMyanmarDigit(text, 0, text.length());
	}
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		System.out.println(isPlusAndMinus('-'));
		System.out.println(isInteger("9896"));
		
		System.out.println(isFraction("0.9"));
		System.out.println(isDigit(" "));
		
	}

}
