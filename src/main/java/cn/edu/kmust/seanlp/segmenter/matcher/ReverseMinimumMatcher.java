package cn.edu.kmust.seanlp.segmenter.matcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.collection.trie.DATrie;
import cn.edu.kmust.seanlp.recognition.RecogTool;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.util.StringUtil;

/**
 * 基于词典的逆向最小匹配算法
 * Dictionary-based reverse minimum matching algorithm
 * @author Zhao Shiyu
 *
 */
public class ReverseMinimumMatcher  extends AbstractMatcher {

	public ReverseMinimumMatcher(DATrie<?> dict) {
		super(dict);
	}

	@Override
	public List<Term> segment(String[] strs) {
		List<Term> result = new LinkedList<Term>();
        //文本长度
        final int textLen=strs.length;
        //从未分词的文本中截取的长度
        int len=wordMinLength;
        //剩下未分词的文本的索引
        int start=textLen-len;
        //只要有词未切分完就一直继续
        while(start>=0){
            //用长为len的字符串查词典
            while(!dict.contains(StringUtil.merge(strs, start, len)) && !RecogTool.recog(StringUtil.merge(strs, start, len))){
                //如果查不到，则长度加一后继续
                //索引向前移动一个字，然后继续
                len++;
                start--;
                //如果长度为词典最大长度且在词典中未找到匹配
                //或已经遍历完剩下的文本且在词典中未找到匹配
                //则按长度为一切分
                if(len>wordMaxLength || start<0){
                    //重置截取长度为一
                    //向后移动start索引
                    start+=len-1;
                    len=wordMinLength;
                    break;
                }
            }
        	 result.add(0, new Term(StringUtil.merge(strs, start, len), null));
            //每一次成功切词后都要重置开始索引位置
            start--;
            //每一次成功切词后都要重置截取长度
            len=wordMinLength;
        }
        return result;
	}

//	@Override
//	public List<Term> segment(String sentence) {
//		List<Term> result = new LinkedList<Term>();
//        //文本长度
//        final int textLen=sentence.length();
//        //从未分词的文本中截取的长度
//        int len=wordMinLength;
//        //剩下未分词的文本的索引
//        int start=textLen-len;
//        //只要有词未切分完就一直继续
//        while(start>=0){
//            //用长为len的字符串查词典
//        	 while(!dict.contains(sentence, start, len)  && !RecogTool.recog(new String(sentence.toCharArray(), start, len))){
//                //如果查不到，则长度加一后继续
//                //索引向前移动一个字，然后继续
//                len++;
//                start--;
//                //如果长度为词典最大长度且在词典中未找到匹配
//                //或已经遍历完剩下的文本且在词典中未找到匹配
//                //则按长度为一切分
//                if(len>wordMaxLength || start<0){
//                    //重置截取长度为一
//                    //向后移动start索引
//                    start+=len-1;
//                    len=wordMinLength;
//                    break;
//                }
//            }
//        	 result.add(0, new Term(new String(sentence.toCharArray(), start, len), null));
//            //每一次成功切词后都要重置开始索引位置
//            start--;
//            //每一次成功切词后都要重置截取长度
//            len=wordMinLength;
//        }
//        return result;
//	}
	
	@Override
	public List<Term> segment(char[] sentence) {
		List<Term> result = new LinkedList<Term>();
        //文本长度
        final int textLen=sentence.length;
        //从未分词的文本中截取的长度
        int len=wordMinLength;
        //剩下未分词的文本的索引
        int start=textLen-len;
        //只要有词未切分完就一直继续
        while(start>=0){
            //用长为len的字符串查词典
        	 while(!dict.contains(sentence, start, len)  && !RecogTool.recog(new String(sentence, start, len))){
                //如果查不到，则长度加一后继续
                //索引向前移动一个字，然后继续
                len++;
                start--;
                //如果长度为词典最大长度且在词典中未找到匹配
                //或已经遍历完剩下的文本且在词典中未找到匹配
                //则按长度为一切分
                if(len>wordMaxLength || start<0){
                    //重置截取长度为一
                    //向后移动start索引
                    start+=len-1;
                    len=wordMinLength;
                    break;
                }
            }
        	 result.add(0, new Term(new String(sentence, start, len), null));
            //每一次成功切词后都要重置开始索引位置
            start--;
            //每一次成功切词后都要重置截取长度
            len=wordMinLength;
        }
        return result;
	}
	
	@Override
	public List<Term> segment(String sentence) {
		return segment(sentence.toCharArray());
	}

}
