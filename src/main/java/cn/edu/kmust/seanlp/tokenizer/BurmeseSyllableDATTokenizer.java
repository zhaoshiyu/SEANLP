package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.BurmeseSyllableDATSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 缅甸语DoubleArrayTrie最长音节切分器
 * 
 * @author Zhao Shiyu
 *
 */
public class BurmeseSyllableDATTokenizer {
	
	/**
	 * 预置音节切分器
	 */	
	public final static Segmenter datBurmeseSyllableSegment = new BurmeseSyllableDATSegmenter();
	
	/**
	 * 音节切分
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return datBurmeseSyllableSegment.segment(text);
	}

}
