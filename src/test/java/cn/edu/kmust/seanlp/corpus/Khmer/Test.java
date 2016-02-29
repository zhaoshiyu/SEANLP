package cn.edu.kmust.seanlp.corpus.Khmer;

import java.util.MissingFormatArgumentException;

public class Test {
	
	public static void main(String[] args) {
		String word = ".";
		if (word.matches("^[!?។]$") || word.matches("[.]{1,1}")) {
			System.out.println(1);
		}
		String[] w = "df|sdf|dfas|".split("\\|");
		System.out.println(w[0]);
	}

}
