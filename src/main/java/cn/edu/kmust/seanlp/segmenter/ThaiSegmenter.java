package cn.edu.kmust.seanlp.segmenter;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 泰语分词器接口
 * 
 * @author  Zhao Shiyu
 *
 */
public interface ThaiSegmenter extends Segmenter {
	
	/**
	 * 泰语音节切分接口
	 * @param sentence 带切分的句子
	 * @return String
	 */
	String syllableSegment(String text);

	/**
	 * 双层条件随机场泰语分词
	 * @param sentence 待分词的句子
	 * @return List<Term>
	 */
	List<Term> dCRFWordSegment(String text);

	/**
	 * 单层条件随机场泰语分词
	 * @param sentence 待分词的句子
	 * @return List<Term>
	 */
	List<Term> gCRFWordSegment(String text);
	
	/**
	 * 音节切分和分词同时完成
	 * @param text
	 * @return
	 */
	List<Term> seg(String text);
	
	/**
	 * 对句子进行分词
	 * @param sentence
	 * @return
	 */
	public List<Term> segmentSentence(String sentence);

}
