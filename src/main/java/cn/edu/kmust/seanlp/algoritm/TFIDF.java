package cn.edu.kmust.seanlp.algoritm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.kmust.seanlp.segmenter.domain.Term;
 
/**
 * TF-IDF算法实现
 * 
 * @author Zhao Shiyu
 *
 */
public class TFIDF {
	/**
	 * 所有文档TF结果.key:文档名,value:该文档TF
	 */
	private static Map<String, Map<String, Double>> allTfMap  =  new HashMap<String, Map<String, Double>>();  
 
	/**
	 * 所有文档分词结果.key:文档名,value:该文档分词统计
	 */
    private static Map<String, Map<String, Integer>> allDocsMap  =  new HashMap<String, Map<String, Integer>>(); 
 
    /**
	 * 所有文档分词的IDF结果.key:文档名,value:词w在整个文档集合中的逆向文档频率IDF (Inverse Document Frequency)，即文档总数n与词w所出现文档数docs(w, D)比值的对数
	 */
    private static Map<String, Double> idfMap  =  new HashMap<String, Double>();  
 
    /**
     * 统计包含单词的文档数  key:单词  value:包含该词的文档数
     */
    private static Map<String, Integer> containWordOfAllDocNumberMap = new HashMap<String, Integer>();
 
    /**
     * 统计单词的TF-IDF
     * key:文档名 value:该文档TF-IDF
     */
    private static Map<String, Map<String, Double>> tfIdfMap  =  new HashMap<String, Map<String, Double>>();  
 
 
    /**
     * 统计各个词出现的次数
     * @param content
     * @return
     */
    private static Map<String, Integer> countWords(List<Term> content){
        Map<String, Integer> words  =  new HashMap<String, Integer>();
        for (Term term : content) {
        	if (words.get(term.getWord()) !=  null) {
        		words.put(term.getWord(), words.get(term.getWord()) + 1);
        	} else {
        		words.put(term.getWord(), 1);
        	}
        }
        return words;
    }
 
