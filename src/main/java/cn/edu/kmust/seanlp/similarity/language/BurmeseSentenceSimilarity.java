package cn.edu.kmust.seanlp.similarity.language;

import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.Config.BaseConf;
import cn.edu.kmust.seanlp.similarity.SentenceSimilarity;
import cn.edu.kmust.seanlp.similarity.Similarity;

/**
 * 缅甸语句子相似度计算
 * 
 * @author Zhao Shiyu
 *
 */
public class BurmeseSentenceSimilarity {
	
	private static Similarity similarity = new SentenceSimilarity();
	
	public BurmeseSentenceSimilarity() {
		BaseConf.seletcLanguage(Language.Burmese);
	}

	public static double getSimilarity(String sent1, String sent2) {
		return similarity.getSimilarity(sent1, sent2);
	}
	
	

}
