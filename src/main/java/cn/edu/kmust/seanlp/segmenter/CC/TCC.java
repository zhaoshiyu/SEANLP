package cn.edu.kmust.seanlp.segmenter.CC;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * TCC切分<br>
 * 使用正则表达式实现TCC切分，效率不高<br>
 * 可以参考Wittawat Jitkrittum 的另一种实现：https://github.com/wittawatj/jtcc.git
 * @author Zhao Shiyu
 *
 */
public class TCC extends AbstractCC {
	
	/*
	private final static String KO_KAI	 = "\u0E01";
	private final static String KHO_KHAI = "\u0E02";
	private final static String KHO_KHUAT = "\u0E03";
	private final static String KHO_KHWAI = "\u0E04";
	private final static String KHO_KHON = "\u0E05";
	private final static String KHO_RAKHANG	 = "\u0E06";
	*/
	
	private final static String CON = "[\u0E01-\u0E2E]";
	private final static String YOYAK = "\u0E22"; 
	private final static String O_ANG = "\u0E2D" ; 
	private final static String WA = "\u0E27\u0E30"; 
	private final static String SIGN = "\u0E2F";
	private final static String A = "\u0E30" ; 
	private final static String MAI_HAN_AKAT = "\u0E31"; 
	private final static String SARA_AA = "\u0E32" ; 
	private final static String SARA_AM = "\u0E33";
	private final static String SARA_I = "\u0E34" ;  
	private final static String SARA_II = "\u0E35" ; 
	private final static String SARA_UE = "\u0E36" ;
	private final static String SARA_UEE = "\u0E37" ;
	private final static String SARA_U = "\u0E38";
	private final static String SARA_UU = "\u0E39";
	private final static String PHINTHU = "\u0E3A";
	private final static String SYMBOL_BAHT = "\u0E3F";
	private final static String EE = "\u0E40"; 
	private final static String AE = "\u0E41"; 
	private final static String OO = "\u0E42"; 
	private final static String MAIMUAN = "\u0E43";
	private final static String MAIMALAI = "\u0E44";	
	private final static String REPE_MARK = "\u0E46";
	private final static String MAITAIKHU = "\u0E47";
	private final static String TON = "[\u0E48\u0E49\u0E4A\u0E4B]";//"\u0E48 + orex + \u0E49 + orex + \u0E4A + orex + \u0E4B" ;
	private final static String THANTHAKHAT = "\u0E4C" ;
	private final static String NIKHAHIT = "\u0E4D" ;
	private final static String YAMAKKAN = "\u0E4E" ;
	private final static String FONGMAN = "\u0E4F" ;
	private final static String THAI_DIGITS = "[\u0E50-\u0E59]+" ;
	private final static String ANGKHANKHU = "\u0E5A" ;
	private final static String KHOMUT = "\u0E5B" ;

	/*
	private final static String TCCIGNORE = "";//"<TCCIGNORE>" (options{greedy=false;} = . )* "</TCCIGNORE>";	// Text within <TCCIGNORE>....</TCCIGNORE> will not be tokenized.
	private final static String HIDDEN	 = ("\r" + orex  + "\n") +  { $channel = HIDDEN; } ;
	private final static String DONT_KNOW = ".";//;( WS + orex + ("0".."9") +  + orex + ("a".."z" + orex  + "A".."Z") +   + orex + ( "\u0E50".."\u0E59") +  + orex + .) ; //ALso detect consecutive Thai numbers.
	 */
	
	private final static String vback = MAI_HAN_AKAT  + orex +  SARA_UEE ;// + orex +  SARA_UE ;
	
	private final static String vfront =  SARA_I  + orex +  SARA_II  + orex +  SARA_U  + orex +  SARA_UU;
	
	private final static String vO1 = A  + orex +  SARA_AA  + orex +  SARA_AM ;
	
	private final static String vneedl = vfront + orex +  vO1  + orex +  SARA_UEE  + orex +  SARA_UE ;
	
	private final static String needl	 = vneedl  + orex +  THANTHAKHAT;

	private final static String vO2 = EE  + orex +  AE  + orex +  OO  + orex +  MAIMUAN  + orex +  MAIMALAI ;

	private final static String con = CON  + orex +  YOYAK  + orex +  O_ANG ;
	
	private final static String newlines = left_parenthesis + NEWLINES + right_parenthesis + question_mark;
	