    public static Map<String, Integer> getMostFrequentWords(int num,Map<String, Integer> words){
 
        Map<String, Integer> keywords  =  new LinkedHashMap<String, Integer>();
        int count = 0;
        // 词频统计
        List<Map.Entry<String, Integer>> info  =  new ArrayList<Map.Entry<String, Integer>>(words.entrySet());
        Collections.sort(info, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
 
        // 高频词输出
        for (int j  =  0; j < info.size(); j++) {
            // 词-->频
            if(info.get(j).getKey().length()>1){
                if(num>count){
                    keywords.put(info.get(j).getKey(), info.get(j).getValue());
                    count++;
                }else{
                    break;
                }
            }
        }
        return keywords;
    }
 
    /**
     * 分词结果转化为tf,公式为:tf(w,d)  =  count(w, d) / size(d)
    * 即词w在文档d中出现次数count(w, d)和文档d中总词数size(d)的比值
     * @param segWordsResult
     * @return
     */
    private static HashMap<String, Double> tf(Map<String, Integer> segWordsResult) { 
 
        HashMap<String, Double> tf  =  new HashMap<String, Double>();// 正规化  
        if(segWordsResult   ==   null || segWordsResult.size()   ==   0){
    		return tf;
    	}
        Double size  =  Double.valueOf(segWordsResult.size());
        for(Entry<String, Integer> entry : segWordsResult.entrySet()){
        	tf.put(entry.getKey(), (double)entry.getValue()/size);
        }
        return tf;  
    }  
    
    /**
     * 
     * @param docs
     * @return
     */
    public static Map<String, Map<String, Double>> allTF(Map<String, List<Term>> docs){
    	for(Map.Entry<String, List<Term>> entry : docs.entrySet()){
			Map<String, Integer> segs  =  countWords(entry.getValue());
			allDocsMap.put(entry.getKey(), segs);
			allTfMap.put(entry.getKey(), tf(segs));
		}
    	return allTfMap;
    }
    
    /**
     * 词统计
     * @param docs
     * @return
     */
    public static Map<String, Map<String, Integer>> wordCount(Map<String, List<Term>> docs){
    	for(Map.Entry<String, List<Term>> entry : docs.entrySet()){
			Map<String, Integer> segs  =  countWords(entry.getValue());
			allDocsMap.put(entry.getKey(), segs);
		}
    	return allDocsMap;
    }
 
 
    /**
     * 统计包含单词的文档数  key:单词  value:包含该词的文档数
     * @param allSegsMap
     * @return
     */
    private static Map<String, Integer> containWordOfAllDocNumber(Map<String, Map<String, Integer>> allSegsMap){
    	if(allSegsMap  ==  null || allSegsMap.size()  ==  0){
    		return containWordOfAllDocNumberMap;
    	}
    	for(Map.Entry<String, Map<String, Integer>> entry : allSegsMap.entrySet()){
    		Map<String, Integer> fileSegs  =  entry.getValue();
    		//获取该文档分词为空或为0,进行下一个文档
    		if(fileSegs   ==   null || fileSegs.size()   ==  0){
    			continue;
    		}
    		//统计每个分词的idf
    		Set<String> segs = fileSegs.keySet();
    		for(String seg : segs){
    			if (containWordOfAllDocNumberMap.containsKey(seg)) {
    				containWordOfAllDocNumberMap.put(seg, containWordOfAllDocNumberMap.get(seg) + 1);
                } else {
                	containWordOfAllDocNumberMap.put(seg, 1);
                }
    		}
 
    	}
    	return containWordOfAllDocNumberMap;
    }
 
    /**
     * idf  =  log(n / docs(w, D)) 
     * @param allSegsMap
     * @return
     */
    public static Map<String, Double> idf(Map<String, Map<String, Integer>> allSegsMap){
    	if(allSegsMap  ==  null || allSegsMap.size()  ==  0){
    		return idfMap;
    	}
    	containWordOfAllDocNumberMap = containWordOfAllDocNumber(allSegsMap);
    	Set<String> words = containWordOfAllDocNumberMap.keySet();
    	Double wordSize = Double.valueOf(containWordOfAllDocNumberMap.size());
    	for(String word: words){
    		Double number = Double.valueOf(containWordOfAllDocNumberMap.get(word));
    		idfMap.put(word, Math.log(wordSize/(number+1.0d)));
    	}
    	return idfMap;
    }
 
    /**
     * TF-IDF
     * @param allTfMap
     * @param idf
     * @return
     */
    public static Map<String, Map<String, Double>> tfIdf(Map<String, Map<String, Double>> allTfMap,Map<String, Double> idf){
 
    	Set<String> docsList = allTfMap.keySet();
     	for(String doc : docsList){
    		Map<String, Double> tfMap = allTfMap.get(doc);
    		Map<String, Double> docTfIdf = new HashMap<String,Double>();
    		Set<String> words = tfMap.keySet();
    		for(String word: words){
    			Double tfValue = Double.valueOf(tfMap.get(word));
        		Double idfValue = idf.get(word);
        		docTfIdf.put(word, tfValue*idfValue);
    		}
    		tfIdfMap.put(doc, docTfIdf);
    	}
    	return tfIdfMap;
    }
    
  public static void main(String[] args){
	System.out.println("TF--------------------------------------");
	Set<String> fileList = allTfMap.keySet();
 	for(String filePath : fileList){
		Map<String, Double> tfMap = allTfMap.get(filePath);
		List<Map.Entry<String,Double>> tfList  =  new ArrayList<Map.Entry<String,Double>>(tfMap.entrySet());
       //然后通过比较器来实现排序
       Collections.sort(tfList,new Comparator<Map.Entry<String,Double>>() {
           //降序排序
    	   @Override
           public int compare(Entry<String, Double> o1,
                   Entry<String, Double> o2) {
               return o2.getValue().compareTo(o1.getValue());
           }
           
       });
       for(Map.Entry<String,Double> mapping:tfList){ 
           System.out.println("fileName:"+filePath + mapping.getKey()+":"+mapping.getValue()); 
      } 
	}

 	System.out.println("IDF--------------------------------------");
	Map<String, Double> idfMap = TFIDF.idf(allDocsMap);
	List<Map.Entry<String,Double>> idfList  =  new ArrayList<Map.Entry<String,Double>>(idfMap.entrySet());
   //然后通过比较器来实现排序
   Collections.sort(idfList,new Comparator<Map.Entry<String,Double>>() {
	 //降序排序
	   @Override
       public int compare(Entry<String, Double> o1,
               Entry<String, Double> o2) {
           return o2.getValue().compareTo(o1.getValue());
       }
   });
   for(Map.Entry<String,Double> mapping:idfList){ 
       System.out.println(mapping.getKey()+":"+mapping.getValue()); 
  } 

 	System.out.println("TF-IDF--------------------------------------");
 	Map<String, Map<String, Double>> tfIdfMap = TFIDF.tfIdf(allTfMap, idfMap);
 	Set<String> files = tfIdfMap.keySet();
 	for(String filePath : files){
 		Map<String, Double> tfIdf = tfIdfMap.get(filePath);
 		List<Map.Entry<String,Double>> tfidfList  =  new ArrayList<Map.Entry<String,Double>>(tfIdf.entrySet());
       //然后通过比较器来实现排序
       Collections.sort(tfidfList,new Comparator<Map.Entry<String,Double>>() {
    	 //降序排序
    	   @Override
           public int compare(Entry<String, Double> o1,
                   Entry<String, Double> o2) {
               return o2.getValue().compareTo(o1.getValue());
           }
       });
       for(Map.Entry<String,Double> mapping:tfidfList){ 
           System.out.println("fileName:"+filePath + mapping.getKey()+":"+mapping.getValue()); 
      } 
 	}
}
}
