package cn.edu.kmust.seanlp.extractor.domain;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class KeyTerm {
	
	/**
	 * 词
	 */
	private String word;
	
	/**
	 * 权重
	 */
	private double score;

	/**
	 * 构造一个关键词
	 * @param word
	 * @param weight
	 */
	public KeyTerm(String word, double score) {
		this.word = word;
		this.score = score;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	/**
	 * toString
	 */
	public String toString() {
		return word + "\t:\t" + score;
	}
	

}
