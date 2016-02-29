package cn.edu.kmust.seanlp.segmenter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.Config.BaseConf;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 老挝语分词器<br>
 * 老挝语分词器的基类（Abstract）<br>
 * 
 * @author  Zhao Shiyu
 *
 */
public abstract class AbstractLaoSegmenter extends AbstractSegmenter implements LaoSegmenter {
	
	/**
	 * 构造分词器，同时配置语言
	 */
	public AbstractLaoSegmenter() {
		BaseConf.seletcLanguage(Language.Lao);
	}
	
	protected abstract List<Term> segmentSentence(char[] sentence);
	
	public List<Term> segmentSentence(String sentence) {
		return segmentSentence(sentence.toCharArray());
	}
	
	protected List<Term> sentenceMerge(String[] sentences) {
		List<Term> terms = new ArrayList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			terms.addAll(segmentSentence(sentences[i]));
			//terms.add(new Term(".", Nature.PM));
		}
		return terms;
	}
	
	public List<Term> segment(String text) {
		return sentenceMerge(sentenceSegment(text));
	}

}
