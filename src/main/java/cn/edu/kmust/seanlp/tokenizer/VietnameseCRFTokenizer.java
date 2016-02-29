package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.VietnameseCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 越南语CRF分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class VietnameseCRFTokenizer {
	
	/**
	 * 预置分词器
	 */
	public final static Segmenter crfVietnameseSegment = new VietnameseCRFSegmenter();

	/**
	 * 分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return crfVietnameseSegment.segment(text);
	}

}
