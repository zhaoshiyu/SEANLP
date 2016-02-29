package cn.edu.kmust.seanlp.corpus.Khmer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.edu.kmust.io.IOUtil;
import cn.edu.kmust.seanlp.corpus.util.Corpus;
import cn.edu.kmust.seanlp.segmenter.CC.KCC;

public class KhmerCorpus {
	
	
	public static void main(String[] args) {
		String readFile = "data/corpus/Khmer/KCorpus.txt";
		List<String> lines = IOUtil.readLines(readFile);
		List<String> sentences = dict2sentence(lines);
//		List<String> natureSet = getNatureSet(sentences);
//		List<String> coreDictionary = makeCoreDictionary(sentences);
//		int[][] transitionMatrix = makeTransitionMatrix(sentences, natureSet);
//		List<String> transitionTable = makeTransitionTable(natureSet, transitionMatrix);
//		IOUtil.overwriteLines("data/corpus/Khmer/CoreDictionary.Khmer.txt", coreDictionary);
//		IOUtil.overwriteLines("data/corpus/Khmer/TransitionMatrix.Khmer.txt", transitionTable);
		
		
		makeCRFData(sentences);
	}
	
public static List<String> dict2sentence(List<String> lines) {
	List<String> sentences = new ArrayList<String>();
	String sentence = "";
	int count = 0;
	for (String line : lines) {
		line = line.trim();
		String[] wn = line.split("\t");
		String word = wn[0].trim();
//		if (word.matches("[។\\pP]+")) {
//			System.err.println(word);
//		}
		
		line = line.replace("\t", "/");
		
		//if (word.equals("។")) {
		if (word.matches("^[!?។]$") || word.matches("[.]{1,1}")) {
			sentence += line + " ";
			sentence = sentence.trim();
			sentences.add(sentence);
			sentence = "";
			count++;
		} else {
			sentence += line + " ";
		}
	}
	System.out.println(count);
	return sentences;
}
	
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
						String word = wn[0].trim();
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
		List<String> naturelist = new ArrayList<String>();
		for (String key : nmap.keySet()) {
			naturelist.add(key);
			System.out.println("nature = " + key + "; number = " + nmap.get(key));
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
		List<String> rets = new ArrayList<String>();
		String line = "";
		int len = naturelist.size();
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
		List<String> rets = new ArrayList<String>();
		for (String key : map.keySet()) {
			rets.add(key + "\t" + map.get(key));
		}
		return rets;
	}
	
	public static void makeCRFData(List<String> sentences) {
		List<String> rets = new ArrayList<String>();
		for (String sentence : sentences) {
			sentence = sentence.trim();
			String[] wordNatures = sentence.split(" ");
			for (String wordNature : wordNatures) {
				String[] wn = wordNature.split("/");
				String word = wn[0].trim();
				String[] kcc = KCC.toKCC(word);
				int len = kcc.length;
				if (len == 0) {
					continue;
				} else if (len == 1) {
					rets.add(kcc[0] + "\t" + "S");
				} else if (len == 2) {
					rets.add(kcc[0] + "\t" + "B");
					rets.add(kcc[1] + "\t" + "E");
				} else {
					for (int i = 0; i < len; i++) {
						if (i == 0) {
							rets.add(kcc[i] + "\t" + "B");
						} else if (i == (len - 1)) {
							rets.add(kcc[i] + "\t" + "E");
						} else {
							rets.add(kcc[i] + "\t" + "M");
						}
					}
				}
			}
			rets.add("");
		}
		for (String ret : rets) {
			System.out.println(ret);
		}
		IOUtil.appendLines("data/corpus/Khmer/Khmer-CRFs-Training-Data.txt", rets);
	}
	
	
	/**
	 * 从句子语料中获取核心词典
	 * @param sentences
	 * @return
	 */
	public static List<String> makeCoreDictionaryByDict() {
		List<String> lines = IOUtil.readLines("data/corpus/Khmer/KCorpus.txt");
		Map<String, String> wnmap = new TreeMap<String, String>();
		for (String line : lines) {
			line = line.trim();
			if (!line.isEmpty()) {
				String[] wn = line.split("\t");
				int len = wn.length;
				if (len == 2) {
					String word = wn[0].trim();
					String nature = wn[1].trim();
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
				} else {
					System.out.println("词条出错：" + len);
				}
			}
		}
//		//排序
//		for (String key : wnmap.keySet()) {
//			String n = wnmap.get(key);
//			String[] value = n.split("\t");
//			int len = value.length;
//			if (len > 2) {
//				String tempNum = ""; 
//				String tempStr = ""; 
//				for (int i = 1; i < len; i = i + 2) {
//					for(int j = 1; j <len -1 - i; j = j + 2){
//						int numj = Integer.parseInt(value[j]);
//						int numjj = Integer.parseInt(value[j + 2]);
//						if(numj < numjj){
//							tempNum = value[j];
//							tempStr = value[j - 1];
//							value[j] = value[j + 2];
//							value[j - 1] = value[j + 1];
//							value[j + 2] = tempNum;
//							value[j + 1] = tempStr;
//						}
//					}
//				}
//				String newNature = "";
//				for (int k = 0; k < value.length; k++) {
//					if (k == 0) {
//						newNature = value[k];
//					} else{
//						newNature += "\t" + value[k];
//					}
//				}
//				System.out.println(newNature);
//				wnmap.put(key, newNature);
//			}
//			
//		}
		return Corpus.mapToList(Corpus.NatureSort(wnmap));
	}
	
	
	
