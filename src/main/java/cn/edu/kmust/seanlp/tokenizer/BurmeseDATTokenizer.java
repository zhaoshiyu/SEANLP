package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.BurmeseDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 缅甸语DoubleArrayTrie最长分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class BurmeseDATTokenizer {
	
	/**
	 * 预置分词器
	 */	
	public final static Segmenter datBurmeseSegment = new BurmeseDoubleArrayTrieSegmenter();
	
	/**
	 * 分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return datBurmeseSegment.segment(text);
	}

}
