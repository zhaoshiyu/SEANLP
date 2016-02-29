package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.language.BurmeseMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.BurmeseMinMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.BurmeseRevMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.BurmeseRevMinMatchSegmenter;

/**
 * 缅甸语正逆向匹配分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class BurmeseMatchTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter maxMatchBurmeseSegment = new BurmeseMaxMatchSegmenter();
	public final static Segmenter minMatchBurmeseSegment = new BurmeseMinMatchSegmenter();
	public final static Segmenter rMaxMatchBurmeseSegment = new BurmeseRevMaxMatchSegmenter();
	public final static Segmenter rMinMatchBurmeseSegment = new BurmeseRevMinMatchSegmenter();
	
	/**
	 * 正向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> maxSegment(String text) {
		return maxMatchBurmeseSegment.segment(text);
	}
	
	/**
	 * 正向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> minSegment(String text) {
		return minMatchBurmeseSegment.segment(text);
	}
	
	/**
	 * 逆向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMaxSegment(String text) {
		return rMaxMatchBurmeseSegment.segment(text);
	}
	
	/**
	 * 逆向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMinSegment(String text) {
		return rMinMatchBurmeseSegment.segment(text);
	}

}
