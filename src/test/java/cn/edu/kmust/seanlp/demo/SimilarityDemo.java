package cn.edu.kmust.seanlp.demo;

import cn.edu.kmust.seanlp.SEANLP;

/**
 * 句子相似度计算demo
 * @author Zhao Shiyu
 *
 */
public class SimilarityDemo {
	public static void main(String[] args) {
		String thText = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
		String viText = "Hệ thống tín dụng - ngân hàng cũng tăng trưởng khá, ngày càng giữ vai trò quan trọng trong cơ cấu kinh tế Thủ đô.";
		String khText = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		String loText = "ທ່ານວິນເຄັນເປັນປະທານບໍລິສັດອຽວເຊີວີເອີແອນ.ວີ.ກຸ່ມບໍລິສັດການພິມຂອງຊາວດັດ.";
		String buText = "ကံဆိုးကံဇာတာကံထိုက်ကံနခိုကံနှိုးဆော်";
		System.out.println(SEANLP.Thai.sentenceSimilarity(thText, thText));
		System.out.println(SEANLP.Vietnamese.sentenceSimilarity(viText, viText));
		System.out.println(SEANLP.Khmer.sentenceSimilarity(khText, khText));
		System.out.println(SEANLP.Lao.sentenceSimilarity(loText, loText));
		System.out.println(SEANLP.Burmese.sentenceSimilarity(buText, buText));
	}
}
