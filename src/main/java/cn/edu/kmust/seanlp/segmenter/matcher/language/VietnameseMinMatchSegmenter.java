package cn.edu.kmust.seanlp.segmenter.matcher.language;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.POS.VietnamesePOS;
import cn.edu.kmust.seanlp.dictionary.language.VietnameseCoreDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractVietnameseSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.Matcher;
import cn.edu.kmust.seanlp.segmenter.matcher.MinimumMatcher;

/**
 * 越南语正向最小匹配算法分词
 * 
 * @author Zhao Shiyu
 *
 */
public class VietnameseMinMatchSegmenter extends AbstractVietnameseSegmenter {
	
	private Matcher minMatcher = new MinimumMatcher(VietnameseCoreDictionary.vietnameseDictionary.dictionaryTrie);
	private POS pos = new VietnamesePOS();
	
	@Override
	public List<Term> segmentSentence(String sentence) {
		List<Term> ret = minMatcher.segment(sentence);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		List<Term> ret = minMatcher.segment(sentence);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	@Override
	protected List<Term> segmentSentence(String[] sentence) {
		List<Term> ret = minMatcher.segment(sentence);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		String text = "Khi nhung trên đầu đã bắtđầu phân nhị , nhánh phân đã có hiện tượng chuẩn bị phân tam ( từ của những người nuôi hươu gọi là yên ngựa ) là lúc cần phải thu hoạch .";
		VietnameseMinMatchSegmenter seg = new VietnameseMinMatchSegmenter();
		System.out.println(seg.segment(text));
		String line = "";
		for (Term term : seg.segment(text)) {
			line += term.getWord() + "|";
		}
		System.err.println(text);
		System.out.println(line);
		
	}

	
}
