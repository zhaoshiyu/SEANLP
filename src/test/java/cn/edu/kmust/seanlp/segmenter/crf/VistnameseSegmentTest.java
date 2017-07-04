package cn.edu.kmust.seanlp.segmenter.crf;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.VietnameseCRFSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import junit.framework.TestCase;

public class VistnameseSegmentTest extends TestCase {
	
	public void testVistnameseWordSegment() {
		Config.BaseConf.enableDebug();
		String text = "Hệ thống tín dụng - ngân hàng cũng tăng trưởng khá, ngày càng giữ vai trò quan trọng trong cơ cấu kinh tế Thủ đô.";
		Segmenter crfVietnameseSegment = new VietnameseCRFSegmenter();
		List<Term> ret = crfVietnameseSegment.segment(text);
		System.out.println(ret);
	}

}
