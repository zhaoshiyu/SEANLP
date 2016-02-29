package cn.edu.kmust.seanlp.regex;

/**
 * 常用正则表达式，与正则表达式相关类的基类
 * 
 * @author Zhao Shiyu
 *
 */
public abstract class AbstractRegex {
	
	/**
	 * 数字
	 */
	protected final static String DIGITS = "[0-9]+" ;
	/**
	 * 英语字符
	 */
	protected final static String ENGLISH = "[a-zA-Z]+" ;
	/**
	 * 标点符号
	 */
	protected final static String PUNCTUATION = "[\\pP‘’“”]+";//("\r" + orex  + "\n") +  ;
	/**
	 * 换行符
	 */
	protected final static String NEWLINES = "[\r\n]+";//("\r" + orex  + "\n") +  ;
	/**
	 * 所有空白字符
	 */
	protected final static String WS = "[\\s]+";//( " " + orex + "\t" + orex + "\n" + orex + "\r" ) + ;
	/**
	 * 其它所有为匹配到的字符
	 */
	protected final static String DONT_KNOW = ".";//;( WS + orex + ("0".."9") +  + orex + ("a".."z" + orex  + "A".."Z") +   + orex + ( "\u0E50".."\u0E59") +  + orex + .) ; //ALso detect consecutive Thai numbers.
	/**
	 * 或
	 */
	protected final static String orex = "|";
	
	/**
	 * 数字
	 */
	protected final static String digit = "^[0-9]+$" ;
	
	/**
	 * 整数
	 */
	protected final static String integer = "^(\\-|\\+)?[1-9]\\d+$" ;
	
	/**
	 * 小数
	 */
	protected final static String fraction = "^(\\-|\\+)?\\d+(\\.|/|．|·|／)\\d+$" ; 
	
	/**
	 * 泰语数字
	 */
	protected final static String digitThai = "^[\u0E50-\u0E59]+$" ; 
	
	/**
	 * 柬埔寨语数字
	 */
	protected final static String digitKhmer = "^[\u17E0-\u17E9\u17F0-\u17F9]+$" ;
	
	/**
	 * 老挝语数字
	 */
	protected final static String digitLao = "^[\u0ED0-\u0ED9]+$" ; 
	
	/**
	 * 缅甸语数字
	 */
	protected final static String digitMyanmar = "^[\u1040-\u1049\u1090-\u1099]+$" ; 
	
	/**
	 * 所有数字
	 */
	protected final static String number = "^[0-9]+$" + orex + integer + orex + fraction+ orex + digitThai + orex + digitLao+ orex + digitKhmer + orex + digitMyanmar;
	
	/**
	 * 英语单词
	 */
	protected final static String english = "^[a-zA-Z]+$" ;

}
