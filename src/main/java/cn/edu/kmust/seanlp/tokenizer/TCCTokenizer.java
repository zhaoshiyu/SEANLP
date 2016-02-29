package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.CC.AbstractCC;
import cn.edu.kmust.seanlp.segmenter.CC.TCC;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 泰语TCC切分器
 * 
 * @author Zhao Shiyu
 *
 */
public class TCCTokenizer {
	
	/**
	 * 预置TCC切分器
	 */	
	public final static AbstractCC tcc = new TCC();
	
	/**
	 * TCC切分
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return tcc.segment(text);
	}
	
	/**
	 * 切分TCC
	 * @param text
	 * @return
	 */
	public static String token(String text) {
		return tcc.token(text);
	}

}
