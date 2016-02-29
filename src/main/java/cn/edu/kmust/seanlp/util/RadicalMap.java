package cn.edu.kmust.seanlp.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取语言字母、音节类别工具类
 * @author Zhao Shiyu
 *
 */
public class RadicalMap {
	//Thai
	private static HashMap<Character, Character> charsToRads = new HashMap<Character, Character>();
	private static HashMap<Character, Set<Character>> radsToChars = new HashMap<Character, Set<Character>>();
	
	static {
		String[] radLists = {
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
		for (int i = 0; i < radLists.length; i++) {
			Set<Character> chars = new HashSet<Character>();
			char rad = radLists[i].charAt(0);
			int j = 1;
			for (int rLeng = radLists[i].length(); j < rLeng; j++) {
				char ch = radLists[i].charAt(j);
				charsToRads.put(Character.valueOf(ch), Character.valueOf(rad));
				chars.add(Character.valueOf(ch));
			}
			radsToChars.put(Character.valueOf(rad), chars);
		}
	}
	
	/**
	 * 当前泰语字母的类别<br>
	 * @param ch
	 * @return Character字符类别
	 */
	public static char getThaiRadical(char ch) {
		Character character = (Character) charsToRads.get(Character.valueOf(ch));
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
		Character character = (Character) charsToRads.get(Character.valueOf(ch));
		if (character != null) {
			return character.toString();
		}
		return "o";
	}

	public static Set<Character> getThaiChars(char ch) {
		return (Set<Character>) radsToChars.get(Character.valueOf(ch));
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
		  String temp="";
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
	
}
