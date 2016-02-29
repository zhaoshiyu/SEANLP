package cn.edu.kmust.seanlp.segmenter;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 分词器接口</br>
 * 所有分词器的接口
 * @author  Zhao Shiyu
 *
 */
public interface Segmenter {
	
	/**
	 * 分词接口
	 * @param sentence 带分词的句子
	 * @return
	 */
	List<Term> segment(String text);

}
