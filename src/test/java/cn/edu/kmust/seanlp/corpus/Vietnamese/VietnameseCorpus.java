package cn.edu.kmust.seanlp.corpus.Vietnamese;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.edu.kmust.io.IOUtil;
import cn.edu.kmust.seanlp.corpus.util.Corpus;
import cn.edu.kmust.seanlp.util.RadicalMap;

/**
 * 
 * @author  Zhao Shiyu
 *
 */
public class VietnameseCorpus {
	
	/**
	 * 从句子语料中获取核心词典
	 * @param sentences
	 * @return
	 */
	public static List<String> makeCoreDictionary(List<String> sentences) {
		Map<String, String> wnmap = new TreeMap<String, String>();
		for (String sentence : sentences) {
			sentence = sentence.trim();
			if (!sentence.isEmpty()) {
				String[] words = sentence.split(" ");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					String[] wn = words[i].split("/");
					if (wn.length == 2) {
						String word = wn[0].trim().replaceAll("_", " ");
						String nature = wn[1].trim().toUpperCase();
						if (wnmap.get(word) == null) {
							wnmap.put(word, nature + "\t" + "1");
						} else {
							String[] nm = wnmap.get(word).split("\t");
							boolean catFlag = false;
							for (String cat : nm) {
								if (nature.equals(cat)) {
									catFlag = true;
								}
							}
							
							if (catFlag) {
								int nlen = nm.length;
								for (int j = 0; j < nlen; j += 2) {
									if (nature.equals(nm[j].trim())) {
										//System.out.println("key = " + word + "; nature = " + wnmap.get(word));
										nm[j + 1] = String.valueOf(Integer.parseInt(nm[j + 1]) + 1);
									}
								}
								String newNature = "";
								for (int k = 0; k < nlen; k++) {
									if (k ==0) {
										newNature += nm[k];
									} else {
										newNature += "\t" + nm[k];
									}
								}
								wnmap.put(word, newNature);
								newNature = "";
							} else {
								wnmap.put(word, wnmap.get(word) + "\t" + nature + "\t" + "1");
							}
						}
					}
				}
			}
		}
		return Corpus.mapToList(Corpus.NatureSort(wnmap));
	}
	
	/**
	 * 获取词性集，并排序
	 * @param sentences
	 * @return
	 */
	public static List<String> getNatureSet(List<String> sentences) {
		Map<String, Integer> nmap = new TreeMap<String, Integer>();
		nmap.put("BEGIN", 1); //句始
		nmap.put("END", 1); //句末
		for (String sentence : sentences) {
			sentence = sentence.trim();
			if (!sentence.isEmpty()) {
				String[] words = sentence.split(" ");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					String[] wn = words[i].split("/");
					if (wn.length == 2) {
						String nature = wn[1].trim().toUpperCase();
						if (nmap.get(nature) == null) {
							nmap.put(nature, 1);
						} else {
							nmap.put(nature, nmap.get(nature) + 1);
						}
					}
				}
			}
		}
		List<String> naturelist = new ArrayList<String>(nmap.size());
		for (Map.Entry<String, Integer> entry : nmap.entrySet()) {
			naturelist.add(entry.getKey());
			System.out.println("nature = " + entry.getKey() + "; number = " + entry.getValue());
		}
		Collections.sort(naturelist);
		return naturelist;
	}
	
	/**
	 * 词性转移矩阵
	 * @param sentences
	 * @param naturelist
	 * @return
	 */
	public static int[][] makeTransitionMatrix(List<String> sentences, List<String> naturelist) {
		Collections.sort(naturelist);
		int[][] transitionMatrix = new int[naturelist.size()][naturelist.size()];
		for (String sentence : sentences) {
			sentence = sentence.trim();
			if (!sentence.isEmpty()) {
				String[] words = sentence.split(" ");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					if (i == 0) {
						String[] wn = words[i].split("/");
						if (wn.length == 2) {
							int beginIndex = naturelist.indexOf("BEGIN");
							int natureIndex = naturelist.indexOf(wn[1].trim().toUpperCase());
							transitionMatrix[beginIndex][natureIndex] = transitionMatrix[beginIndex][natureIndex] + 1;
						}
					} else if (i == (len - 1)) {
						String[] wn = words[i].split("/");
						if (wn.length == 2) {
							int natureIndex = naturelist.indexOf(wn[1].trim().toUpperCase());
							int endIndex = naturelist.indexOf("END");
							transitionMatrix[natureIndex][endIndex] = transitionMatrix[natureIndex][endIndex] + 1;
						}
					} else {
						String[] bword = words[i - 1].split("/");
						String[] word = words[i].split("/");
						if (bword.length == 2 && word.length == 2) {
							int bIndex = naturelist.indexOf(bword[1].trim().toUpperCase());
							int natureIndex = naturelist.indexOf(word[1].trim().toUpperCase());
							transitionMatrix[bIndex][natureIndex] = transitionMatrix[bIndex][natureIndex] + 1;
						}
					}
				}
			}
		}
		return transitionMatrix;
	}
	
	public static List<String> makeTransitionTable(List<String> naturelist, int[][] transitionMatrix) {
		Collections.sort(naturelist);
		int len = naturelist.size();
		List<String> rets = new ArrayList<String>(len);
		String line = "";
		// 表头
		for (int i = 0; i < len; i++) {
			line += "\t" + naturelist.get(i);
		}
		rets.add(line);
		line = "";
		//表内容
		for (int i = 0; i < len; i++) {
			line = naturelist.get(i);
			for (int j = 0; j < len; j++) {
				line  += "\t" + transitionMatrix[i][j];
			}
			rets.add(line);
			line = "";
		}
		return rets;
	}
	
	public static List<String> mapToList(Map<String, String> map) {
		List<String> rets = new ArrayList<String>(map.size());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			rets.add(entry.getKey() + "\t" + entry.getValue());
		}
		return rets;
	}
	
	public static void makeCRFData() {
		String file = "data/corpus/Vietnamese/vi-pos-corpus.txt";
		List<String> rets = new LinkedList<String>();
		List<String> sentences = IOUtil.readLines(file);
		for (String sentence : sentences) {
			sentence = sentence.trim();
			String[] wordNatures = sentence.split(" ");
			for (String wordNature : wordNatures) {
				String[] wn = wordNature.split("/");
				String word = wn[0].trim();
				String[] syllables = word.split("_");
				int len = syllables.length;
				if (len == 0) {
					continue;
				} else if (len == 1) {
					rets.add(syllables[0] + "\t" + RadicalMap.getVietnameseType(syllables[0]) + "\t" + "S");
				} else if (len == 2) {
					rets.add(syllables[0] + "\t" + RadicalMap.getVietnameseType(syllables[0]) + "\t" + "B");
					rets.add(syllables[1] + "\t" + RadicalMap.getVietnameseType(syllables[1]) + "\t" + "E");
				} else {
					for (int i = 0; i < len; i++) {
						if (i == 0) {
							rets.add(syllables[i] + "\t" + RadicalMap.getVietnameseType(syllables[i]) + "\t" + "B");
						} else if (i == (len - 1)) {
							rets.add(syllables[i] + "\t" + RadicalMap.getVietnameseType(syllables[i]) + "\t" + "E");
						} else {
							rets.add(syllables[i] + "\t" + RadicalMap.getVietnameseType(syllables[i]) + "\t" + "M");
						}
					}
				}
			}
			rets.add("");
		}
		for (String ret : rets) {
			System.out.println(ret);
		}
		IOUtil.appendLines("data/corpus/Vietnamese/Vi-CRFs-Training-Data.txt", rets);
	}
	
	public static void main(String[] args) {
		
		//生成CRF训练和测试数据
//		makeCRFData();
		
		//生成核心词典和词性转移矩阵
		String file = "data/corpus/Vietnamese/vi-pos-corpus.txt";
		List<String> sentences = IOUtil.readLines(file);
		List<String> natureSet = getNatureSet(sentences);
		List<String> coreDictionary = makeCoreDictionary(sentences);
		int[][] transitionMatrix = makeTransitionMatrix(sentences, natureSet);
		List<String> transitionTable = makeTransitionTable(natureSet, transitionMatrix);
		IOUtil.overwriteLines("data/corpus/Vietnamese/CoreDictionary.Vietnamese.txt", coreDictionary);
//		IOUtil.writeLines("data/corpus/Vietnamese/TransitionMatrix.Vietnamese.txt", transitionTable);
//		
//		for (String nature : natureSet) {
//			System.out.println(nature);
//		}
	}
	
}