	private final static String tccfirst = vO2  + orex +  left_parenthesis + con + right_parenthesis  + orex +  WA  + orex +  DONT_KNOW; //  + orex +  TCCIGNORE  //  + orex;// +  EOF;
	
	private final static String dont_know = 
			WA
			+  orex + EE
			+  orex  + AE
			+  orex + OO
			+  orex + MAIMUAN
			+  orex + MAIMALAI
			+  orex + YOYAK
			+  orex + MAITAIKHU
			+  orex + THANTHAKHAT
			+  orex + A
			+  orex + O_ANG
			+  orex + SARA_UE
			+  orex + SARA_UEE 
			+  orex + SARA_I 
			+  orex + SARA_II
			+  orex + SARA_AA
			+  orex + SARA_AM 
			+  orex + MAI_HAN_AKAT
			+  orex + SARA_U 
			+  orex + SARA_UU
			+  orex + TON
			//+  orex + TCCIGNORE
			+  orex + CON
			+  orex + SIGN
			+  orex + REPE_MARK
			+  orex + PHINTHU
			+  orex + SYMBOL_BAHT
			+  orex + NIKHAHIT
			+  orex + YAMAKKAN
			+  orex + FONGMAN
			+  orex + THAI_DIGITS
			+  orex + ANGKHANKHU
			+  orex + KHOMUT
			+  orex + DIGITS
			+  orex + ENGLISH
			+  orex + NEWLINES
			+  orex + PUNCTUATION
			+  orex + WS
			+  orex + DONT_KNOW
			;// end dont_know head
	

	private final static String tcc = 
			left_parenthesis + EE + left_parenthesis + con + right_parenthesis + MAITAIKHU + left_parenthesis + con + right_parenthesis + right_parenthesis
			 //+ orex + left_parenthesis + EE  + left_parenthesis + con + right_parenthesis + TON + question_mark + SARA_AA + right_parenthesis//me
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + TON + question_mark + SARA_AA + A + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + SARA_II + TON + question_mark + YOYAK + A + right_parenthesis
			 //+ orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + SARA_II + TON + question_mark + YOYAK + tccfirst + right_parenthesis + "=>" + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + SARA_II + TON + question_mark + YOYAK
//			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + SARA_II + TON + question_mark + YOYAK + left_parenthesis + tccfirst + right_parenthesis + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + SARA_II + TON + question_mark + YOYAK + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + O_ANG + A + right_parenthesis	
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + MAITAIKHU + left_parenthesis + con + right_parenthesis + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + SARA_I + left_parenthesis + con + right_parenthesis + THANTHAKHAT + left_parenthesis + con + right_parenthesis + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + SARA_I + TON + question_mark + left_parenthesis + con + right_parenthesis + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + SARA_II + TON + question_mark + YOYAK + A + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + SARA_II + O_ANG + A + question_mark + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + SARA_UEE + TON + O_ANG + A + question_mark + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + SARA_UEE + O_ANG + A + question_mark + right_parenthesis
			// + orex + (EE + con + vfront + TON + question_mark + YOYAK + tccfirst) + "=>" + EE + con + vfront + TON + question_mark + YOYAK
			//+ orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + vfront + right_parenthesis + TON + question_mark + YOYAK + left_parenthesis + tccfirst + right_parenthesis + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + left_parenthesis + vfront + right_parenthesis + TON + question_mark + YOYAK + right_parenthesis
			 + orex + left_parenthesis + EE + left_parenthesis + con + right_parenthesis + TON + question_mark + SARA_AA + question_mark + A + question_mark + right_parenthesis
			 + orex + left_parenthesis + left_parenthesis + vO2 + right_parenthesis + left_parenthesis + con + right_parenthesis + TON + question_mark + right_parenthesis
			 + orex + left_parenthesis + left_parenthesis + con + right_parenthesis + MAI_HAN_AKAT + TON + question_mark + WA + right_parenthesis
			 + orex + left_parenthesis + left_parenthesis + con + right_parenthesis + left_parenthesis + vback + right_parenthesis + TON + question_mark + con + left_parenthesis + SARA_U + orex + SARA_I + orex + A + right_parenthesis + question_mark + right_parenthesis
			 + orex + left_parenthesis + left_parenthesis + con + right_parenthesis + left_parenthesis + SARA_I + orex + SARA_U + right_parenthesis + question_mark + THANTHAKHAT + right_parenthesis
			 + orex + left_parenthesis + left_parenthesis + con + right_parenthesis + left_parenthesis + vneedl + right_parenthesis + TON + question_mark + right_parenthesis
			 + orex + left_parenthesis + left_parenthesis + con + right_parenthesis + MAITAIKHU + right_parenthesis
			 + orex + left_parenthesis + left_parenthesis + con + right_parenthesis + TON + question_mark + SARA_AA + question_mark + A + question_mark + right_parenthesis
			 + orex + left_parenthesis + left_parenthesis + con + right_parenthesis + TON + question_mark + left_parenthesis + vO1 + right_parenthesis + question_mark + right_parenthesis
			 + orex + left_parenthesis + AE + left_parenthesis + con + right_parenthesis + MAITAIKHU + left_parenthesis + con + right_parenthesis + right_parenthesis
			 + orex + left_parenthesis + AE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + THANTHAKHAT + right_parenthesis
			 + orex + left_parenthesis + AE + left_parenthesis + con + right_parenthesis + TON + question_mark + A + right_parenthesis
			 + orex + left_parenthesis + AE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + MAITAIKHU + left_parenthesis + con + right_parenthesis + right_parenthesis	
			 + orex + left_parenthesis + AE + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + left_parenthesis + con + right_parenthesis + THANTHAKHAT + right_parenthesis	
			 + orex + left_parenthesis + OO + left_parenthesis + con + right_parenthesis + TON + question_mark + A + right_parenthesis
			 + orex + dont_know
			;// + end TCC head
	
