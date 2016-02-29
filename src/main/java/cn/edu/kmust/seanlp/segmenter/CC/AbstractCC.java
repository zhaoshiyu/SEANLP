package cn.edu.kmust.seanlp.segmenter.CC;

import cn.edu.kmust.seanlp.regex.AbstractRegex;
import cn.edu.kmust.seanlp.segmenter.Segmenter;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public abstract class AbstractCC extends AbstractRegex implements Segmenter {
	
	/**
	 * 有或没有
	 */
	protected final static String question_mark = "?";
	/**
	 * 一个或多个
	 */
	protected final static String plus = "+";
	/**
	 * 左括号
	 */
	protected final static String left_parenthesis = "(";
	/**
	 * 右括号
	 */
	protected final static String right_parenthesis = ")";
	
	
	
	public abstract String token(String sentence);
	
	

}
