package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.language.KhmerMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.KhmerMinMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.KhmerRevMaxMatchSegmenter;
import cn.edu.kmust.seanlp.segmenter.matcher.language.KhmerRevMinMatchSegmenter;

/**
 * 柬埔寨语正逆向匹配分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class KhmerMatchTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter maxMatchKhmerSegment = new KhmerMaxMatchSegmenter();
	public final static Segmenter minMatchKhmerSegment = new KhmerMinMatchSegmenter();
	public final static Segmenter rMaxMatchKhmerSegment = new KhmerRevMaxMatchSegmenter();
	public final static Segmenter rMinMatchKhmerSegment = new KhmerRevMinMatchSegmenter();
	
	/**
	 * 正向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> maxSegment(String text) {
		return maxMatchKhmerSegment.segment(text);
	}
	
	/**
	 * 正向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> minSegment(String text) {
		return minMatchKhmerSegment.segment(text);
	}
	
	/**
	 * 逆向最大分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMaxSegment(String text) {
		return rMaxMatchKhmerSegment.segment(text);
	}
	
	/**
	 * 逆向最小分词
	 * @param text
	 * @return
	 */
	public static List<Term> rMinSegment(String text) {
		return rMinMatchKhmerSegment.segment(text);
	}
	

}
