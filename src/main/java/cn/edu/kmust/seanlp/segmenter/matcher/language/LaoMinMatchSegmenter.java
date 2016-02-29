package cn.edu.kmust.seanlp.segmenter.matcher.language;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.POS.LaoPOS;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.dictionary.language.LaoCoreDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractLaoSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.Matcher;
import cn.edu.kmust.seanlp.segmenter.matcher.MinimumMatcher;

/**
 * 老挝语正向最小匹配算法分词
 * 
 * @author Zhao Shiyu
 *
 */
public class LaoMinMatchSegmenter extends AbstractLaoSegmenter {
	
	private Matcher minMatcher = new MinimumMatcher(LaoCoreDictionary.laoDictionary.dictionaryTrie);
	private POS pos = new LaoPOS();
	
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
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		String text = "ທ່ານວິນເຄັນເປັນປະທານບໍລິສັດອຽວເຊີວີເອີແອນ.ວີ.ກຸ່ມບໍລິສັດການພິມຂອງຊາວດັດ.";
//		String text = "我是一个学生！";
		LaoMinMatchSegmenter seg = new LaoMinMatchSegmenter();
		System.out.println(seg.segment(text));
		String line = "";
		for (Term term : seg.segment(text)) {
			line += term.getWord() + "|";
		}
		System.err.println(text);
		System.out.println(line);
		
	}

	
}