	public static void CoreDictionary() {
		//data/corpus/Khmer/seafreq.txt
		List<String> lines = IOUtil.readLines("data/corpus/Khmer/KCorpus.txt");
		Map<String, String> treemap = new TreeMap<String, String>();
		List<String> rets = new ArrayList<String>();
		for (String line : lines) {
			line = line.trim();
			rets.add(line + "\t" + "1");
			System.out.println(line);
			String[] wn = line.split("\t");
			treemap.put(wn[1].trim(), "");
		}
		System.out.println(rets.size());
		for (String key : treemap.keySet()) {
			System.out.println(key);
		}
//		io.writeLines("data/corpus/Khmer/CoreDictionary.Khmer.txt", rets);
	}
	
	public static void commonDictionary() {
		//data/corpus/Khmer/seafreq.txt
		List<String> lines = IOUtil.readLines("data/corpus/Khmer/seafreq.txt");
		Map<String, Integer> treemap = new TreeMap<String, Integer>();
		for (String line : lines) {
			line = line.trim();
			System.out.println(line);
			String[] words = line.split("\t");
			if (treemap.get(words[0].trim()) == null) {
				treemap.put(words[0].trim(), Integer.parseInt(words[1].trim()));
			}
		}
		
		lines.clear();
		lines = IOUtil.readLines("data/corpus/Khmer/KHOV.txt");
		for (String line : lines) {
			line = line.trim();
			System.out.println(line);
			String[] words = line.split("\t");
			if (treemap.get(words[0].trim()) == null) {
				treemap.put(words[0].trim(), Integer.parseInt(words[1].trim()));
			}
		}
		
		lines.clear();
		lines = IOUtil.readLines("data/corpus/Khmer/KHSV.txt");
		for (String line : lines) {
			line = line.trim();
			System.out.println(line);
			String[] words = line.split("\t");
			if (treemap.get(words[0].trim()) == null) {
				treemap.put(words[0].trim(), Integer.parseInt(words[1].trim()));
			}
		}
		
		lines.clear();
		lines = IOUtil.readLines("data/corpus/Khmer/villages.txt");
		for (String line : lines) {
			line = line.trim();
			System.out.println(line);
			String[] words = line.split("\t");
			if (treemap.get(words[0].trim()) == null) {
				treemap.put(words[0].trim(), Integer.parseInt(words[1].trim()));
			}
		}
		System.out.println(treemap.size());
		List<String> rets = new ArrayList<String>();
		for (String key : treemap.keySet()) {
			String ret = key + "\t" + String.valueOf(treemap.get(key));
			rets.add(ret);
			System.out.println(ret);
		}
		System.out.println(rets.size());
		
		IOUtil.appendLines("data/corpus/Khmer/CommonDictionary.Khmer.txt", rets);
	}

}
