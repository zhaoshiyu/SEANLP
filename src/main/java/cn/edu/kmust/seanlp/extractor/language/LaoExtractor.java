package cn.edu.kmust.seanlp.extractor.language;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.extractor.domain.KeyTerm;
import cn.edu.kmust.seanlp.extractor.textrank.TextRankKeyword;
import cn.edu.kmust.seanlp.extractor.textrank.TextRankSummarization;

/**
 * 老挝语关键词抽取和自动摘要
 * 
 * @author Zhao Shiyu
 *
 */
public class LaoExtractor {
	
	static {
		Config.BaseConf.seletcLanguage(Language.Lao);
	}
	
	/**
	 * 提取关键词
	 * 
	 * @param document
	 *            文档内容
	 * @param size
	 *            希望提取几个关键词
	 * @return 一个列表
	 */
	public static List<KeyTerm> extractKeyword(String document, int size) {
		return TextRankKeyword.getKeywordList(document, size);
	}

	/**
	 * 自动摘要
	 * 
	 * @param document
	 *            目标文档
	 * @param size
	 *            需要的关键句的个数
	 * @return 关键句列表
	 */
	public static List<String> extractSummary(String document, int size) {
		return TextRankSummarization.getTopSentenceList(document, size);
	}

	/**
	 * 自动摘要
	 * 
	 * @param document
	 *            目标文档
	 * @param max_length
	 *            需要摘要的长度
	 * @return 摘要文本
	 */
	public static String getSummary(String document, int max_length) {
		return TextRankSummarization.getSummary(document, max_length);
	}

}
