package cn.edu.kmust.seanlp.algoritm;

import java.util.*;

import cn.edu.kmust.seanlp.dictionary.NatureTransitionMatrix;
import cn.edu.kmust.seanlp.segmenter.domain.Nature;
import cn.edu.kmust.seanlp.segmenter.domain.Vertex;

/**
 * 维特比算法
 * 
 */
public class Viterbi {
	/**
	 * 求解HMM模型，所有概率请提前取对数
	 * 
	 * @param obs
	 *            观测序列
	 * @param states
	 *            隐状态
	 * @param start_p
	 *            初始概率（隐状态）
	 * @param trans_p
	 *            转移概率（隐状态）
	 * @param emit_p
	 *            发射概率 （隐状态表现为显状态的概率）
	 * @return 最可能的序列
	 */
	public static int[] compute(int[] obs, int[] states, double[] start_p,
			double[][] trans_p, double[][] emit_p) {
		int _max_states_value = 0;
		for (int s : states) {
			_max_states_value = Math.max(_max_states_value, s);
		}
		++_max_states_value;
		double[][] V = new double[obs.length][_max_states_value];
		int[][] path = new int[_max_states_value][obs.length];

		for (int y : states) {
			V[0][y] = start_p[y] + emit_p[y][obs[0]];
			path[y][0] = y;
		}

		for (int t = 1; t < obs.length; ++t) {
			int[][] newpath = new int[_max_states_value][obs.length];

			for (int y : states) {
				double prob = Double.MAX_VALUE;
				int state;
				for (int y0 : states) {
					double nprob = V[t - 1][y0] + trans_p[y0][y]
							+ emit_p[y][obs[t]];
					if (nprob < prob) {
						prob = nprob;
						state = y0;
						// 记录最大概率
						V[t][y] = prob;
						// 记录路径
						System.arraycopy(path[state], 0, newpath[y], 0, t);
						newpath[y][t] = y;
					}
				}
			}

			path = newpath;
		}

		double prob = Double.MAX_VALUE;
		int state = 0;
		for (int y : states) {
			if (V[obs.length - 1][y] < prob) {
				prob = V[obs.length - 1][y];
				state = y;
			}
		}

		return path[state];
	}

	/**
	 * 特化版的求解HMM模型
	 * 
	 * @param vertexList
	 *            包含Vertex.B节点的路径
	 * @param natureTransitionMatrix
	 */
	public static void compute(List<Vertex> vertexList,
			NatureTransitionMatrix<Nature> natureTransitionMatrix) {
		int length = vertexList.size() - 1;
		double[][] cost = new double[length][];
		Iterator<Vertex> iterator = vertexList.iterator();
		Vertex start = iterator.next();
		Nature pre = start.guessNature();
		// 第一个是确定的
		double total = 0.0;
		for (int i = 0; i < cost.length; ++i) {
			Vertex item = iterator.next();
			cost[i] = new double[item.attribute.nature.length];
			for (int j = 0; j < cost[i].length; ++j) {
				Nature cur = item.attribute.nature[j];
				cost[i][j] = total + natureTransitionMatrix.transititon_probability[pre.ordinal()][cur.ordinal()] - Math.log((item.attribute.frequency[j] + 1e-8) / natureTransitionMatrix.getTotalFrequency(cur));
			}
			double perfect_cost_line = Double.MAX_VALUE;
			int perfect_j = 0;
			for (int j = 0; j < cost[i].length; ++j) {
				if (perfect_cost_line > cost[i][j]) {
					perfect_cost_line = cost[i][j];
					perfect_j = j;
				}
			}
			total = perfect_cost_line;
			pre = item.attribute.nature[perfect_j];
			item.confirmNature(pre);
		}
	}
}
