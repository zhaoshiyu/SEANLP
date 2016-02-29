package cn.edu.kmust.seanlp.segmenter.domain;

import cn.edu.kmust.seanlp.Config;

/**
 * 词（包括词、词性、长度和词的位置）
 * 
 * @author  Zhao Shiyu
 *
 */
public class Term {
	
	/**
	 * 词
	 */
	private String word;

	/**
	 * 词性
	 */
	private Nature nature;

	/**
	 * 在文本中的位置（需开启分词器的offset选项）
	 */
	private int offset;

	/**
	 * 构造一个词
	 * 
	 * @param word
	 *            词语
	 * @param nature
	 *            词性
	 */
	public Term(String word, Nature nature) {
		this.word = word;
		this.nature = nature;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public Nature getNature() {
		return nature;
	}
	
	public void setNature(Nature nature) {
		this.nature = nature;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 * toString
	 */
	public String toString() {
		if (Config.BaseConf.speechTagging) {
			return word + "/" + nature;
		}
		return word;
	}

	/**
	 * 长度
	 * 
	 * @return
	 */
	public int length() {
		return word.length();
	}
}

