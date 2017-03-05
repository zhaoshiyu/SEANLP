package cn.edu.kmust.seanlp.corpus.Thai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.midi.Synthesizer;

import cn.edu.kmust.io.IOUtil;
import cn.edu.kmust.seanlp.corpus.util.Corpus;

/**
 * 
 * @author  Zhao Shiyu
 *
 */
public class Orchid97 {
	
	/**
	 * 读取文件，获得语料中的所有句子
	 * @param path
	 * @param io
	 * @return
	 */
	public static List<String> getSentence(String path) {
		List<String> sentences = new ArrayList<String>();
		List<String> lines = IOUtil.readLines(path);
		int sIdxs = 0;
		boolean sentenceFlag = false;
		String sentence = "";
		for (String line : lines) {
			//查找句子编号
			if (line.matches("^#\\d+$")) {
				++sIdxs;
				sentence = sentence.trim();
				if (!sentence.isEmpty()) {
					sentences.add(sentence);
				}
				sentence = "";
				sentenceFlag = true;
				System.out.printf("第 %d 个句子： %s \n", sIdxs, line);
			}
			//查找句子分词结果
			if (line.matches("^[^%#].+[^\\/\\\\]$")) {
				if (sentenceFlag) {
					sentence += line;
					sentenceFlag = false;
				} else {
					sentence += "###" + line;
				}
			}
		}
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
				String[] words = sentence.split("###");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					String[] wn = words[i].split("/");
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
				String[] words = sentence.split("###");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					String[] wn = words[i].split("/");
					String nature = wn[1].trim();
					if (nmap.get(nature) == null) {
						nmap.put(nature, 1);
					} else {
						nmap.put(nature, nmap.get(nature) + 1);
					}
				}
			}
		}
		List<String> naturelist = new ArrayList<String>();
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
				String[] words = sentence.split("###");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					if (i == 0) {
						int beginIndex = naturelist.indexOf("BEGIN");
						String[] wn = words[i].split("/");
						int natureIndex = naturelist.indexOf(wn[1].trim());
						transitionMatrix[beginIndex][natureIndex] = transitionMatrix[beginIndex][natureIndex] + 1;
					} else if (i == (len - 1)) {
						String[] wn = words[i].split("/");
						int natureIndex = naturelist.indexOf(wn[1].trim());
						int endIndex = naturelist.indexOf("END");
						transitionMatrix[natureIndex][endIndex] = transitionMatrix[natureIndex][endIndex] + 1;
					} else {
						String[] bword = words[i - 1].split("/");
						String[] word = words[i].split("/");
						int bIndex = naturelist.indexOf(bword[1].trim());
						int natureIndex = naturelist.indexOf(word[1].trim());
						transitionMatrix[bIndex][natureIndex] = transitionMatrix[bIndex][natureIndex] + 1;
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
		for (Map.Entry<String, String> entry : map.entrySet()) {
			rets.add(entry.getKey() + "\t" + entry.getValue());
		}
		return rets;
	}
	
	public static void main(String[] args) {
		/*
		String orchidFile = "data/corpus/Thai/orchid97-me/orchid97.crp.utf.txt";
		List<String> sentences = getSentence(orchidFile);
		List<String> natureSet = getNatureSet(sentences);
		List<String> coreDictionary = makeCoreDictionary(sentences);
		int[][] transitionMatrix = makeTransitionMatrix(sentences, natureSet);
		List<String> transitionTable = makeTransitionTable(natureSet, transitionMatrix);
		IOUtil.overwriteLines("data/corpus/Thai/orchid97-me/CoreDictionary.Thai.txt", coreDictionary);
		IOUtil.overwriteLines("data/dictionary/tmp/orchid97.TransitionMatrix.txt", transitionTable);
		*/
		
		List<String> lines = IOUtil.readLines("data/corpus/Thai/orchid97-me/CoreDictionary.Thai.txt");
		int count = 0;
		for (String line : lines) {
			String[] wn = line.split("\t");
			if (wn[0].trim().matches("[ก-๛ ]+")) {
				++count;
				System.out.println(line);
			}
		}
		System.out.println(count);
	}
	
