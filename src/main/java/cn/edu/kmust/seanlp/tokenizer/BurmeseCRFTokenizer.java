package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.BurmeseCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 缅甸语CRF分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class BurmeseCRFTokenizer {
	
	/**
	 * 预置分词器
	 */
	public final static Segmenter crfBurmeseSegment = new BurmeseCRFSegmenter();
	
	/**
	 * 分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return crfBurmeseSegment.segment(text);
	}
	
	

}
