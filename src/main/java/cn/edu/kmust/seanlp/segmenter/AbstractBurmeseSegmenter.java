package cn.edu.kmust.seanlp.segmenter;

import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.Config.BaseConf;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 缅甸语分词器<br>
 * 缅甸语分词器的基类（Abstract）<br>
 * 
 * @author  Zhao Shiyu
 *
 */
public abstract class AbstractBurmeseSegmenter  extends AbstractSegmenter implements BurmeseSegmenter {
	
	public AbstractBurmeseSegmenter() {
		BaseConf.seletcLanguage(Language.Burmese);
	}
	
	protected abstract List<Term> segmentSentence(char[] sentence);
	
	public List<Term> segmentSentence(String sentence) {
		return segmentSentence(sentence.toCharArray());
	}
	
	private List<Term> sentenceMerge(String[] sentences) {
		List<Term> termList = new LinkedList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			termList.addAll(segmentSentence(sentences[i]));
			//termList.add(new Term(".",  null));
		}
		return termList;
	}
	
	public List<Term> segment(String text) {
		return sentenceMerge(sentenceSegment(text));
	}

}
