package cn.edu.kmust.seanlp.extractor;

import cn.edu.kmust.seanlp.dictionary.stopword.StopWordDictionary;
import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 *  提取关键词的基类
 *  
 * @author Zhao Shiyu
 *
 */
public class KeywordExtractor extends AbstractExtractor {
	
	/**
	 * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
	 *
	 * @param term
	 * @return 是否应当
	 */
	public boolean shouldInclude(Term term) {
		// 除掉停用词
		if (term.getNature() == null)
			return false;
		String nature = term.getNature().toString();
		char firstChar = nature.charAt(0);
		switch (firstChar) {
		case 'm':
		case 'b':
		case 'c':
		case 'e':
		case 'o':
		case 'p':
		case 'q':
		case 'u':
		case 'y':
		case 'z':
		case 'r':
		case 'w': {
			return false;
		}
		default: {
			if (term.getWord().trim().length() > 1
					&& !StopWordDictionary.contains(term.getWord())) {
				return true;
			}
		}
			break;
		}

		return false;
	}

	/**
	 * 设置关键词提取器使用的分词器
	 * 
	 * @param segment
	 *            任何开启了词性标注的分词器
	 * @return 自己
	 */
	public KeywordExtractor setSegment(Segmenter segment) {
		defaultSegment = segment;
		return this;
	}
}
