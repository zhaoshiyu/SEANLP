package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.ThaiSegmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.ThaiCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 泰语CRF分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class ThaiCRFTokenizer  {
	
	/**
	 * 预置分词器
	 */
	public final static ThaiSegmenter crfThaiSegment = new ThaiCRFSegmenter();

	/**
	 * 层叠CRF分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return crfThaiSegment.segment(text);
	}
	
	/**
	 * 单层CRF分词
	 * @param text
	 * @return
	 */
	public static List<Term> gCRFSegment(String text) {
		return crfThaiSegment.gCRFWordSegment(text);
	}
	
	/**
	 * CRF音节切分
	 * @param text
	 * @return
	 */
	public static String syllableSegment(String text) {
		return crfThaiSegment.syllableSegment(text);
	}

}
