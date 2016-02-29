package cn.edu.kmust.seanlp.segmenter.matcher;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 词典匹配算法接口
 * 
 * @author Zhao Shiyu
 *
 */
public interface Matcher {
	
	public List<Term> segment(String[] strs);
	
	public List<Term> segment(String sentence);
	
	public List<Term> segment(char[] sentence);

}
