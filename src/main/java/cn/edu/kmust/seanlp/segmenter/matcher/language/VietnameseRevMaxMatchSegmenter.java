package cn.edu.kmust.seanlp.segmenter.matcher.language;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.POS.VietnamesePOS;
import cn.edu.kmust.seanlp.dictionary.language.VietnameseCoreDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractVietnameseSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.Matcher;
import cn.edu.kmust.seanlp.segmenter.matcher.ReverseMaximumMatcher;

/**
 * 越南语逆向最大匹配算法分词
 * 
 * @author Zhao Shiyu
 *
 */
public class VietnameseRevMaxMatchSegmenter  extends AbstractVietnameseSegmenter {
	
	private Matcher revMaxMatcher = new ReverseMaximumMatcher(VietnameseCoreDictionary.vietnameseDictionary.dictionaryTrie);
	private POS pos = new VietnamesePOS();
	
	@Override
	public List<Term> segmentSentence(String sentence) {
		List<Term> ret = revMaxMatcher.segment(sentence);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		List<Term> ret = revMaxMatcher.segment(sentence);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	@Override
	protected List<Term> segmentSentence(String[] sentence) {
		List<Term> ret = revMaxMatcher.segment(sentence);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		String text = "Khi nhung trên đầu đã bắtđầu phân nhị , nhánh phân đã có hiện tượng chuẩn bị phân tam ( từ của những người nuôi hươu gọi là yên ngựa ) là lúc cần phải thu hoạch .";
		VietnameseRevMaxMatchSegmenter seg = new VietnameseRevMaxMatchSegmenter();
		System.out.println(seg.segment(text));
		String line = "";
		for (Term term : seg.segment(text)) {
			line += term.getWord() + "|";
		}
		
		System.err.println(text);
		System.out.println(line);
		
	}
	
}
