package cn.edu.kmust.seanlp.extractor.TFIDF;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Language;
import cn.edu.kmust.seanlp.algoritm.MaxHeap;
import cn.edu.kmust.seanlp.extractor.KeywordExtractor;
import cn.edu.kmust.seanlp.extractor.domain.KeyTerm;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class TFIDFKeyword  extends KeywordExtractor {
	
	/**
	 * 提取多少个关键字
	 */
	int nKeyword = 10;
	
	public static List<KeyTerm> getKeywordList(String document, int size) {
		TFIDFKeyword keyword = new TFIDFKeyword();
		keyword.nKeyword = size;

		return keyword.getKeyword(document);
	}
	
	/**
	 * 提取关键词
	 * 
	 * @param content
	 * @return
	 */
	public List<KeyTerm> getKeyword(String content) {
		Set<Map.Entry<String, Float>> entrySet = getTerm(content, nKeyword).entrySet();
		List<KeyTerm> result = new ArrayList<KeyTerm>(entrySet.size());
		for (Map.Entry<String, Float> entry : entrySet) {
			result.add(new KeyTerm(entry.getKey(), entry.getValue()));
		}
		return result;
	}
	
	/**
	 * 返回分数最高的前size个分词结果和对应的keyword
	 * 
	 * @param content
	 * @param size
	 * @return
	 */
	public Map<String, Float> getTerm(String content, Integer size) {
		Map<String, Float> map = getTerm(content);
		Map<String, Float> result = new LinkedHashMap<String, Float>();
		for (Map.Entry<String, Float> entry : new MaxHeap<Map.Entry<String, Float>>(
				size, new Comparator<Map.Entry<String, Float>>() {
					@Override
					public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				}).addAll(map.entrySet()).toList()) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}
	
	/**
	 * 返回全部分词结果和对应的rank
	 * 
	 * @param content
	 * @return
	 */
	public Map<String, Float> getTerm(String content) {
		assert content != null;
		List<Term> termList = defaultSegment.segment(content);
		if (Config.DEBUG) {
			System.out.println("分词结果：" + termList);
		}
		return getTerm(termList);
	}
	
	public Map<String, Float> getTerm(List<Term> termList) {
		List<String> wordList = new ArrayList<String>(termList.size());
		for (Term term: termList) {
			if (shouldInclude(term) && !term.getWord().trim().isEmpty()) {
				wordList.add(term.getWord());
			}
		}
		return getTF(wordsCount(termList));
		
	}
	
	/**
     * 
    * @Title: TF
    * @Description: 分词结果转化为TF,公式为:TF(w,d) = count(w, d) / size(d)
    * 即词w在文档d中出现次数count(w, d)和文档d中总词数size(d)的比值
    * @param @param segWordsResult
    * @param @return    
    * @return HashMap<String,Double>   
    * @throws
     */
    private static HashMap<String, Float> getTF(Map<String, Integer> WordsCount) { 
 
        HashMap<String, Float> tf = new HashMap<String, Float>();// 正规化  
        if(WordsCount==null || WordsCount.size()==0){
    		return tf;
    	}
        Float size=Float.valueOf(WordsCount.size());
        for(Map.Entry<String, Integer> entry : WordsCount.entrySet()){
        	tf.put(entry.getKey(), Float.valueOf(entry.getValue())/size);
        }
        return tf;
    } 
    
    /**
     * 统计各个词出现的次数
     * @param content
     * @return
     */
    private static Map<String, Integer> wordsCount(List<Term> content){
        Map<String, Integer> words = new HashMap<String, Integer>();
        for (Term term : content) {
        	if (words.get(term.getWord()) != null) {
        		words.put(term.getWord(), words.get(term.getWord()) + 1);
        	} else {
        		words.put(term.getWord(), 1);
        	}
        }
        return words;
    }
    
    public static void main(String[] args) {
    	
    	Config.BaseConf.seletcLanguage(Language.Thai);
    	String thDocument = "ญี่ปุ่นควรระมัดระวังคำพูดและพฤติกรรมเกี่ยวกับปัญหาทะเลจีนใต้ \n"
				+ "สำนักข่าวแห่งประเทศจีนรายงานว่า นายหง เหล่ย โฆษกกระทรวงการต่างประเทศจีนกล่าวเมื่อวันที่ 19 มกราคมว่า ญี่ปุ่นควรจดจำประวัติศาสตร์การรุกรานให้แม่นยำ สำนึกผิดอย่างยิ่ง และระมัดระวังคำพูดและพฤติกรรมเกี่ยวกับปัญหาทะเลจีนใต้ \n"
				+ "นายชินโซ อาเบะ นายกรัฐมนตรีญี่ปุ่นกล่าวเมื่อวันที่ 18 มกราคมว่า ญี่ปุ่นสใส่ใจอย่างยิ่งต่อการที่จีนสร้างเกาะเทียมกลางทะเลจีนใต้ และทดลองบุกเบิกทรัพยากรทั้งน้ำมันและแก๊สธรรมชาติในทะเลจีนตะวันออก เรียกร้องประชาคมโลกแสดงความเห็นเกี่ยวกับเรื่องนี้มากขึ้น \n"
				+ "นายหง เหล่ยกล่าวต่อการนี้ว่า การบุกเบิกน้ำมันและแก๊สธรรมชาติของจีน ล้วนกระทำในน่านน้ำทะเลที่อยู่ภายใต้การควบคุมของจีนเองโดยปราศจากข้อกังขา ทุกสิ่งทุกอย่างอยู่ในกรอบอธิปไตยของจีนเอง อนึ่ง จีนครองอธิปไตยเหนือหมู่เกาะหนานซาและน่านน้ำทะเลโดยรอบอย่างมิอาจโต้แย้งได้";
		
		System.out.println(getKeywordList(thDocument, 6));
		
		List<KeyTerm> dd = getKeywordList(thDocument, 6);
		System.out.println(dd.get(1).getWord().trim().isEmpty());
	}

}
