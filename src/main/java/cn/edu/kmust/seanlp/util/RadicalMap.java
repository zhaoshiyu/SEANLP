package cn.edu.kmust.seanlp.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 获取语言字母、音节类别工具类
 * @author Zhao Shiyu
 *
 */
public class RadicalMap {
	//Thai
	private static Map<Character, Character> thaiCharsToRads = new HashMap<Character, Character>();
	private static Map<Character, Set<Character>> thaiRadsToChars = new HashMap<Character, Set<Character>>();
	
	//Myanmar
	private static Map<Character, Character> myanmarCharsToRads = new HashMap<Character, Character>();
	private static Map<Character, Set<Character>> myanmarRadsToChars = new HashMap<Character, Set<Character>>();
	
	static {
		//Thai
		String[] thaiRadLists = {
				"eabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
				"cกขฃคฆงจชซญฎฏฐฑฒณดตถทธนบปพฟภมยรลวศษสฬอ",
				"nฅฉผฝฌหฮฤฦ",
				"vะัิีึืฺุูาำๅ",
				"wเแโใไ", 
				"t่้๊๋",
				"sฯๆ๎์ํ๏๚๛",
				"d0123456789๐๑๒๓๔๕๖๗๘๙",
				"q.,?!;:`~-_=+'\"\\/()[]{}<>@#$%^&*",
				"p "
				 };
		for (int i = 0; i < thaiRadLists.length; i++) {
			Set<Character> chars = new HashSet<Character>();
			char rad = thaiRadLists[i].charAt(0);
			int j = 1;
			for (int rLeng = thaiRadLists[i].length(); j < rLeng; j++) {
				char ch = thaiRadLists[i].charAt(j);
				thaiCharsToRads.put(Character.valueOf(ch), Character.valueOf(rad));
				chars.add(Character.valueOf(ch));
			}
			thaiRadsToChars.put(Character.valueOf(rad), chars);
		}
		
		
		//Myanmar
		String[] myanmarRadLists = {
				"EabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
				"D0123456789၀၁၂၃၄၅၆၇၈၉",                            //1040-1049
				"Cကခဂဃငစဆဇဈဉ ညဋဌဍဎဏတထဒဓနပဖဗဘမယရလဝသဟဠအ",   //1000-1021
				"Mျြှွ",										//103B-103E
				"Vါာိီုူေဲ",									//102B-1032
				"A်",										//103A
				"S◌",										//1039
				"Fး့ံ",										//1036-1038
				"Iဤဧဪ၌၍၏",  								//1024,1027,102A,104C,104D,104F
				"Eဣဥဦဩ၎",									//1023,1025,1026,1029,104E
				"Gဿ",										//103F								
				"P၊။",										//104A-104B
				"W "										//0020
		};
		
		for (int i = 0; i < myanmarRadLists.length; i++) {
			Set<Character> chars = new HashSet<Character>();
			char rad = myanmarRadLists[i].charAt(0);
			int j = 1;
			for (int rLeng = myanmarRadLists[i].length(); j < rLeng; j++) {
				char ch = myanmarRadLists[i].charAt(j);
				myanmarCharsToRads.put(Character.valueOf(ch), Character.valueOf(rad));
				chars.add(Character.valueOf(ch));
			}
			myanmarRadsToChars.put(Character.valueOf(rad), chars);
		}
	}
	
	/**
	 * 当前泰语字母的类别<br>
	 * @param ch
	 * @return Character字符类别
	 */
	public static char getThaiRadical(char ch) {
		Character character = (Character) thaiCharsToRads.get(Character.valueOf(ch));
		if (character != null) {
			return character.charValue();
		}
		return 'o';
	}

	/**
	 * 当前泰语字符的类别<br>
	 * @param ch 泰语字符
	 * @return String字符串类别
	 */
	public static String getThaiCharType(char ch) {
		Character character = (Character) thaiCharsToRads.get(Character.valueOf(ch));
		if (character != null) {
			return character.toString();
		}
		return "o";
	}

	public static Set<Character> getThaiChars(char ch) {
		return (Set<Character>) thaiRadsToChars.get(Character.valueOf(ch));
	}
	
