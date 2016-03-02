package cn.edu.kmust.seanlp.collection.trie;

import java.io.Serializable;

/**
 * 
 * @author  Zhao Shiyu
 *
 */
public interface ITrie extends Serializable {
	
	/**
     * 词典中的词的最大长度，即有多少个字符
     * @return 长度
     */
    int getMaxLength();
    
    /**
     * 判断指定的文本是不是一个词
     * @param text 文本
     * @param offset 指定的文本从哪个下标索引开始
     * @param count 指定的文本的长度
     * @return 
     */
    boolean contains(char[] text, int offset, int count);

    /**
     * 判断指定的文本是不是一个词
     * @param text 文本
     * @param offset 指定的文本从哪个下标索引开始
     * @param count 指定的文本的长度
     * @return 
     */
    boolean contains(String text, int offset, int count);

    /**
     * 判断文本是不是一个词
     * @param key 词
     * @return 是否
     */
    boolean contains(String key);

    /**
     * 清空词典中的所有的词
     */
    void clear();

}
