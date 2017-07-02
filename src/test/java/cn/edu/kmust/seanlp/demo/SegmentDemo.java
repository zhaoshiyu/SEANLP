package cn.edu.kmust.seanlp.demo;

import cn.edu.kmust.seanlp.SEANLP;

/**
 * 分词demo
 * @author Zhao Shiyu
 *
 */
public class SegmentDemo {
	
	public static void main(String[] args) {
		//泰语分词
		String thText = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
		System.out.println(SEANLP.Thai.syllableSegment(thText));
		System.out.println(SEANLP.Thai.dCRFSegment(thText));
		System.out.println(SEANLP.Thai.gCRFSegment(thText));
		System.out.println(SEANLP.Thai.datSegment(thText));
		System.out.println(SEANLP.Thai.maxSegment(thText));
		System.out.println(SEANLP.Thai.minSegment(thText));
		System.out.println(SEANLP.Thai.reMaxSegment(thText));
		System.out.println(SEANLP.Thai.reMinSegment(thText));
		
		//越南语分词
		String viText = "Hệ thống tín dụng - ngân hàng cũng tăng trưởng khá, ngày càng giữ vai trò quan trọng trong cơ cấu kinh tế Thủ đô.";
		System.out.println(SEANLP.Vietnamese.crfSegment(viText));
		System.out.println(SEANLP.Vietnamese.datSegment(viText));
		System.out.println(SEANLP.Vietnamese.maxSegment(viText));
		System.out.println(SEANLP.Vietnamese.minSegment(viText));
		System.out.println(SEANLP.Vietnamese.reMaxSegment(viText));
		System.out.println(SEANLP.Vietnamese.reMinSegment(viText));
		
		//柬埔寨语（高棉语）分词
		String khText = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		System.out.println(SEANLP.Khmer.crfSegment(khText));
		System.out.println(SEANLP.Khmer.datSegment(khText));
		System.out.println(SEANLP.Khmer.maxSegment(khText));
		System.out.println(SEANLP.Khmer.minSegment(khText));
		System.out.println(SEANLP.Khmer.reMaxSegment(khText));
		System.out.println(SEANLP.Khmer.reMinSegment(khText));
		
		//老挝语分词
		String loText = "ທ່ານວິນເຄັນເປັນປະທານບໍລິສັດອຽວເຊີວີເອີແອນ.ວີ.ກຸ່ມບໍລິສັດການພິມຂອງຊາວດັດ.";
		System.out.println(SEANLP.Lao.datSegment(loText));
		System.out.println(SEANLP.Lao.maxSegment(loText));
		System.out.println(SEANLP.Lao.minSegment(loText));
		System.out.println(SEANLP.Lao.reMaxSegment(loText));
		System.out.println(SEANLP.Lao.reMinSegment(loText));
		
		//缅甸语分词
		//String buText = "ကံဆိုးကံဇာတာကံထိုက်ကံနခိုကံနှိုးဆော်";
		//ဝတ္ထုတို/n တစ်/tn ပုဒ်/part ဘယ်လို/adv ရ/v လာ/part တယ်/ppm ဆို/v တာ/part မျိုး/n လေ/part ။/punc
		String buText = "ဝတ္ထုတိုတစ်ပုဒ်ဘယ်လိုရလာတယ်ဆိုတာမျိုးလေ။";
		System.out.println(SEANLP.Burmese.datSegment(buText));
		System.out.println(SEANLP.Burmese.crfSegment(buText));
		System.out.println(SEANLP.Burmese.maxSegment(buText));
		System.out.println(SEANLP.Burmese.minSegment(buText));
		System.out.println(SEANLP.Burmese.reMaxSegment(buText));
		System.out.println(SEANLP.Burmese.reMinSegment(buText));
		System.out.println(SEANLP.Burmese.syllableSegment(buText));
	}

}
