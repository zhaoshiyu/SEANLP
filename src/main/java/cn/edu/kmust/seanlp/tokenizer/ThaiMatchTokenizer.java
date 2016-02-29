package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.language.ThaiMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.ThaiMinMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.ThaiRevMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.ThaiRevMinMatchSegmenter;

/**
 * 泰语正逆向匹配分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class ThaiMatchTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter maxMatchThaiSegment = new ThaiMaxMatchSegmenter();
	public final static Segmenter minMatchThaiSegment = new ThaiMinMatchSegmenter();
	public final static Segmenter rMaxMatchThaiSegment = new ThaiRevMaxMatchSegmenter();
	public final static Segmenter rMinMatchThaiSegment = new ThaiRevMinMatchSegmenter();
	
	/**
	 * 正向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> maxSegment(String text) {
		return maxMatchThaiSegment.segment(text);
	}
	
	/**
	 * 正向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> minSegment(String text) {
		return minMatchThaiSegment.segment(text);
	}
	
	/**
	 * 逆向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMaxSegment(String text) {
		return rMaxMatchThaiSegment.segment(text);
	}
	
	/**
	 * 逆向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMinSegment(String text) {
		return rMinMatchThaiSegment.segment(text);
	}


}
