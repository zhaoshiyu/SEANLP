package cn.edu.kmust.seanlp.extractor.textrank;

import java.util.*;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.dictionary.stopword.StopWordDictionary;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.util.StringUtil;

/**
 * TextRank 自动摘要
 *源自hankcs的对中文的实现
 * @author hankcs
 */
public class TextRankSummarization extends AbstractTextRank {
	
	/**
	 * 文档句子的个数
	 */
	int D;
	/**
	 * 拆分为[句子[单词]]形式的文档
	 */
	List<List<String>> docs;
	/**
	 * 排序后的最终结果 score <-> index
	 */
	TreeMap<Double, Integer> top;

	/**
	 * 句子和其他句子的相关程度
	 */
	double[][] weight;
	/**
	 * 该句子和其他句子相关程度之和
	 */
	double[] weight_sum;
	/**
	 * 迭代之后收敛的权重
	 */
	double[] vertex;

	/**
	 * BM25相似度
	 */
	BM25 bm25;

	public TextRankSummarization(List<List<String>> docs) {
		this.docs = docs;
		bm25 = new BM25(docs);
		D = docs.size();
		weight = new double[D][D];
		weight_sum = new double[D];
		vertex = new double[D];
		top = new TreeMap<Double, Integer>(Collections.reverseOrder());
		solve();
	}

	private void solve() {
		int cnt = 0;
		for (List<String> sentence : docs) {
			double[] scores = bm25.simAll(sentence);
			// System.out.println(Arrays.toString(scores));
			weight[cnt] = scores;
			weight_sum[cnt] = sum(scores) - scores[cnt]; // 减掉自己，自己跟自己肯定最相似
			vertex[cnt] = 1.0;
			++cnt;
		}
		for (int k = 0; k < max_iter; ++k) {
			double[] m = new double[D];
			double max_diff = 0;
			for (int i = 0; i < D; ++i) {
				m[i] = 1 - d;
				for (int j = 0; j < D; ++j) {
					if (j == i || weight_sum[j] == 0)
						continue;
					m[i] += (d * weight[j][i] / weight_sum[j] * vertex[j]);
				}
				double diff = Math.abs(m[i] - vertex[i]);
				if (diff > max_diff) {
					max_diff = diff;
				}
			}
			vertex = m;
			if (max_diff <= min_diff)
				break;
		}
		// 我们来排个序吧
		for (int i = 0; i < D; ++i) {
			top.put(vertex[i], i);
		}
	}

	/**
	 * 获取前几个关键句子
	 *
	 * @param size
	 *            要几个
	 * @return 关键句子的下标
	 */
	public int[] getTopSentence(int size) {
		Collection<Integer> values = top.values();
		size = Math.min(size, values.size());
		int[] indexArray = new int[size];
		Iterator<Integer> it = values.iterator();
		for (int i = 0; i < size; ++i) {
			indexArray[i] = it.next();
		}
		return indexArray;
	}

	/**
	 * 简单的求和
	 *
	 * @param array
	 * @return
	 */
	private static double sum(double[] array) {
		double total = 0;
		for (double v : array) {
			total += v;
		}
		return total;
	}

	/**
	 * 将文章分割为句子
	 *
	 * @param document
	 * @return
	 */
	static List<String> spiltSentence(String document) {
		List<String> sentences = new ArrayList<String>();
		for (String line : document.split("[။\r\n]")) {
			line = line.trim();
			if (line.length() == 0)
				continue;
			String regex = "[，,。:：“”？?！!；;។]";;
			if (Config.language.equals(Language.Thai)) {
				regex = "|[ ]";
			}
			for (String sent : StringUtil.sentenceSegment(line, regex)) {
				sent = sent.trim();
				if (sent.isEmpty())
					continue;
				sentences.add(sent);
			}
		}
		return sentences;
	}

	/**
	 * 将句子列表转化为文档
	 *
	 * @param sentenceList
	 * @return
	 */
	private static List<List<String>> convertSentenceListToDocument(
			List<String> sentenceList) {
		List<List<String>> docs = new ArrayList<List<String>>(sentenceList.size());
		for (String sentence : sentenceList) {
			List<Term> termList = defaultSegment.segment(sentence);
			List<String> wordList = new LinkedList<String>();
			for (Term term : termList) {
				if (StopWordDictionary.shouldInclude(term)) {
					wordList.add(term.getWord());
				}
			}
			docs.add(wordList);
		}
		return docs;
	}

	/**
	 * 一句话调用接口
	 *
	 * @param document
	 *            目标文档
	 * @param size
	 *            需要的关键句的个数
	 * @return 关键句列表
	 */
	public static List<String> getTopSentenceList(String document, int size) {
		List<String> sentenceList = spiltSentence(document);
		List<List<String>> docs = convertSentenceListToDocument(sentenceList);
		TextRankSummarization textRank = new TextRankSummarization(docs);
		int[] topSentence = textRank.getTopSentence(size);
		List<String> resultList = new LinkedList<String>();
		for (int i : topSentence) {
			resultList.add(sentenceList.get(i));
		}
		return resultList;
	}

	/**
	 * 一句话调用接口
	 *
	 * @param document
	 *            目标文档
	 * @param max_length
	 *            需要摘要的长度
	 * @return 摘要文本
	 */
	public static String getSummary(String document, int max_length) {
		List<String> sentenceList = spiltSentence(document);

		int sentence_count = sentenceList.size();
		int document_length = document.length();
		int sentence_length_avg = document_length / sentence_count;
		int size = max_length / sentence_length_avg + 1;
		List<List<String>> docs = convertSentenceListToDocument(sentenceList);
		TextRankSummarization textRank = new TextRankSummarization(docs);
		int[] topSentence = textRank.getTopSentence(size);
		List<String> resultList = new LinkedList<String>();
		for (int i : topSentence) {
			resultList.add(sentenceList.get(i));
		}

		resultList = permutation(resultList, sentenceList);
		resultList = pick_sentences(resultList, max_length);
		return StringUtil.join(". ", resultList);
	}

	public static List<String> permutation(List<String> resultList,
			List<String> sentenceList) {
		int index_buffer_x;
		int index_buffer_y;
		String sen_x;
		String sen_y;
		int length = resultList.size();
		// bubble sort derivative
		for (int i = 0; i < length; i++)
			for (int offset = 0; offset < length - i; offset++) {
				sen_x = resultList.get(i);
				sen_y = resultList.get(i + offset);
				index_buffer_x = sentenceList.indexOf(sen_x);
				index_buffer_y = sentenceList.indexOf(sen_y);
				// if the sentence order in sentenceList does not conform that
				// is in resultList, reverse it
				if (index_buffer_x > index_buffer_y) {
					resultList.set(i, sen_y);
					resultList.set(i + offset, sen_x);
				}
			}

		return resultList;
	}

	public static List<String> pick_sentences(List<String> resultList,
			int max_length) {
		int length_counter = 0;
		int length_buffer;
		int length_jump;
		List<String> resultBuffer = new LinkedList<String>();
		for (int i = 0; i < resultList.size(); i++) {
			length_buffer = length_counter + resultList.get(i).length();
			if (length_buffer <= max_length) {
				resultBuffer.add(resultList.get(i));
				length_counter += resultList.get(i).length();
			} else if (i < (resultList.size() - 1)) {
				length_jump = length_counter + resultList.get(i + 1).length();
				if (length_jump <= max_length) {
					resultBuffer.add(resultList.get(i + 1));
					length_counter += resultList.get(i + 1).length();
					i++;
				}
			}
		}
		return resultBuffer;
	}

}
