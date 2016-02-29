package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.ThaiDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 泰语DoubleArrayTrie最长分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class ThaiDATTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter datThaiSegment = new ThaiDoubleArrayTrieSegmenter();
	
	/**
	 * 分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return datThaiSegment.segment(text);
	}


}
