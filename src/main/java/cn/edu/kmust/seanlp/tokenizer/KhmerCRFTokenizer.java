package cn.edu.kmust.seanlp.tokenizer;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.KhmerCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 柬埔寨语CRF分词器
 * 
 * @author Zhao Shiyu
 *
 */
public class KhmerCRFTokenizer {
	
	/**
	 * 预置分词器
	 */
	public final static Segmenter crfKhmerSegment = new KhmerCRFSegmenter();
	
	/**
	 * 分词
	 * @param text
	 * @return
	 */
	public static List<Term> segment(String text) {
		return crfKhmerSegment.segment(text);
	}
	
	

}