	/*
	private final static String tcc = 
			"(" + EE + "(" + con + ")" + MAITAIKHU + "(" + con + ")" + ")"
			 //+ orex + "(" + EE  + "(" + con + ")" + TON + "?" + SARA_AA + ")"//me
			 + orex + "(" + EE + "(" + con + ")" + "(" + con + ")" + TON + "?" + SARA_AA + A + ")"
			 + orex + "(" + EE + "(" + con + ")" + "(" + con + ")" + SARA_II + TON + "?" + YOYAK + A + ")"
			 //+ orex + "(" + EE + "(" + con + ")" + "(" + con + ")" + SARA_II + TON + "?" + YOYAK + tccfirst + ")" + "=>" + EE + "(" + con + ")" + "(" + con + ")" + SARA_II + TON + "?" + YOYAK
//			 + orex + "(" + EE + "(" + con + ")" + "(" + con + ")" + SARA_II + TON + "?" + YOYAK + "(" + tccfirst + ")" + ")"
			 + orex + "(" + EE + "(" + con + ")" + "(" + con + ")" + SARA_II + TON + "?" + YOYAK + ")"
			 
			
			 + orex + "(" + EE + "(" + con + ")" + "(" + con + ")" + O_ANG + A + ")"	
			 + orex + "(" + EE + "(" + con + ")" + "(" + con + ")" + MAITAIKHU + "(" + con + ")" + ")"
			 + orex + "(" + EE + "(" + con + ")" + SARA_I + "(" + con + ")" + THANTHAKHAT + "(" + con + ")" + ")"
			 + orex + "(" + EE + "(" + con + ")" + SARA_I + TON + "?" + "(" + con + ")" + ")"
			 + orex + "(" + EE + "(" + con + ")" + SARA_II + TON + "?" + YOYAK + A + ")"
			 + orex + "(" + EE + "(" + con + ")" + SARA_II + O_ANG + A + "?" + ")"
			 + orex + "(" + EE + "(" + con + ")" + SARA_UEE + TON + O_ANG + A + "?" + ")"
			 + orex + "(" + EE + "(" + con + ")" + SARA_UEE + O_ANG + A + "?" + ")"
			// + orex + (EE + con + vfront + TON + "?" + YOYAK + tccfirst) + "=>" + EE + con + vfront + TON + "?" + YOYAK
//			+ orex + "(" + EE + "(" + con + ")" + "(" + vfront + ")" + TON + "?" + YOYAK + "(" + tccfirst + ")" + ")"
			 + orex + "(" + EE + "(" + con + ")" + "(" + vfront + ")" + TON + "?" + YOYAK + ")"
			 
			 + orex + "(" + EE + "(" + con + ")" + TON + "?" + SARA_AA + "?" + A + "?" + ")"
			 + orex + "(" + "(" + vO2 + ")" + "(" + con + ")" + TON + "?" + ")"
			 + orex + "(" + "(" + con + ")" + MAI_HAN_AKAT + TON + "?" + WA + ")"
			 + orex + "(" + "(" + con + ")" + "(" + vback + ")" + TON + "?" + con + "(" + SARA_U + orex + SARA_I + orex + A + ")" + "?" + ")"
			 + orex + "(" + "(" + con + ")" + "(" + SARA_I + orex + SARA_U + ")" + "?" + THANTHAKHAT + ")"
			 + orex + "(" + "(" + con + ")" + "(" + vneedl + ")" + TON + "?" + ")"
			 + orex + "(" + "(" + con + ")" + MAITAIKHU + ")"
			 + orex + "(" + "(" + con + ")" + TON + "?" + SARA_AA + "?" + A + "?" + ")"
			 + orex + "(" + "(" + con + ")" + TON + "?" + "(" + vO1 + ")" + "?" + ")"
			 + orex + "(" + AE + "(" + con + ")" + MAITAIKHU + "(" + con + ")" + ")"
			 + orex + "(" + AE + "(" + con + ")" + "(" + con + ")" + THANTHAKHAT + ")"
			 + orex + "(" + AE + "(" + con + ")" + TON + "?" + A + ")"
			 + orex + "(" + AE + "(" + con + ")" + "(" + con + ")" + MAITAIKHU + "(" + con + ")" + ")"	
			 + orex + "(" + AE + "(" + con + ")" + "(" + con + ")" + "(" + con + ")" + THANTHAKHAT + ")"	
			 + orex + "(" + OO + "(" + con + ")" + TON + "?" + A + ")"
			 + orex + dont_know
			;// end TCC head
	*/
	
