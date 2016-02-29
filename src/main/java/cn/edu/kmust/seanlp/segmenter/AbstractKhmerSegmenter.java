package cn.edu.kmust.seanlp.segmenter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.kmust.seanlp.Config.BaseConf;
import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.segmenter.CC.KCC;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
* 高棉语分词器<br>
* 越南语分词器的基类（Abstract）<br>
* 
* @author  Zhao Shiyu
*
*/
public abstract class AbstractKhmerSegmenter extends AbstractSegmenter implements KhmerSegmenter {
	
	/**
	 * 构造分词器，同时配置语言
	 */
	public AbstractKhmerSegmenter() {
		BaseConf.seletcLanguage(Language.Khmer);
	}
	
	protected String[] toKCC(String sentence) {
		return KCC.toKCC(sentence);
	}
	
	protected String[] splitSpace(String sentence) {
		return sentence.split(" ");
	}
	
	protected abstract List<Term> segmentSentence(char[] sentence);
	
//	/**
//	 * 条件随机场高棉语分词方法
//	 * @param sentence 句子字符数组
//	 * @return
//	 */
	protected abstract List<Term> segmentSentence(String[] sentence);
	
	public List<Term> segmentSentence(String sentence) {
		return segmentSentence(toKCC(sentence));
	}
	
	private List<Term> sentenceMerge(String[] sentences) {
		List<Term> terms = new ArrayList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			terms.addAll(segmentSentence(sentences[i]));
			//terms.add(new Term("។", Nature.M));
		}
		return terms;
	}
	
	public List<Term> segment(String text) {
		return sentenceMerge(sentenceSegment(text));
	}

}
