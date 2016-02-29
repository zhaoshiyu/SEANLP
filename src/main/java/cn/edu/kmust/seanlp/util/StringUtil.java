package cn.edu.kmust.seanlp.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Language;

/**
 * 字符串操作工具（主要针对音节，句子切分等）
 * 
 * @author  Zhao Shiyu
 *
 */
public class StringUtil {
	
	/**
	 * 从字符串数组中获取词
	 * 
	 * @param strArray
	 * @param offset
	 * @param count
	 * @return
	 */
	public static String merge(String[] strArray, int offset, int count) {
		if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > strArray.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }
        return toString(Arrays.copyOfRange(strArray, offset, offset+count));
	}
	
	/**
	 * 获取越南语词（越南语与其它四种语言不同，单独处理）
	 * @param strArray
	 * @param offset
	 * @param count
	 * @return
	 */
	public static String getViWord(String[] strArray, int offset, int count) {
		if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        if (offset > strArray.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }
        return StringUtil.getViWord(Arrays.copyOfRange(strArray, offset, offset+count));
	}
	
	/**
	 * String数组转String
	 * @param values
	 * @return
	 */
	public static String toString(String[] values) {
		int len = values.length;
		if (len == 0) return null;
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i< len; i++) {
        	if (Config.language.equals(Language.Vietnamese)) 
        		sb.append(' ').append(values[i]);
        	else
        		sb.append(values[i]);
        }
		return sb.toString().trim();
	}
	
	/**
	 * 获取越南语单词
	 * @param values
	 * @return
	 */
	public static String getViWord(String[] values) {
		StringBuffer sb = new StringBuffer();
		int len = values.length;
        for (int i = 0; i< len; i++) {
        	if (i == 0) sb.append(values[i]);
        	else sb.append(" " + values[i]);
        }
		return sb.toString();
	}
	
	/**
	 * 将文本分为句子
	 * @param text 待分句子的文本
	 * @param regex 正则表达式
	 * @return
	 */
	public static String[] sentenceSegment(String text, String regex) {
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
	
	/**
	 * 将文本分为句子
	 * @param text 文本
	 * @return 句子
	 */
	public static String[] sentenceSegment(String text) {
		String regex="[.;!?។\r\n]";
		if (Config.language.equals(Language.Thai)) {
			regex += "|[ ]";
		}
		return sentenceSegment(text, regex);
	}
	
	/**
	 * 将异常转为字符串
	 *
	 * @param e
	 * @return
	 */
	public static String exceptionToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	  public static String join(String delimiter, Collection<String> stringCollection) {
        StringBuilder sb = new StringBuilder(stringCollection.size() * (16 + delimiter.length()));
        for (String str : stringCollection) {
            sb.append(str).append(delimiter);
        }
        return sb.toString();
    }
	  
}
