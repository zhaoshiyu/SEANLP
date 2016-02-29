package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.language.VietnameseMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.VietnameseMinMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.VietnameseRevMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.VietnameseRevMinMatchSegmenter;

/**
 * 越南语正逆向匹配分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class VietnameseMatchTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter maxMatchVietnameseSegment = new VietnameseMaxMatchSegmenter();
	public final static Segmenter minMatchVietnameseSegment = new VietnameseMinMatchSegmenter();
	public final static Segmenter rMaxMatchVietnameseSegment = new VietnameseRevMaxMatchSegmenter();
	public final static Segmenter rMinMatchVietnameseSegment = new VietnameseRevMinMatchSegmenter();
	
	/**
	 * 正向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> maxSegment(String text) {
		return maxMatchVietnameseSegment.segment(text);
	}
	
	/**
	 * 正向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> minSegment(String text) {
		return minMatchVietnameseSegment.segment(text);
	}
	
	/**
	 * 逆向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMaxSegment(String text) {
		return rMaxMatchVietnameseSegment.segment(text);
	}
	
	/**
	 * 逆向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMinSegment(String text) {
		return rMinMatchVietnameseSegment.segment(text);
	}

}
