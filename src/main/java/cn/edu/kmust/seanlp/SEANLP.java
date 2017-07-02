package cn.edu.kmust.seanlp;

import java.util.List;

import cn.edu.kmust.seanlp.extractor.domain.KeyTerm;
import cn.edu.kmust.seanlp.extractor.language.BurmeseExtractor;
import cn.edu.kmust.seanlp.extractor.language.KhmerExtractor;
import cn.edu.kmust.seanlp.extractor.language.LaoExtractor;
import cn.edu.kmust.seanlp.extractor.language.ThaiExtractor;
import cn.edu.kmust.seanlp.extractor.language.VietnameseExtractor;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.similarity.language.BurmeseSentenceSimilarity;
import cn.edu.kmust.seanlp.similarity.language.KhmerSentenceSimilarity;
import cn.edu.kmust.seanlp.similarity.language.LaoSentenceSimilarity;
import cn.edu.kmust.seanlp.similarity.language.ThaiSentenceSimilarity;
import cn.edu.kmust.seanlp.similarity.language.VietnameseSentenceSimilarity;
import cn.edu.kmust.seanlp.tokenizer.BurmeseCRFTokenizer;
import cn.edu.kmust.seanlp.tokenizer.BurmeseDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.BurmeseMatchTokenizer;
import cn.edu.kmust.seanlp.tokenizer.BurmeseSyllableDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.KCCTokenizer;
import cn.edu.kmust.seanlp.tokenizer.KhmerCRFTokenizer;
import cn.edu.kmust.seanlp.tokenizer.KhmerDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.KhmerMatchTokenizer;
import cn.edu.kmust.seanlp.tokenizer.LaoDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.LaoMatchTokenizer;
import cn.edu.kmust.seanlp.tokenizer.ThaiCRFTokenizer;
import cn.edu.kmust.seanlp.tokenizer.ThaiDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.ThaiMatchTokenizer;
import cn.edu.kmust.seanlp.tokenizer.VietnameseCRFTokenizer;
import cn.edu.kmust.seanlp.tokenizer.VietnameseDATTokenizer;
import cn.edu.kmust.seanlp.tokenizer.VietnameseMatchTokenizer;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class SEANLP {
	
	public static class Thai {
		
		/**
		 * 泰语TCC切分
		 * @param text
		 * @return
		 */
		public static List<Term> segmentTCC(String text) {
			return KCCTokenizer.segment(text);
		}
		
		/**
		 * CRF泰语音节切分
		 * @param text
		 * @return
		 */
		public static String syllableSegment(String text) {
			return ThaiCRFTokenizer.syllableSegment(text);
		}
		
		/**
		 * 泰语DAT分词
		 * @param text
		 * @return
		 */
		public static List<Term> datSegment(String text) {
			return ThaiDATTokenizer.segment(text);
		}
		
		/**
		 * 泰语层叠CRF分词
		 * @param text
		 * @return
		 */
		public static List<Term> dCRFSegment(String text) {
			return ThaiCRFTokenizer.segment(text);
		}
		
		/**
		 * 泰语CRF分词
		 * @param text
		 * @return
		 */
		public static List<Term> gCRFSegment(String text) {
			return ThaiCRFTokenizer.segment(text);
		}
		
		/**
		 * 泰语正向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> maxSegment(String text) {
			return ThaiMatchTokenizer.maxSegment(text);
		}
		
		/**
		 * 泰语正向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> minSegment(String text) {
			return ThaiMatchTokenizer.minSegment(text);
		}
		
		/**
		 * 泰语逆向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMaxSegment(String text) {
			return ThaiMatchTokenizer.rMaxSegment(text);
		}
		
		/**
		 * 泰语逆向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMinSegment(String text) {
			return ThaiMatchTokenizer.rMinSegment(text);			
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
			return ThaiExtractor.extractKeyword(document, size);
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
			return ThaiExtractor.extractSummary(document, size);
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
			return ThaiExtractor.getSummary(document, max_length);
		}
		
		
		/**
		 * 句子相似度计算
		 * @param sent1
		 * @param sent2
		 * @return
		 */
		public static double sentenceSimilarity(String sent1, String sent2) {
			return ThaiSentenceSimilarity.getSimilarity(sent1, sent2);
		}
		
	}
	
	public static class Vietnamese {
		
		/**
		 * 越南语DAT分词
		 * @param text
		 * @return
		 */
		public static List<Term> datSegment(String text) {
			return VietnameseDATTokenizer.segment(text);
		}
		/**
		 * 越南语CRF分词
		 * @param text
		 * @return
		 */
		public static List<Term> crfSegment(String text) {
			return VietnameseCRFTokenizer.segment(text);
		}
		
		/**
		 * 越南语正向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> maxSegment(String text) {
			return VietnameseMatchTokenizer.maxSegment(text);
		}
		
		/**
		 * 越南语正向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> minSegment(String text) {
			return VietnameseMatchTokenizer.minSegment(text);
		}
		
		/**
		 * 越南语逆向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMaxSegment(String text) {
			return VietnameseMatchTokenizer.rMaxSegment(text);
		}
		
		/**
		 * 越南语逆向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMinSegment(String text) {
			return VietnameseMatchTokenizer.rMinSegment(text);			
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
			return VietnameseExtractor.extractKeyword(document, size);
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
			return VietnameseExtractor.extractSummary(document, size);
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
			return VietnameseExtractor.getSummary(document, max_length);
		}
		
		/**
		 * 句子相似度计算
		 * @param sent1
		 * @param sent2
		 * @return
		 */
		public static double sentenceSimilarity(String sent1, String sent2) {
			return VietnameseSentenceSimilarity.getSimilarity(sent1, sent2);
		}
		
	}
	
	public static class Lao {
		
		/**
		 * 老挝语DAT分词
		 * @param text
		 * @return
		 */
		public static List<Term> datSegment(String text) {
			return LaoDATTokenizer.segment(text);
		}
		
		/**
		 * 老挝语正向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> maxSegment(String text) {
			return LaoMatchTokenizer.maxSegment(text);
		}
		
		/**
		 * 老挝语正向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> minSegment(String text) {
			return LaoMatchTokenizer.minSegment(text);
		}
		
		/**
		 * 老挝语逆向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMaxSegment(String text) {
			return LaoMatchTokenizer.rMaxSegment(text);
		}
		
		/**
		 * 老挝语逆向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMinSegment(String text) {
			return LaoMatchTokenizer.rMinSegment(text);			
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
			return LaoExtractor.extractKeyword(document, size);
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
			return LaoExtractor.extractSummary(document, size);
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
			return LaoExtractor.getSummary(document, max_length);
		}
		
		/**
		 * 句子相似度计算
		 * @param sent1
		 * @param sent2
		 * @return
		 */
		public static double sentenceSimilarity(String sent1, String sent2) {
			return LaoSentenceSimilarity.getSimilarity(sent1, sent2);
		}
		
	}

	public static class Burmese {
		
		public static List<Term> syllableSegment(String text) {
			return BurmeseSyllableDATTokenizer.segment(text);
		}
		
		/**
		 * 缅甸语DAT分词
		 * @param text
		 * @return
		 */
		public static List<Term> datSegment(String text) {
			return BurmeseDATTokenizer.segment(text);
		}
		
		/**
		 * 缅甸语CRF分词
		 * @param text
		 * @return
		 */
		public static List<Term> crfSegment(String text) {
			return BurmeseCRFTokenizer.segment(text);
		}
		
		/**
		 * 缅甸语正向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> maxSegment(String text) {
			return BurmeseMatchTokenizer.maxSegment(text);
		}
		
		/**
		 * 缅甸语正向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> minSegment(String text) {
			return BurmeseMatchTokenizer.minSegment(text);
		}
		
		/**
		 * 缅甸语逆向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMaxSegment(String text) {
			return BurmeseMatchTokenizer.rMaxSegment(text);
		}
		
		/**
		 * 缅甸语逆向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMinSegment(String text) {
			return BurmeseMatchTokenizer.rMinSegment(text);			
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
			return BurmeseExtractor.extractKeyword(document, size);
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
			return BurmeseExtractor.extractSummary(document, size);
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
			return BurmeseExtractor.getSummary(document, max_length);
		}
		
		/**
		 * 句子相似度计算
		 * @param sent1
		 * @param sent2
		 * @return
		 */
		public static double sentenceSimilarity(String sent1, String sent2) {
			return BurmeseSentenceSimilarity.getSimilarity(sent1, sent2);
		}
		
	}

	public static class Khmer {
		/**
		 * 高棉语词典分词
		 * @param text
		 * @return
		 */
		public static List<Term> datSegment(String text) {
			return KhmerDATTokenizer.segment(text);
		}
		
		/**
		 * 柬埔寨语CRF分词
		 * @param text
		 * @return
		 */
		public static List<Term> crfSegment(String text) {
			return KhmerCRFTokenizer.segment(text);
		}
		
		/**
		 * 柬埔寨语正向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> maxSegment(String text) {
			return KhmerMatchTokenizer.maxSegment(text);
		}
		
		/**
		 * 柬埔寨语正向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> minSegment(String text) {
			return KhmerMatchTokenizer.minSegment(text);
		}
		
		/**
		 * 柬埔寨语逆向最大分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMaxSegment(String text) {
			return KhmerMatchTokenizer.rMaxSegment(text);
		}
		
		/**
		 * 柬埔寨语逆向最小分词
		 * @param text
		 * @return
		 */
		public static List<Term> reMinSegment(String text) {
			return KhmerMatchTokenizer.rMinSegment(text);			
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
			return KhmerExtractor.extractKeyword(document, size);
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
			return KhmerExtractor.extractSummary(document, size);
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
			return KhmerExtractor.getSummary(document, max_length);
		}
		
		/**
		 * 句子相似度计算
		 * @param sent1
		 * @param sent2
		 * @return
		 */
		public static double sentenceSimilarity(String sent1, String sent2) {
			return KhmerSentenceSimilarity.getSimilarity(sent1, sent2);
		}
		
	}

}
