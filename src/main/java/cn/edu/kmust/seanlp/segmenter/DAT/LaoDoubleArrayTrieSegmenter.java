package cn.edu.kmust.seanlp.segmenter.DAT;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.collection.trie.DATrie;
import cn.edu.kmust.seanlp.dictionary.CoreDictionary;
import cn.edu.kmust.seanlp.dictionary.language.LaoCoreDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractLaoSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Nature;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 使用DoubleArrayTrie实现的最长分词器
 * 
 * @author  Zhao Shiyu
 *
 */
public class LaoDoubleArrayTrieSegmenter extends AbstractLaoSegmenter {
	
	/**
	 * 构造分词器，同时配置语言
	 */
	public LaoDoubleArrayTrieSegmenter() {
		super();
		Config.BaseConf.useCustomDictionary = false;
	}
	
	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		char[] charArray = sentence;
		final int[] wordNet = new int[charArray.length];
		Arrays.fill(wordNet, 1);
		final Nature[] natureArray = Config.BaseConf.speechTagging ? new Nature[charArray.length]
				: null;
		DATrie<CoreDictionary.Attribute>.Searcher searcher = LaoCoreDictionary.laoDictionary.dictionaryTrie.getSearcher(sentence, 0);
		while (searcher.next()) {
			int length = searcher.length;
			if (length > wordNet[searcher.begin]) {
				wordNet[searcher.begin] = length;
				if (Config.BaseConf.speechTagging) {
					natureArray[searcher.begin] = searcher.value.nature[0];
				}
			}
		}
		LinkedList<Term> termList = new LinkedList<Term>();
		if (Config.BaseConf.speechTagging) {
			for (int i = 0; i < natureArray.length;) {
				if (natureArray[i] == null) {
					int j = i + 1;
					for (; j < natureArray.length; ++j) {
						if (natureArray[j] != null)
							break;
					}
					
					List<Term> nodeList = quickSegment(charArray,i, j);
					for (Term node : nodeList) {
						if (node.getWord().length() >= wordNet[i]) {
							wordNet[i] = node.getWord().length();
							natureArray[i] = node.getNature();
							i += wordNet[i];
						}
					}
					i = j;
				} else {
					++i;
				}
			}
		}
		for (int i = 0; i < wordNet.length;) {
			Term term = new Term(new String(charArray, i, wordNet[i]),
					Config.BaseConf.speechTagging ? (natureArray[i] == null ? Nature.PM	: natureArray[i]) : null);
			term.setOffset(i);
			termList.add(term);
			i += wordNet[i];
		}
		return termList;
	}
	
	protected static List<Term> quickSegment(char[] charArray, int start, int end) {
		List<Term> nodeList = new LinkedList<Term>();
		int offsetAtom = start;
		while (++offsetAtom < end) {
			nodeList.add(new Term(new String(charArray, start, offsetAtom - start), null));
			start = offsetAtom;
		}
		if (offsetAtom == end)
			nodeList.add(new Term(new String(charArray, start, offsetAtom - start), null));

		return nodeList;
	}
	
}
