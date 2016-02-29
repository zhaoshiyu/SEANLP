package cn.edu.kmust.seanlp.segmenter.DAT;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.collection.trie.DATrie;
import cn.edu.kmust.seanlp.dictionary.language.BurmeseSyllableDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractBurmeseSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 使用DoubleArrayTrie实现的最长分词器
 * 
 * @author  Zhao Shiyu
 *
 */
public class BurmeseSyllableDATSegmenter extends AbstractBurmeseSegmenter {
	
	/**
	 * 构造分词器，同时配置语言
	 */
	public BurmeseSyllableDATSegmenter() {
		super();
		Config.BaseConf.useCustomDictionary = false;
	}

	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		final int[] wordNet = new int[sentence.length];
		Arrays.fill(wordNet, 1);
		DATrie<String>.Searcher searcher = BurmeseSyllableDictionary.burmeseSyllableDictionary.dictionaryTrie.getSearcher(sentence, 0);
//		System.out.println(BurmeseSyllableDictionary.burmeseSyllableDictionary.dictionaryTrie.contains("ကျ့ၩ့"));
//		System.out.println(BurmeseSyllableDictionary.burmeseSyllableDictionary.dictionaryTrie.contains("ကျၨၪ့"));
//		System.out.println(BurmeseSyllableDictionary.burmeseSyllableDictionary.dictionaryTrie.contains("ကြ့ာ်"));
		while (searcher.next()) {
			int length = searcher.length;
			if (length > wordNet[searcher.begin]) {
				wordNet[searcher.begin] = length;
			}
		}
		
		LinkedList<Term> termList = new LinkedList<Term>();
		for (int i = 0; i < wordNet.length;) {
			Term term = new Term(new String(sentence, i, wordNet[i]), null);
			term.setOffset(i);
			termList.add(term);
			i += wordNet[i];
		}
		return termList;
	}

}
