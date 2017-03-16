package cn.edu.kmust.seanlp.segmenter.CC;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * KCC切分
 * 
 * @author Zhao Shiyu
 *
 */
public class KCC extends AbstractCC {
	
	private final static String KHMER_PUNCTUATION = "([\u17D4-\u17DD])"; // 高棉文标点符号
	private final static String KHMER_DIGITS = "([\u17E0-\u17E9]+)";// 高棉文数字0-9
	private final static String KHMER_DIGITS_O = "([\u17F0-\u17F9]+)";// 高棉文数字0-9另一种形式
	/**
	 * KCC由一个元音加上一个特殊元音构成：元音+特殊元音
	 */
	private final static String KCC_VV = "([\u17A3-\u17C5][\u17C6-\u17C8])";
	
	/**
	 * KCC由一个辅音或元音加上元音再加上一个特殊元音构成：辅音/元音+元音+特殊元音
	 */
	private final static String KCC_CVVV = "(([\u1780-\u17A2]|[\u17A3-\u17C5])[\u17A3-\u17C5][\u17C6-\u17C8])";
	
	private final static String KCC_K = "(([\u1780-\u17A2]|[\u17A3-\u17C5])(\u17CC|(\u17C9|\u17CA))?"
			+ "(\u17D2([\u1780-\u17A2](\u17C9|\u17CA)?|[\u17A3-\u17C5]"
			+ "(\u17C9|\u17CA)?)){0,2}[\u17B6-\u17C8]?[\u17CB-\u17D3]{0,2}(\u17D2[\u17A3-\u17C5])?"
			+ "[\u17B6-\u17C8])";
	
	private final static String KCC_CC = "(([\u1780-\u17A2]|[\u17A3-\u17C5])(\u17CC|(\u17C9|\u17CA))?"
			+ "(\u17D2([\u1780-\u17A2](\u17C9|\u17CA)?|[\u17A3-\u17C5](\u17C9|\u17CA)?)){0,2}"
			+ "[\u17B6-\u17C8]?[\u17CB-\u17D3]{0,2}(\u17D2([\u1780-\u17A2]|[\u17A3-\u17C5]))?)";
	
	private final static String dont_know = 
			DIGITS
			+ orex + ENGLISH
			+ orex + PUNCTUATION
			+ orex + NEWLINES
			+ orex + WS
			+ orex + DONT_KNOW
			;
	
	private final static String kcc = 
			KCC_VV
			 + orex + KCC_CVVV
			 + orex + KCC_K
			 + orex + KCC_CC
			 + orex + KHMER_PUNCTUATION
			 + orex + KHMER_DIGITS
			+ orex + KHMER_DIGITS_O 
			+ orex + dont_know
			;// end KCC head
	
	// 将高棉文分解为KCC序列
	public static String[] toKCC(String khmer) {

		Pattern p = Pattern.compile(kcc);//编译regEx,成为Pattern类的实例，java中用于匹配正则表达式
		Matcher m = p.matcher(khmer);//调用matcher方法，找到khmer中与正则表达式匹配的。
		StringBuffer sb = new StringBuffer();
		while (m.find()) {//如果匹配到结果
			sb.append(m.group()).append(orex);
		}
		return sb.toString().split("\\|");
	}
	
	// 将高棉文分解为KCC序列
	@Override
	public String token(String sentence) {
		Pattern p = Pattern.compile(kcc);
		Matcher m = p.matcher(sentence);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {//如果匹配到结果
			sb.append(m.group()).append(orex);
		}
		return sb.toString();
	}
	
	@Override
	public List<Term> segment(String sentence) {
		Pattern p = Pattern.compile(kcc);
		Matcher m = p.matcher(sentence);
		List<Term> result = new LinkedList<Term>();
		while (m.find()) {//如果匹配到结果
			result.add(new Term(m.group(), null));
		}
		return result;
	}
	
	public static void main(String[] args) {
		
		String text = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		
//		System.out.println(kcc);
		System.out.println(KCC.toKCC(text));
		int segWords = text.length();
		int pressure = 10000;		
		long start = System.currentTimeMillis();
		for (int i = 0; i < pressure; ++i) {
			KCC.toKCC(text);
		}
		double costTime = (System.currentTimeMillis() - start) / (double)1000;
		System.out.printf("速度：%.2f 字符每秒", segWords * pressure / costTime);
		System.err.println();
		System.err.println(text);
		String rText = "";
		for (String t : KCC.toKCC(text)) {
			rText += t + "|";
		}
		System.out.println(rText);
	}

}
