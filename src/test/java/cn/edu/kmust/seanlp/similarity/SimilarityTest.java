package cn.edu.kmust.seanlp.similarity;

import cn.edu.kmust.seanlp.SEANLP;
import junit.framework.TestCase;

public class SimilarityTest extends TestCase {
	
	public void testThSimilarity() {
		String sent = "ญี่ปุ่นควรระมัดระวังคำพูดและพฤติกรรมเกี่ยวกับปัญหาทะเลจีนใต้";
		System.out.println(SEANLP.Thai.sentenceSimilarity(sent, sent));
	}
	
	public void testViSimilarity() {
		String sent = "Đại hội lần thứ XII của Đảng họp phiên trù bị";
		System.out.println(SEANLP.Vietnamese.sentenceSimilarity(sent, sent));
	}
	
	public void testKmSimilarity() {
		String sent = "កម្មវិធី ​អប់រំ​បច្ចេកទេស​ដែល​កំពុង​អនុវត្ត​នាពេលបច្ចុប្បន្ន";
		System.out.println(SEANLP.Khmer.sentenceSimilarity(sent, ""));
	}
	
	public void testLoSimilarity() {
		String sent = "ປະທານ​ປະ​ເທດ​ຈີນ​ເລີ່​ມຢ້ຽມຢາມ​ຊາ​ອຸ​ດິດອາຣັບບີ";
		System.out.println(SEANLP.Lao.sentenceSimilarity(sent, sent));
	}
	
	public void testBuSimilarity() {
		String sent = "ဂ်ာကာတာ-ဘန္ေဒါင္း ျမန္နွုုန္းျမင္႔ရထားလမ္း ေဖါက္လုပ္မည္";
		System.out.println(SEANLP.Burmese.sentenceSimilarity(sent, sent));
	}

}
