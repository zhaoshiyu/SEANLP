package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.KhmerCommonDATSegmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.KhmerDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 柬埔寨语DoubleArrayTrie最长分词器
 * @author Zhao Shiyu
 *
 */
public class KhmerDATTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter datKhmerSegment = new KhmerDoubleArrayTrieSegmenter();
	public final static Segmenter datCommKhmerSegmenter = new KhmerCommonDATSegmenter();
	
	/**
	 * 分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return datKhmerSegment.segment(text);
	}
	
	public static List<Term> segByCommDict(String text) {
		return datCommKhmerSegmenter.segment(text);
	}

}
