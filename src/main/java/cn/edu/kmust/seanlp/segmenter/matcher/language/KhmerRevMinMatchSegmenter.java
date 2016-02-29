package cn.edu.kmust.seanlp.segmenter.matcher.language;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.POS.KhmerPOS;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.dictionary.language.KhmerCommonDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractKhmerSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.Matcher;
import cn.edu.kmust.seanlp.segmenter.matcher.ReverseMinimumMatcher;

/**
 * 柬埔寨语逆向最小匹配算法分词
 * 
 * @author Zhao Shiyu
 *
 */
public class KhmerRevMinMatchSegmenter  extends AbstractKhmerSegmenter {
	
	private Matcher revMinMatcher = new ReverseMinimumMatcher(KhmerCommonDictionary.khmerDictionary.dictionaryTrie);
	private POS pos = new KhmerPOS();
	
	@Override
	protected List<Term> segmentSentence(String[] sentence) {
		List<Term> ret = revMinMatcher.segment(sentence);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		List<Term> ret = revMinMatcher.segment(sentence);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		String text = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		KhmerRevMinMatchSegmenter seg = new KhmerRevMinMatchSegmenter();
		System.out.println(seg.segment(text));
		String line = "";
		for (Term term : seg.segment(text)) {
			line += term.getWord() + "|";
		}
		System.err.println(text);
		System.out.println(line.replaceAll("\\|", ""));
		
	}

}
