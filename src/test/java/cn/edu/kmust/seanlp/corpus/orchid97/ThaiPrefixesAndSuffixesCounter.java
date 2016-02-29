package cn.edu.kmust.seanlp.corpus.orchid97;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.kmust.io.IOUtil;

public class ThaiPrefixesAndSuffixesCounter {
	
	private static int subsetCounter(String complete, String subset) {
		// TODO Auto-generated method stub
		int counter = 0;
		for (int i = 0; i <= complete.length() - subset.length(); i++) {
			if (complete.substring(i, i + subset.length()).equalsIgnoreCase(subset)) {
				counter++;
			}
		}
		return counter;
	}
	
	public static void main(String[] args) {
		String encoding = "UTF-8";
		String preAndSufFile = "corpus/ThaiPrefixesAndSuffixes.txt";
		String orchid97Corpus = "corpus/orchid97/orchid97.crp.utf.txt";
		IOUtil io = new IOUtil();
		List<String> preAndSufsTemp = io.readLines(preAndSufFile, encoding);
		List<String> lines = io.readLines(orchid97Corpus, encoding);
		List<String> preAndSufs = new ArrayList<String>();
		List<String> includePFWords = new ArrayList<String>();
		List<String> pfs = new ArrayList<String>();
		for (String p:preAndSufsTemp) {
			if (!preAndSufs.contains(p)) {
				preAndSufs.add(p);
			}
		}
		Collections.sort(preAndSufs);
		System.err.println("词根词缀数：" + preAndSufs.size());
		int wordIncludePFSCounter = 0;
		int wordTotal = 0;
		for (String line:lines) {
			if (!line.isEmpty() && line.matches("[ก-๛0-9a-zA-Z<>]+/[A-Z@]+")) {
				String[] wordInf = line.split("/");
				if (wordInf.length == 2) {
					wordTotal++;
					int pfCountForword = 0;
					int wordIncludePFS = 0;
					String word = wordInf[0];
					for (String pf:preAndSufs) {
						int count = subsetCounter(word, pf);
						pfCountForword += count;
						if (count > 0) {
							wordIncludePFS++;
							pfs.add(pf);
//							System.out.println("词：" + word + "中存在：" + count + "个词根词缀：" + pf);
						}
					}
					System.out.println("词：" + word + "中存在：" + wordIncludePFS + "种词根词缀" + pfs.toString() +"，词根词缀数为：" + pfCountForword);
					pfs.clear();
					if (wordIncludePFS > 0) {
						includePFWords.add(word);
						wordIncludePFSCounter ++ ;
					}
				}
			}
		}
		System.out.println("总词数：" + wordTotal + "共：" + wordIncludePFSCounter + "个词包含词根词缀。");
		List<String> includePFWordsTemp = new ArrayList<String>();
		for (String word:includePFWords) {
			if (!includePFWordsTemp.contains(word)) {
				includePFWordsTemp.add(word);
			}
		}
		Collections.sort(includePFWordsTemp);
		System.out.println("词典中有：" + includePFWordsTemp.size());
	}

	

}
