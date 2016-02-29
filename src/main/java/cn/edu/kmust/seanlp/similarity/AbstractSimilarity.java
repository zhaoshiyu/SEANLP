package cn.edu.kmust.seanlp.similarity;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.tokenizer.BurmeseDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.KhmerDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.LaoDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.ThaiCRFTokenizer;
import cn.edu.kmust.seanlp.tokenizer.VietnameseCRFTokenizer;

/**
 * 相似度计算基类
 * 
 * @author Zhao Shiyu
 *
 */
public abstract class AbstractSimilarity implements Similarity {
	
	/**
	 * 默认分词器
	 */
	protected static Segmenter defaultSegment;
	
	public AbstractSimilarity() {
		switch (Config.language) {
		case Thai :
//			defaultSegment = ThaiDictTokenizer.dictThaiSegment;
			defaultSegment = ThaiCRFTokenizer.crfThaiSegment;
			break;
			
		case Vietnamese :
			defaultSegment = VietnameseCRFTokenizer.crfVietnameseSegment;
//			defaultSegment = VietnameseDictTokenizer.dictVietnameseSegment;
			break;
			
		case Lao :
			defaultSegment = LaoDATTokenizer.datLaoSegment;
			break;
			
		case Khmer :
			defaultSegment = KhmerDATTokenizer.datKhmerSegment;
			break;
			
		case Burmese :
			defaultSegment = BurmeseDATTokenizer.datBurmeseSegment;
			break;
			
		default:
			defaultSegment = ThaiCRFTokenizer.crfThaiSegment;
			break;
		}
	}
	
	/**
	 * 计算两个已分词句子的相似度
	 * @param sent1
	 * @param sent2
	 * @return
	 * @throws Exception
	 */
	protected abstract double getSimilarity(List<Term> sent1, List<Term> sent2) throws Exception;
	
	/**
	 * 计算两个句子的相似度
	 * @param sent1
	 * @param sent2
	 * @return
	 */
	public double getSimilarity(String sent1, String sent2) {
		double sim = 0;
		try {
			sim = getSimilarity(defaultSegment.segment(sent1), defaultSegment.segment(sent2));
		} catch (Exception e) {
			e.printStackTrace();
			Log.logger.severe("传参有问题！");
		}
		return sim;
	}

}
