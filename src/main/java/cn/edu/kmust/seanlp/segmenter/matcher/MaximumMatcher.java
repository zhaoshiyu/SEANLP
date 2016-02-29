package cn.edu.kmust.seanlp.segmenter.matcher;

import java.util.ArrayList;
import java.util.List;

import cn.edu.kmust.seanlp.collection.trie.DATrie;
import cn.edu.kmust.seanlp.recognition.RecogTool;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.util.StringUtil;

/**
 * 基于词典的正向最大匹配算法
 * Dictionary-based maximum matching algorithm
 * @author Zhao Shiyu
 *
 */
public class MaximumMatcher extends AbstractMatcher {

	public MaximumMatcher(DATrie <?>  dict) {
		super(dict);
	}
	
	@Override
	public List <Term> segment(String[] strs) {
		List < Term >  result  =  new ArrayList < Term > ();;
        //文本长度
        final int textLen = strs.length;
        //从未分词的文本中截取的长度
        //int len =  dictTrie.getMaxLength();
        int len = wordMaxLength;
        //剩下未分词的文本的索引
        int start = 0;
        //只要有词未切分完就一直继续
        while(start < textLen){
            if(len > textLen-start){
                //如果未分词的文本的长度小于截取的长度
                //则缩短截取的长度
                len = textLen-start;
            }
            //用长为len的字符串查词典，并做特殊情况识别
            while(!dict.contains(StringUtil.merge(strs, start, len)) && !RecogTool.recog(StringUtil.merge(strs, start, len))){
                //如果长度为一且在词典中未找到匹配
                //则按长度为一切分
                if(len == 1){
                    break;
                }
                //如果查不到，则长度减一后继续
                len--;
            }
            result.add(new Term(StringUtil.merge(strs, start, len), null));
            //从待分词文本中向后移动索引，滑过已经分词的文本
            start += len;
            //每一次成功切词后都要重置截取长度
            //len =  dictTrie.getMaxLength();
            len = wordMaxLength;
        }
        return result;
    } 
	
//	@Override
//	public List <Term> segment(String sentence) {
//        List < Term >  result  =  new ArrayList < Term > ();;
//        //文本长度
//        //从未分词的文本中截取的长度
//        //int len =  dictTrie.getMaxLength();
//        int len = wordMaxLength;
//        //剩下未分词的文本的索引
//        int start = 0;
//        //只要有词未切分完就一直继续
//        while(start < textLen){
//            if(len > textLen-start){
//                //如果未分词的文本的长度小于截取的长度
//                //则缩短截取的长度
//                len = textLen-start;
//            }
//            //用长为len的字符串查词典，并做特殊情况识别
//            while(!dict.contains(sentence, start, len)  && !RecogTool.recog(new String(sentence.toCharArray(), start, len))){
//                //如果长度为一且在词典中未找到匹配
//                //则按长度为一切分
//                if(len  == 1){
//                    break;
//                }
//                //如果查不到，则长度减一后继续
//                len--;
//            }
//            result.add(new Term(new String(sentence.toCharArray(), start, len), null));
//           
//            //从待分词文本中向后移动索引，滑过已经分词的文本
//            start += len;
//            //每一次成功切词后都要重置截取长度
//            //len =  dictTrie.getMaxLength();
//            len = wordMaxLength;
//        }
//        return result;
//    } 
	
	@Override
	public List <Term> segment(char[] sentence) {
        List < Term >  result  =  new ArrayList < Term > ();;
        //文本长度
        final int textLen = sentence.length;
        //从未分词的文本中截取的长度
        //int len =  dictTrie.getMaxLength();
        int len = wordMaxLength;
        //剩下未分词的文本的索引
        int start = 0;
        //只要有词未切分完就一直继续
        while(start < textLen){
            if(len  >  textLen - start){
                //如果未分词的文本的长度小于截取的长度
                //则缩短截取的长度
                len  =  textLen - start;
            }
            //用长为len的字符串查词典，并做特殊情况识别
            while(!dict.contains(sentence, start, len)  && !RecogTool.recog(new String(sentence, start, len))){
                //如果长度为一且在词典中未找到匹配
                //则按长度为一切分
                if(len  ==  1){
                    break;
                }
                //如果查不到，则长度减一后继续
                len--;
            }
            result.add(new Term(new String(sentence, start, len), null));
           
            //从待分词文本中向后移动索引，滑过已经分词的文本
            start  +=  len;
            //每一次成功切词后都要重置截取长度
            //len =  dictTrie.getMaxLength();
            len  =  wordMaxLength;
        }
        return result;
    } 
	
	@Override
	public List <Term> segment(String sentence) {
		return segment(sentence.toCharArray());
	}
	
}
