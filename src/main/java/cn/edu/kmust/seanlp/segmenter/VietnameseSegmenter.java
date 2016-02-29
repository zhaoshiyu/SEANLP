package cn.edu.kmust.seanlp.segmenter;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 越南语分词器接口
 * 
 * @author  Zhao Shiyu
 *
 */
public interface VietnameseSegmenter extends Segmenter {
	
	/**
	 * 对句子进行分词
	 * @param sentence
	 * @return
	 */
	public List<Term> segmentSentence(String sentence);
	
}
