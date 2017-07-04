package cn.edu.kmust.seanlp.segmenter.crf;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.BurmeseCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import junit.framework.TestCase;

public class BurmeseCRFTest extends TestCase {
	
	public void testBurmeseCRFSegment() {
		Config.BaseConf.enableDebug();
		//ဝတ္ထုတို/n တစ်/tn ပုဒ်/part ဘယ်လို/adv ရ/v လာ/part တယ်/ppm ဆို/v တာ/part မျိုး/n လေ/part ။/punc
		String text = "ဝတ္ထုတိုတစ်ပုဒ်ဘယ်လိုရလာတယ်ဆိုတာမျိုးလေ။";
		Segmenter segmentKhmer = new BurmeseCRFSegmenter();
		List<Term> terms = segmentKhmer.segment(text);
		System.out.println(terms);
	}

}
