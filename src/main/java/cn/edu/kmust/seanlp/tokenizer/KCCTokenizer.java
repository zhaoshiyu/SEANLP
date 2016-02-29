package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.CC.AbstractCC;
import cn.edu.kmust.seanlp.segmenter.CC.KCC;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 *柬埔寨语 KCC 切分器
 *
 * @author Zhao Shiyu
 *
 */
public class KCCTokenizer {
	
	/**
	 * 预置KCC切分器
	 */	
	public final static AbstractCC kcc = new KCC();
	
	/**
	 * KCC切分
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return kcc.segment(text);
	}
	
	/**
	 * 切分KCC
	 * @param text
	 * @return
	 */
	public static String token(String text) {
		return kcc.token(text);
	}

}
