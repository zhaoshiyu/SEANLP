package cn.edu.kmust.seanlp.segmenter.matcher.language;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.dictionary.language.BurmeseCommonDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractBurmeseSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.Matcher;
import cn.edu.kmust.seanlp.segmenter.matcher.ReverseMaximumMatcher;

/**
 * 缅甸语逆向最大匹配算法分词
 * @author Zhao Shiyu
 *
 */
public class BurmeseRevMaxMatchSegmenter  extends AbstractBurmeseSegmenter {
	
	private Matcher revMaxMatcher = new ReverseMaximumMatcher(BurmeseCommonDictionary.burmeseDictionary.dictionaryTrie);
	
	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		return revMaxMatcher.segment(sentence);
	}
	
	@Override
	public List<Term> segmentSentence(String sentence) {
		return revMaxMatcher.segment(sentence);
	}
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		String text = "ကံဆိုးကံဇာတာကံထိုက်ကံနခိုကံနှိုးဆော်";
		BurmeseRevMaxMatchSegmenter seg = new BurmeseRevMaxMatchSegmenter();
		System.out.println(seg.segment(text));
		String line = "";
		for (Term term : seg.segment(text)) {
			line += term.getWord() + "|";
		}
		
		System.err.println(text);
		System.out.println(line);
		
	}

}
