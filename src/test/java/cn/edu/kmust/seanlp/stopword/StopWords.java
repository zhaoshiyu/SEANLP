package cn.edu.kmust.seanlp.stopword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.edu.kmust.io.IOUtil;

public class StopWords {
	
	public static void main(String[] args) {
		String file1 = "data/corpus/stopwords/stopwords.Thai.txt";
		String file2 = "data/corpus/stopwords/stopword-thai.txt";
		List<String> lines = IOUtil.readLines(file1);
		Map<String, String> map = new TreeMap<String, String>();
		for (String line : lines) {
			line = line.trim();
			map.put(line, "");
		}
		
		lines.clear();
		lines = IOUtil.readLines(file2);
		for (String line : lines) {
			line = line.trim();
			map.put(line, "");
		}
		
		List<String> rets = new ArrayList<String>(map.size());
		for (String key : map.keySet()) {
			rets.add(key);
//			System.out.println(key);
		}
		
//		io.writeLines("data/corpus/stopwords/stopwords.txt", rets);
		
		lines.clear();
		map.clear();
		rets.clear();
		lines = IOUtil.readLines("data/dictionary/stopwords.Chinese.txt");
		for (String line : lines) {
			map.put(line, "");
		}
		for (String key : map.keySet()) {
			rets.add(key);
			System.out.println(key);
		}
		IOUtil.appendLines("data/corpus/stopwords/stopwords-chinese.txt", rets);
	}

}