//	public static void main(String[] args) {
//		String orchidFile = "data/corpus/orchid97/orchid97.crp.utf.txt";
//		List<String> lines = IOUtil.readFileByLine(orchidFile);
//		//查找句子编号
//        //sIdxs = regexp(line, '^#\d+$', 'ONCE');
//		
//		
//		//查找句子分词结果
//        //sIdxs = regexp(line, '^[^%#].+[^\/\\]$', 'ONCE');
//
//		int sIdxs = 0;
//		boolean sentenceFlag = false;
//		String sentence = "";
//		Map<String, String> nmap = new TreeMap<String, String>();
//		nmap.put("BEGIN", "1");
//		nmap.put("END", "1");
//		Map<String, String> wnmap = new TreeMap<String, String>();
//		for (String line : lines) {
//			//查找句子分词结果
//			if (line.matches("^#\\d+$")) {
//				++sIdxs;
//				sentence = sentence.trim();
//				if (!sentence.isEmpty()) {
//					
//					String[] words = sentence.split("###");
//					int len = words.length;
//					for (int i = 0; i < len; i++) {
//						String[] wn = words[i].split("/");
//						String word = wn[0].trim();
//						String nature = wn[1].trim();
//						nmap.put(nature, "1");
//						if (wnmap.get(word) == null) {
//							wnmap.put(word, nature + "\t" + "1");
//						} else {
//							String[] nm = wnmap.get(word).split("\t");
//							boolean catFlag = false;
//							for (String cat : nm) {
//								if (nature.equals(cat)) {
//									catFlag = true;
//								}
//							}
//							
//							if (catFlag) {
//								int nlen = nm.length;
//								for (int j = 0; j < nlen; j += 2) {
//									if (nature.equals(nm[j].trim())) {
//										//System.out.println("key = " + word + "; nature = " + wnmap.get(word));
//										nm[j + 1] = String.valueOf(Integer.parseInt(nm[j + 1]) + 1);
//									}
//								}
//								String newNature = "";
//								for (int k = 0; k < nlen; k++) {
//									if (k ==0) {
//										newNature += nm[k];
//									} else {
//										newNature += "\t" + nm[k];
//									}
//								}
//								wnmap.put(word, newNature);
//								newNature = "";
//							} else {
//								wnmap.put(word, wnmap.get(word) + "\t" + nature + "\t" + "1");
//							}
//						}
//					}
//					
//					IOUtil.writeLine("data/corpus/orchid97/orchid97-sentence.txt", sentence);
//					//System.out.println(sentence);
//				}
//				sentence = "";
//				sentenceFlag = true;
//				System.out.printf("第 %d 个句子： %s \n", sIdxs, line);
//			}
//			
//			//查找句子分词结果
//	        //sIdxs = regexp(line, '^[^%#].+[^\/\\]$', 'ONCE');
//			if (line.matches("^[^%#].+[^\\/\\\\]$")) {
//				if (sentenceFlag) {
//					sentence += line;
//					sentenceFlag = false;
//				} else {
//					sentence += "###" + line;
//				}
//				
//			}
//
//			
//		}
//		
//		
////		List<String> words = mapToList(wnmap);
//////		IOUtil.writeLines("data/corpus/orchid97/orchid97.CoreDictionary.txt", words);
////		for (String word : words) {
////			System.out.println(word);
////		}
//		
//		//词性转移矩阵
//		List<String> naturelist = new ArrayList<String>();
//		for (String key : nmap.keySet()) {
//			naturelist.add(key);
//			System.out.println(key);
//		}
//		Collections.sort(naturelist);
//		System.out.println(naturelist);
//		System.out.println(naturelist.size());
//		
//	}
	
	
	
		

}
