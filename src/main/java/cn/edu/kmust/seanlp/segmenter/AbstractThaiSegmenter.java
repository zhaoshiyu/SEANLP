package cn.edu.kmust.seanlp.segmenter;

import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.Config.BaseConf;
import cn.edu.kmust.seanlp.segmenter.CC.TCC;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 泰语分词器<br>
 * 泰语分词器的基类（Abstract）<br>
 * 
 * @author  Zhao Shiyu
 *
 */
public abstract class AbstractThaiSegmenter extends AbstractSegmenter implements ThaiSegmenter {
	
	/**
	 * 构造分词器，同时配置语言
	 */
	public AbstractThaiSegmenter() {
		BaseConf.seletcLanguage(Language.Thai);
	}
	
	protected String[] toTCC(String sentence) {
		return TCC.toTCC(sentence);
	}
	
	/**
	 * 音节切分<br>
	 * 对多个泰语句子进行处理<br>
	 * @param sentences 句子数组
	 * @return
	 */
	protected abstract String syllableSegment(String[] sentences);
	
	/**
	 * 音节切分<br>
	 * 对单个泰语句子进行处理<br>
	 * @param chars 句子字符数组
	 * @return 返回以|分割的字符串
	 */
	protected abstract StringBuffer syllableSegment(char[] chars);
	
	/**
	 * 实现音节切分<br>
	 * 对单个句子进行处理<br>
	 * @param chars 句子字符数组
	 * @return 返回字符串音节数组
	 */
	protected abstract String[] sentenceTosyllables(char[] chars);
	
	/**
	 * 音节切分<br>
	 * 对单个句子进行处理<br>
	 * @param sentence 句子
	 * @return
	 */
	protected abstract String[] sentenceTosyllables(String sentence);
	
	/**
	 * 音节切分<br>
	 * 对多个句子进行处理<br>
	 * @param sentences 句子数组
	 * @return
	 */
	protected abstract List<Term> syllableMerge(String[] sentences);
	
	/**
	 *  音节合并<br>
	 * 对单个句子进行处理<br>
	 * @param sentence 句子
	 * @return
	 */
	protected abstract List<Term> syllableMerge(String sentence);
	
	/**
	 * 音节合并
	 * 对单个句子进行处理<br>
	 * @param syllables 待合并的音节数组
	 * @return List<Term>
	 */
	protected abstract List<Term> syllableMerging(String[] syllables);
	
	/**
	 * 音节合并<br>
	 * 对多个句子进行处理<br>
	 * @param syllables 待合并的音节数组
	 * @return List<Term>
	 */
	protected abstract List<Term> gCRFWordSegment(String[] sentences);
	
	/**
	 * 双层条件随机场泰语分词
	 * @param sentences 待分词的句子数组
	 * @return List<Term>
	 */
	protected abstract List<Term> segment(String[] sentences);
	
	/**
	 * 实现双层条件随机场泰语分词
	 * @param chars
	 * @return
	 */
	protected abstract List<Term> segment(char[] chars);
	
	/**
	 * 实现单层条件随机场分词<br>
	 * @param chars 句子字符数组
	 * @return List<Term>
	 */
	protected abstract List<Term> gCRFWordSegment(char[] chars);
	
	public List<Term> segmentSentence(String sentence) {
		return segment(sentence.toCharArray());
	}
	
	protected List<Term> sentenceMerge(String[] sentences) {
		List<Term> termList = new LinkedList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			termList.addAll(segmentSentence(sentences[i]));
			//termList.add(new Term(" ",  Nature.PUNC));
		}
		return termList;
	}
	
	public List<Term> segment(String text) {
		return sentenceMerge(sentenceSegment(text));
	}
	
}