   public static String getThaiSyllableType(String str) {
	   String type;
	   if (str.matches("[0-9]+")) {
			type = "d";
		} else if (str.matches("[a-zA-Z]+")) {
			type = "e";
		} else if (str.matches("[ก-๛]+")) {
			type = "t";
		} else if (str.matches("[\\pP‘’“”]+")) {
			type = "p";
		} else {
			type = "v";
		}
	   return type;
   }
	
	/**
	 * 当前泰语音节类别<br>
	 * @param syllable 泰语音节
	 * @return String 类型
	 */
	/**
	public static String getThaiSyllableType(String syllable) {
		String type;
		if (syllable.matches("[ก-๛]+")) {
			if (syllable.matches(".*่.*")) {
				type = "l";
			} else if (syllable.matches(".*้.*")) {
				type = "f";
			} else if (syllable.matches(".*๊.*")) {
				type = "h";
			} else if (syllable.matches(".*๋.*")) {
				type = "r";
			} else {
				type = "m";
			}
		} else if (syllable.matches("[0-9๐-๙]+")) { //syllable.matches("[0-9\u0E50-\u0E59]+")
			type = "d";
		} else if (syllable.matches("[A-Za-z]+")) {
			type = "e";
		} else if (syllable.matches("[\\pP‘’“”]+")) {
			type = "p";
		}else {
			type = "o";
		}
		return type;
	}
	**/
	
	/**
	 * 当前泰语音节类别<br>
	 * @param str
	 * @return String
	 */
	   /**
	public static String getThaiSyllableType(String str) {
		String type;
		if (str.matches("[ก-๛]+")) {
			if (str.contains("\u0E48")) {
				type = "l";
			} else if (str.contains("\u0E49")) {
				type = "f";
			} else if (str.contains("\u0E49")) {
				type = "h";
			} else if (str.contains("\u0E49")) {
				type = "r";
			} else {
				type = "m";
			}
		} else if (str.matches("[0-9๐-๙]+")) {
			type = "d";
		} else if (str.matches("[a-zA-Z]+")) {
			type = "e";
		} else if (str.matches("[\\pP‘’“”]+")) {
			type = "p";
		} else {
			type = "o";
		}
		return type;
	}
	**/
	
	/**
	 * 当前泰语词类别<br>
	 * @param word 泰语词
	 * @return 
	 */
	public static String getThaiWordType(String word) {
		String type;
		if (word.matches("[0-9]+")) {
			type = "d";
		} else if (word.matches("[a-zA-Z]+")) {
			type = "e";
		} else if (word.matches("[ก-๛]+")) {
			type = "t";
		} else if (word.matches("[\\pP‘’“”]+")) {
			type = "p";
		} else {
			type = "v";
		}
		return type;
	}
	
	/**
	 * 当前越南语词类别<br>
	 * @param vnword
	 * @return String
	 */
	public static String getVietnameseType(String viWord) {
		String temp = "";
		if (viWord.matches("^[0-9]+$")) {
			temp = "d";
		} else if (viWord.matches("^[A-Za-z]+$")) {
			temp = "e";
		} else if (viWord.matches("^[À-ỹ]+$")) {
			temp = "t";
		} else if (viWord.matches("[\\pP]+")) {
			temp = "p";
		} else {
			temp = "v";
		}
		return temp;
	}
	
	/**
	 * 当前缅甸语字母的类别<br>
	 * @param ch 缅甸语
	 * @return Character字符类别
	 */
	public static char getMyanmarRadical(char ch) {
		Character character = (Character) myanmarCharsToRads.get(Character.valueOf(ch));
		if (character != null) {
			return character.charValue();
		}
		return 'o';
	}

	/**
	 * 当前缅甸语字符的类别<br>
	 * @param ch 缅甸语字符
	 * @return String字符串类别
	 */
	public static String getMyanmarCharType(char ch) {
		Character character = (Character) myanmarCharsToRads.get(Character.valueOf(ch));
		if (character != null) {
			return character.toString();
		}
		return "o";
	}

	public static Set<Character> getMyanmarChars(char ch) {
		return (Set<Character>) myanmarRadsToChars.get(Character.valueOf(ch));
	}
	
}
