package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.LaoDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 老挝语DoubleArrayTrie最长分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class LaoDATTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter datLaoSegment = new LaoDoubleArrayTrieSegmenter();
	
	/**
	 * 分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return datLaoSegment.segment(text);
	}

}
