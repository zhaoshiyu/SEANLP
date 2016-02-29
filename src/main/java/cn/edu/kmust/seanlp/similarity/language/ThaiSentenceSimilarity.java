package cn.edu.kmust.seanlp.similarity.language;

import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.Config.BaseConf;
import cn.edu.kmust.seanlp.similarity.SentenceSimilarity;
import cn.edu.kmust.seanlp.similarity.Similarity;

/**
 * 泰语句子相似度计算
 * 
 * @author Zhao Shiyu
 *
 */
public class ThaiSentenceSimilarity {
	
	private static Similarity similarity = new SentenceSimilarity();
	
	public ThaiSentenceSimilarity() {
		BaseConf.seletcLanguage(Language.Thai);
	}
	
	public static double getSimilarity(String sent1, String sent2) {
		return similarity.getSimilarity(sent1, sent2);
	}

}
