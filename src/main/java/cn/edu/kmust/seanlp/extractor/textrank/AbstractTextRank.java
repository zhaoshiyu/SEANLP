package cn.edu.kmust.seanlp.extractor.textrank;

import cn.edu.kmust.seanlp.extractor.KeywordExtractor;

/**
 * TextRank
 * 
 * @author Zhao Shiyu
 *
 */
public abstract class AbstractTextRank extends KeywordExtractor {
	
	/**
	 * 阻尼系数，一般取值为0.85
	 */
	public final static float d = 0.85f;
	/**
	 * 最大迭代次数
	 */
	public final static int max_iter = 200;
	public final static float min_diff = 0.001f;

}
