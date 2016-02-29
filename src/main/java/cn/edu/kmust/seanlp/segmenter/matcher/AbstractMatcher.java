package cn.edu.kmust.seanlp.segmenter.matcher;

import cn.edu.kmust.seanlp.collection.trie.DATrie;

/**
 * 词典匹配算法基类
 * 
 * @author Zhao Shiyu
 *
 */
public abstract class AbstractMatcher implements Matcher {
	
	protected static int wordMaxLength = 20;
	protected static int wordMinLength = 1;
	
	protected static DATrie<?> dict;
	
	public AbstractMatcher(DATrie<?> datDict) {
		dict = datDict;
	}
	
}
