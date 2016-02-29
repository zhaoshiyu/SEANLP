package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.language.LaoMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.LaoMinMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.LaoRevMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.LaoRevMinMatchSegmenter;

/**
 * 老挝语正逆向匹配分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class LaoMatchTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter maxMatchLaoSegment = new LaoMaxMatchSegmenter();
	public final static Segmenter minMatchLaoSegment = new LaoMinMatchSegmenter();
	public final static Segmenter rMaxMatchLaoSegment = new LaoRevMaxMatchSegmenter();
	public final static Segmenter rMinMatchLaoSegment = new LaoRevMinMatchSegmenter();
	
	/**
	 * 正向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> maxSegment(String text) {
		return maxMatchLaoSegment.segment(text);
	}
	
	/**
	 * 正向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> minSegment(String text) {
		return minMatchLaoSegment.segment(text);
	}
	
	/**
	 * 逆向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMaxSegment(String text) {
		return rMaxMatchLaoSegment.segment(text);
	}
	
	/**
	 * 逆向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMinSegment(String text) {
		return rMinMatchLaoSegment.segment(text);
	}

}
