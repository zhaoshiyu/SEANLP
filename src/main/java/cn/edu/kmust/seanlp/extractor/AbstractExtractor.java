package cn.edu.kmust.seanlp.extractor;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.tokenizer.BurmeseDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.KhmerDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.LaoDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.ThaiCRFTokenizer;
import cn.edu.kmust.seanlp.tokenizer.VietnameseCRFTokenizer;

/**
 * 关键词抽取和自动摘要基类
 * 主要分配分词模型
 * 
 * @author Zhao Shiyu
 *
 */
public abstract class AbstractExtractor {
	
	/**
	 * 默认分词器
	 */
	protected static Segmenter defaultSegment;
	
	public AbstractExtractor() {
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
			
		case Myanmar :
			defaultSegment = BurmeseDATTokenizer.datBurmeseSegment;
			break;
			
		default:
			defaultSegment = ThaiCRFTokenizer.crfThaiSegment;
			break;
		}
	}

}
