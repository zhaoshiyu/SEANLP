package cn.edu.kmust.seanlp.segmenter.dictionary;

import java.util.List;

import junit.framework.TestCase;
import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.SEANLP;
import cn.edu.kmust.seanlp.segmenter.Segmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.BurmeseDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.KhmerDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.LaoDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.ThaiDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.DAT.VietnameseDoubleArrayTrieSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

public class TestDictSegment extends TestCase {
	
	public void testThaiDictSegment() {
		Config.BaseConf.enableDebug();
		String text = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
		Segmenter dictThaiSeg = new ThaiDoubleArrayTrieSegmenter();
		List<Term> termListThai = dictThaiSeg.segment(text);
		String resultThai = "";
		for (Term term : termListThai) {
			resultThai += term.getWord() + "|";
		}
		System.out.println(termListThai);
		System.out.println("分词结果：" + resultThai);
		//分词结果：ความ|สัมพันธ์|ใน|ทาง|เศรษฐกิจ|กับ|ระบบ|ความ|สัมพันธ์|ทาง|กฎหมาย|
		//分词结果：ความสัมพันธ์|ในทาง|เศรษฐกิจ|กับ|ระบบ|ความสัมพันธ์|ทาง|กฎหมาย|
		
		int segWords = termListThai.size();
		int pressure = 10000;		
		long start = System.currentTimeMillis();
		for (int i = 0; i < pressure; ++i) {
			dictThaiSeg.segment(text);
		}
		double costTime = (System.currentTimeMillis() - start) / (double)1000;
		System.out.printf("分词速度：%.2f词每秒", segWords * pressure / costTime);
	}
	
	public void testLaoDictSegment() {
		Config.BaseConf.enableDebug();
		String text = "ທ່ານວິນເຄັນເປັນປະທານບໍລິສັດອຽວເຊີວີເອີແອນ.ວີ.ກຸ່ມບໍລິສັດການພິມຂອງຊາວດັດ.";
		//标准结果：ທ່ານ<TTL> ວິນເຄັນ<PRN> ເປັນ<V> ປະທານ<N>ບໍລິສັດ<N> ອຽວເຊີວີເອີ<PRN> ແອນ<PRN>.<PM>ວີ<PRN>.<PM> ກຸ່ມ<CLT>ບໍລິສັດ<N>ການພິມ<N>ຂອງ<PRE>ຊາວ<CLT>ດັດ<PRN>.<PM>
		Segmenter dictLaoSeg = new LaoDoubleArrayTrieSegmenter();
		List<Term> termListLao = dictLaoSeg.segment(text);
		String resultLao = "";
		for (Term term : termListLao) {
			resultLao += term.getWord() + "|";
		}
		
		System.out.println(termListLao);
		System.out.println("分词结果：" + resultLao);
				
		int segWords = text.length();
		int pressure = 10000;		
		long start = System.currentTimeMillis();
		for (int i = 0; i < pressure; ++i) {
			dictLaoSeg.segment(text);
		}
		double costTime = (System.currentTimeMillis() - start) / (double)1000;
		System.out.printf("分词速度：%.2f词每秒", segWords * pressure / costTime);
		
	}
	
	public void testVietnameseDictSegment() {
		Config.BaseConf.enableDebug();
		String text = "Khi nhung trên đầu đã bắtđầu phân nhị , nhánh phân đã có hiện tượng chuẩn bị phân tam ( từ của những người nuôi hươu gọi là yên ngựa ) là lúc cần phải thu hoạch .";
		//Khi/N nhung/N trên/E đầu/N đã/R bắt_đầu/V phân/V nhị/M ,/CH nhánh/N phân/N đã/R có/V hiện_tượng/N chuẩn_bị/V phân/V tam/M (/CH từ/E của/E những/L người/N nuôi/V hươu/N gọi_là/V yên/N ngựa/N )/CH là/V lúc/N cần/V phải/V thu_hoạch/V ./CH
		//Khi/N nhung/N trên/E đầu/N đã/R bắt/V đầu/N phân/N nhị/M ,/CH nhánh/N phân/N đã/R có/V hiện tượng/N chuẩn bị/V phân/N tam/M (/CH từ/E của/E những/L người/N nuôi/V hươu/N gọi là/V yên/A ngựa/N )/CH là/V lúc/N cần/V phải/V thu hoạch/V 
		Segmenter dictViSeg = new VietnameseDoubleArrayTrieSegmenter();
		List<Term> termListVi = dictViSeg.segment(text);
		String resultVi = "";
		for (Term term : termListVi) {
			if (term.getWord().trim().isEmpty()) {
				resultVi += " ";
			} else {
				resultVi += term.getWord() + "/" + term.getNature() + " ";
			}
		}
		
		System.out.println(termListVi);
		System.out.println("分词结果：" + resultVi);
				
		int segWords = text.length();
		int pressure = 10000;		
		long start = System.currentTimeMillis();
		for (int i = 0; i < pressure; ++i) {
			dictViSeg.segment(text);
		}
		double costTime = (System.currentTimeMillis() - start) / (double)1000;
		System.out.printf("分词速度：%.2f词每秒", segWords * pressure / costTime);
		
	}
	
	public void testBurmeseDictSegment() {
		Config.BaseConf.enableDebug();
		String text = "ကံဆိုးကံဇာတာကံထိုက်ကံနခိုကံနှိုးဆော်";
		Segmenter dictBuSeg = new BurmeseDoubleArrayTrieSegmenter();
		List<Term> termListBu = dictBuSeg.segment(text);
		String resultBu = "";
		for (Term term : termListBu) {
			resultBu += term.getWord() + "|";
		}
		
		System.out.println(termListBu);
		System.out.println("分词结果：" + resultBu);
				
		int segWords = text.length();
		int pressure = 10000;		
		long start = System.currentTimeMillis();
		for (int i = 0; i < pressure; ++i) {
			dictBuSeg.segment(text);
		}
		double costTime = (System.currentTimeMillis() - start) / (double)1000;
		System.out.printf("分词速度：%.2f词每秒", segWords * pressure / costTime);
		System.out.println(SEANLP.Burmese.syllableSegment(text));
	}
	
	public void testKhmerDictSegment() {
//		ធាតុ	NN	1
//		ពិត	JJ	1
//		និង	CC	1
//		ការបន្ដ	NN	1
//		គំរាមកំហែង	V	1
//		មកលើ	IN	1
//		អ្នកការពារ	NN	1
//		សិទ្ធិ	NN	1
//		មនុស្ស	NN	1
//		នៅ	IN	1
//		កម្ពុជា	NNP	1
//		។	M	1
//		របាយការណ៍	NN	1
//		នេះ	PRP	1
//		ផ្អែកលើ	V	1
//		ការស៊ើបអង្កេត	NN	1
		
		Config.BaseConf.enableDebug();
		String text = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
//		Segmenter dictKhSeg = new KhmerCommonDATSegmenter();
		Segmenter dictKhSeg = new KhmerDoubleArrayTrieSegmenter();
		List<Term> termListKh = dictKhSeg.segment(text);
		String resultKh = "";
		for (Term term : termListKh) {
			resultKh += term.getWord() + "|";
		}
		
		System.out.println(termListKh);
		System.out.println("分词结果：" + resultKh);
				
		int segWords = text.length();
		int pressure = 10000;		
		long start = System.currentTimeMillis();
		for (int i = 0; i < pressure; ++i) {
			dictKhSeg.segment(text);
		}
		double costTime = (System.currentTimeMillis() - start) / (double)1000;
		System.out.printf("分词速度：%.2f词每秒", segWords * pressure / costTime);
		
		
	}

}
