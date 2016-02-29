package cn.edu.kmust.seanlp.segmenter.matcher;

import cn.edu.kmust.seanlp.SEANLP;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import junit.framework.TestCase;

public class MatcherSegmentTest extends TestCase {
	
	public void testMatchSegment() {
		String thText = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
		String loText = "ທ່ານວິນເຄັນເປັນປະທານບໍລິສັດອຽວເຊີວີເອີແອນ.ວີ.ກຸ່ມບໍລິສັດການພິມຂອງຊາວດັດ.";
		String viText = "Khi nhung trên đầu đã bắtđầu phân nhị , nhánh phân đã có hiện tượng chuẩn bị phân tam ( từ của những người nuôi hươu gọi là yên ngựa ) là lúc cần phải thu hoạch .";
		String khText = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		String buText = "ကံဆိုးကံဇာတာကံထိုက်ကံနခိုကံနှိုးဆော်";
		System.out.println(SEANLP.Thai.maxSegment(thText));
		System.out.println(SEANLP.Thai.minSegment(thText));
		System.out.println(SEANLP.Thai.reMaxSegment(thText));
		System.out.println(SEANLP.Thai.reMinSegment(thText));
		
		System.out.println(SEANLP.Lao.maxSegment(loText));
		System.out.println(SEANLP.Lao.minSegment(loText));
		System.out.println(SEANLP.Lao.reMaxSegment(loText));
		System.out.println(SEANLP.Lao.reMinSegment(loText));
		
		System.out.println(SEANLP.Vietnamese.maxSegment(viText));
		System.out.println(SEANLP.Vietnamese.minSegment(viText));
		System.out.println(SEANLP.Vietnamese.reMaxSegment(viText));
		System.out.println(SEANLP.Vietnamese.reMinSegment(viText));
		
		System.out.println(SEANLP.Khmer.maxSegment(khText));
		System.out.println(SEANLP.Khmer.minSegment(khText));
		System.out.println(SEANLP.Khmer.reMaxSegment(khText));
		System.out.println(SEANLP.Khmer.reMinSegment(khText));
		
		System.out.println(SEANLP.Burmese.maxSegment(buText));
		System.out.println(SEANLP.Burmese.minSegment(buText));
		System.out.println(SEANLP.Burmese.reMaxSegment(buText));
		System.out.println(SEANLP.Burmese.reMinSegment(buText));
		System.out.println(SEANLP.Burmese.syllableSegment(buText));
		System.out.println(buText);
		String line = "";
		for (Term t : SEANLP.Burmese.syllableSegment(buText)) {
			line += t.getWord();
		}
		System.err.println(line);
	}

}
