package cn.edu.kmust.seanlp.corpus.orchid97;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.kmust.io.IOUtil;
import cn.edu.kmust.seanlp.segmenter.ThaiSegmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.ThaiCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

public class Orchid97 {
	
	private static ThaiSegmenter segmentThai;
	
	public Orchid97() {
		segmentThai = new ThaiCRFSegmenter();
	}
	
	public static String syllableSegment(String sentence) {
		
		return segmentThai.syllableSegment(sentence);
	}
	
	public static String wordSegment(String sentence) {
		
		List<Term> termListThai = segmentThai.segment(sentence);
		String resultThai = "";
		for (Term term : termListThai) {
			resultThai += term.getWord() + "|";
		}
		return resultThai;
	}
	
	public static void main(String[] args) {
		
		String orchid97Path = "/home/zsy/nlp/thai/corpus/orchid97-thai-poscorpus.txt";
		String encoding = "UTF-8";
		int k = 0;
		int p =0;
		List<String> syllables = new ArrayList<String>(); 
		List<String> syllablesNoT = new ArrayList<String>(); 
		List<String> words = new ArrayList<String>(); 
		List<String> wordsTemp = new ArrayList<String>(); 
		List<String> lines = IOUtil.readLines(orchid97Path, encoding);
		for (String line:lines) {
			if (!line.equals("NULL") && !line.equals("<space>.*") && !line.matches(".*//")) {
				System.out.println("词数：" + ++k);
				String[] ls = line.split("/");
				line = ls[0];
				line = line.replaceAll("<space>", "");
				line = line.replaceAll("[0-9a-zA-Z-]+", "");
				line = line.trim();
				if (!line.isEmpty()) {
					System.err.println("泰语词数：" + ++p);
					if (Collections.frequency(words, line) < 1) {
						words.add(line);
						 System.out.println("词典数：" + words.size());
					 }
					wordsTemp.add(line);
					String[] ss = wordSegment(line).replaceAll("\\|$", "").split("\\|");
					for (String s:ss) {
						s = s.replaceAll("<space>", "");
						s = s.replaceAll("[0-9a-zA-Z-]+", "");
						s = s.trim();
						if (!s.isEmpty()) {
							 if (Collections.frequency(syllables, s) < 1) {
								 syllables.add(s);
								 System.out.println("音节数：" + syllables.size());
							 }
							 
							 String t = s.replaceAll("[\u0E48\u0E49\u0E4A\u0E4B]+", "");
							 if (Collections.frequency(syllablesNoT, t) < 1) {
								 syllablesNoT.add(s);
								 System.out.println("去掉音调音节数：" + syllablesNoT.size());
							 }
						}
						
						 
						 
						
//						if (syllables.size() > 0) {
//							for (int i = 0; i < syllables.size(); i++) {
//								System.out.println(s.equals(syllables.get(i)));
//								if (!s.equals(syllables.get(i))) {
//									syllables.add(s);
//								}
//							}
//						} else {
//							syllables.add(s);
//						}
					}
				}
				
			}
		}
		System.out.println("总音节数：" + syllables.size());
//		System.out.println(syllables.toString());
		System.err.println("去掉音调总音节数：" + syllablesNoT.size());
//		System.out.println(syllablesNoT.toString());
		
		System.out.println("总词数：" + wordsTemp.size());
		System.err.println("总词典数：" + words.size());
		
		Collections.sort(syllables);
		Collections.sort(syllablesNoT);
		IOUtil.appendLines("/home/zsy/nlp/thai/corpus/syllable.txt", syllables);
		IOUtil.appendLines("/home/zsy/nlp/thai/corpus/syllablesNoT.txt", syllablesNoT);
		Collections.sort(words);
		Collections.sort(wordsTemp);
		IOUtil.appendLines("/home/zsy/nlp/thai/corpus/words.txt", words);
		IOUtil.appendLines("/home/zsy/nlp/thai/corpus/wordsTemp.txt", wordsTemp);
		
	}

}