	@Override
	public String token(String sentence) {
		Pattern p = Pattern.compile(tcc);
		Matcher m = p.matcher(sentence);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {//如果匹配到结果
			sb.append(m.group()).append(orex);
		}
		return sb.toString();
	}
	
	@Override
	public List<Term> segment(String sentence) {
		Pattern p = Pattern.compile(tcc);
		Matcher m = p.matcher(sentence);
		
		List<Term> result = new LinkedList<Term>();
		while (m.find()) {//如果匹配到结果
			result.add(new Term(m.group(), null));
		}
		return result;
	}
	
	public static String[] toTCC(String sentence) {
		Pattern p = Pattern.compile(tcc);
		Matcher m = p.matcher(sentence);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {//如果匹配到结果
			sb.append(m.group()).append(orex);
		}
		return sb.toString().split("\\|");
	}
	
	public String regexToken(String regex, String sentence) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sentence);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {//如果匹配到结果
			sb.append(m.group()).append(orex);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String content = "เจ้าของบ้านจะต้องไปติดต่อที่การไฟฟ้านครหลวง หรือการไฟฟ้าส่วนภูมิภาคของเขตนั้นๆ เพื่อขออนุญาตใช้ไฟฟ้าเอ็ง \n ds:gdf!`sa。fds!af*f(jh)sa&dhk^kh……ggf;dg.ds2423sd234324@#@$d我；";
		
		//System.out.println(tcc);
		TCC tCC =new TCC();
		System.out.println(toTCC(content));
		System.out.println(tCC.regexToken(tcc, content));
		System.out.println("เจ้า|ข|อ|ง|บ้า|น|จะ|ต้|อ|ง|ไป|ติ|ด|ต่|อ|ที่|กา|ร|ไฟ|ฟ้า|น|ค|ร|ห|ล|ว|ง| |ห|รือ|กา|ร|ไฟ|ฟ้า|ส่|ว|น|ภู|มิ|ภา|ค|ข|อ|ง|เข|ต|นั้น|ๆ| |เพื่อ|ข|อ|อ|นุ|ญา|ต|ใช้|ไฟ|ฟ้า|เอ็ง|");
		
		int segWords = content.length();
		int pressure = 10000;		
		long start = System.currentTimeMillis();
		for (int i = 0; i < pressure; ++i) {
			tCC.token(content);
		}
		double costTime = (System.currentTimeMillis() - start) / (double)1000;
		System.out.printf("速度：%.2f 字符每秒", segWords * pressure / costTime);
	}

	

	

	

	
			
}
