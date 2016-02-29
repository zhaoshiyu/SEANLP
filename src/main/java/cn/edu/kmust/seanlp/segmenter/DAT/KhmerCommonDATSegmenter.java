package cn.edu.kmust.seanlp.segmenter.DAT;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.POS.KhmerPOS;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.collection.trie.DATrie;
import cn.edu.kmust.seanlp.dictionary.language.KhmerCommonDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractKhmerSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 使用DoubleArrayTrie实现的最长分词器
 * 
 * @author  Zhao Shiyu
 *
 */
public class KhmerCommonDATSegmenter extends AbstractKhmerSegmenter {
	
	private final POS pos = new KhmerPOS();
	
	/**
	 * 构造分词器，同时配置语言
	 */
	public KhmerCommonDATSegmenter() {
		super();
		Config.BaseConf.useCustomDictionary = false;
	}

	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		final int[] wordNet = new int[sentence.length];
		Arrays.fill(wordNet, 1);
//		final Nature[] natureArray = Config.BaseConf.speechTagging ? new Nature[charArray.length]	: null;
		DATrie<String>.Searcher searcher = KhmerCommonDictionary.khmerDictionary.dictionaryTrie.getSearcher(sentence, 0);
		while (searcher.next()) {
			int length = searcher.length;
			if (length > wordNet[searcher.begin]) {
				wordNet[searcher.begin] = length;
			}
		}
		
		List<Term> termList = new LinkedList<Term>();
		for (int i = 0; i < wordNet.length;) {
//			Term term = new Term(new String(charArray, i, wordNet[i]), Config.BaseConf.speechTagging ? (natureArray[i] == null ? Nature.WXXX	: natureArray[i]) : null);
			Term term = new Term(new String(sentence, i, wordNet[i]), null);
			term.setOffset(i);
			termList.add(term);
			i += wordNet[i];
		}
		if (Config.BaseConf.speechTagging) {
			termList = pos.speechTagging(termList);
		}
		return termList;
	}
	
	@Override
	public List<Term> segmentSentence(String sentence) {
		return segmentSentence(sentence.toCharArray());
	}

	@Override
	protected List<Term> segmentSentence(String[] sentence) {
		return null;
	}

}
