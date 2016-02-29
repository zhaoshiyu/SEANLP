package cn.edu.kmust.seanlp.segmenter;

import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Config.BaseConf;
import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
* 越南语分词器<br>
* 越南语分词器的基类（Abstract）<br>
* 
* @author  Zhao Shiyu
*
*/
public abstract class AbstractVietnameseSegmenter extends AbstractSegmenter implements VietnameseSegmenter {
	
	/**
	 * 构造分词器，同时配置语言
	 */
	public AbstractVietnameseSegmenter() {
		BaseConf.seletcLanguage(Language.Vietnamese);
	}
	
	protected abstract List<Term> segmentSentence(char[] sentence);
	
	/**
	 * 越南语分词方法
	 * @param sentence 句子字符数组
	 * @return
	 */
	protected abstract List<Term> segmentSentence(String[] sentence);
	
	public List<Term> segmentSentence(String sentence) {
		return segmentSentence(sentence.split(" "));
	}
	
	protected List<Term> sentenceMerge(String[] sentences) {
		List<Term> termList = new LinkedList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			termList.addAll(segmentSentence(sentences[i].split(" ")));
			//termList.add(new Term(".",  Nature.CH));
		}
		return termList;
	}
	
	public List<Term> segment(String text) {
		text = text.replaceAll("[\\pP]+", " $0");
		return sentenceMerge(sentenceSegment(text));
	}

}
