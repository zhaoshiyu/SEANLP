package cn.edu.kmust.seanlp.segmenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Language;

/**
 * 分词器基本配置<br>
 * 是所有分词器的基类（Abstract）<br>
 * 
 * @author  Zhao Shiyu
 *
 */
public abstract class AbstractSegmenter {
	/**
	 * 分词器配置
	 */
	protected Config config;

	/**
	 * 构造一个分词器
	 */
	public AbstractSegmenter() {
		config = new Config();
	}
	
	/**
	 * 将文本分为句子，在对句子进行分词
	 * @param text 文本
	 * @return 句子
	 */
	protected String[] sentenceSegment(String text) {
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
	
	/**
	 * 开启词性标注
	 * 
	 * @param enable
	 * @return
	 */
	public AbstractSegmenter enablePartOfSpeechTagging(boolean enable) {
		Config.BaseConf.speechTagging = enable;
		return this;
	}
	
}