package cn.edu.kmust.seanlp.segmenter.crf;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.KhmerCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

public class KhmerCRFTest {
	
	public void testKhmerCRFSegment() {
		Config.BaseConf.enableDebug();
		String text = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		Segmenter segmentKhmer = new KhmerCRFSegmenter();
		List<Term> terms = segmentKhmer.segment(text);
		System.out.println(terms);
	}
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		String text = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		Segmenter segmentKhmer = new KhmerCRFSegmenter();
		List<Term> terms = segmentKhmer.segment(text);
		System.out.println(terms);
	}

}
