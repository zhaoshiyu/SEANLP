package cn.edu.kmust.seanlp.similarity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.kmust.math.Statistics;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * sentence similarity
 * 
 * @author Zhao Shiyu
 *
 */
public class SentenceSimilarity extends AbstractSimilarity {

	
	public final static double threshold = 0.2;
	
	/**
	 * 计算两个已分词句子的相似度
	 * @param sent1
	 * @param sent2
	 * @return 相似度
	 * @throws Exception
	 */
	@Override
	protected double getSimilarity(List<Term> sent1, List<Term> sent2) throws Exception {
		int size1 = sent1.size(), size2 = sent2.size();
		if (sent1  != null && size1 > 0 && sent2 != null && size2 > 0) {
			Map<String, double[]> union = new HashMap<String, double[]>();
			// 句子1和句子2的并集
			String index = null;
			for (int i = 0; i < size1; i++) {
				index = sent1.get(i).getWord();
				if (index != null) {
					double[] c = union.get(index);
					c = new double[2];
					c[0] = 1; // 句子1的语义分数Ci
					c[1] = threshold;// 句子2的语义分数Ci
					union.put(index, c);
				}
			}
			for (int i = 0; i < size2; i++) {
				index = sent2.get(i).getWord();
				if (index != null) {
					double[] c = union.get(index);
					if (c != null && c.length == 2) {
						c[1] = 1; // 词在句子2中也存在，词在句子2的语义分数=1
					} else {
						c = new double[2];
						c[0] = threshold; // 词在句子1的语义分数Ci
						c[1] = 1; // 词在句子2的语义分数Ci
						union.put(index, c);
					}
				}
			}
			// 取出句子的语义分数
			Iterator<String> it = union.keySet().iterator();
			//int size = size1 > size2 ? size1 : size2;
			int size = union.size();
			double[] s1 = new double[size];
			double[] s2 = new double[size];
			int count = 0;
			while (it.hasNext()) {
				double[] c = union.get(it.next());
				s1[count] = c[0];
				s2[count] = c[1];
				++count;
			}
			// 计算余弦相似度
			return Statistics.includedAngleCosine(s1, s2);
		} else {
			throw new Exception("parameter error");
		}
	}
	
	/**
	 * 计算两个句子的相似度
	 * @param sent1
	 * @param sent2
	 * @return
	 */
	public static double similarity(String sent1, String sent2) {
		SentenceSimilarity ss = new SentenceSimilarity();
		return ss.getSimilarity(sent1, sent2);
	}

}
