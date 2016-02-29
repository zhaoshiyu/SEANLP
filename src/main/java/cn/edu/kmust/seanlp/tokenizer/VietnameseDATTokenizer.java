package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.VietnameseDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 越南语DoubleArrayTrie最长分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class VietnameseDATTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter datVietnameseSegment = new VietnameseDoubleArrayTrieSegmenter();
	
	/**
	 * 分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return datVietnameseSegment.segment(text);
	}

}
