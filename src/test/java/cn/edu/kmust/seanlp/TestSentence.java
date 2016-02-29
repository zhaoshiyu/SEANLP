package cn.edu.kmust.seanlp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSentence {
	public static String[] sentenceSegment(String text) {
		String regex="[.;!?។]";
		if (Config.language.equals(Language.Thai)) {
			regex += "|[ ]";
		}
		Pattern pattern =Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		//按照句子结束符分割句子
		String[] substrs = pattern.split(text);
		//将句子结束符连接到相应的句子后
		if (substrs.length > 0) {
		    int count = 0;
		    while (count < substrs.length) {
		        if (matcher.find()) {
		        	substrs[count] += matcher.group();
		        }
		        count++;
		    }
		}
		return substrs;
	}
	
	public static void main(String[] args) {
		String text = "ความสัมพันธ์...ในทาง;เศรษฐกิจกับระบบความสัมพันธ์.ทาง  กฎหมาย";
		String[] dd = sentenceSegment(text);
		for (String str : dd) 
		System.out.println(str);
		String ss = "";
		System.out.println(ss.isEmpty());
		System.out.println(ss.length() == 0);
	}

}
