package cn.edu.kmust.seanlp.segmenter.crf;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.segmenter.ThaiSegmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.ThaiCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import junit.framework.TestCase;

public class ThaigCRFTest extends TestCase {
	
	public void testThaiCRFSegmentation() {
//		Config.BaseConf.enableDebug();
		ThaiSegmenter segmentThai = new ThaiCRFSegmenter();
		String textThai = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
		
		List<Term> termListThai = segmentThai.gCRFWordSegment(textThai);
		String resultThai = "";
		for (Term term : termListThai) {
			resultThai += term.getWord() + "|";
		}
		System.out.println(termListThai);
		System.out.println("分词结果：" + resultThai);
		
		int segWords = textThai.length();
		int pressure = 10000;		
		long start = System.currentTimeMillis();
		for (int i = 0; i < pressure; ++i) {
			segmentThai.gCRFWordSegment(textThai);
		}
		double costTime = (System.currentTimeMillis() - start) / (double)1000;
		System.out.printf("分词速度：%.2f 字符每秒", segWords * pressure / costTime);
	}
	

}
