package cn.edu.kmust.seanlp.dictionary.stopword;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ListIterator;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.FileExtensions;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.io.IOUtil;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.util.ByteArray;
import cn.edu.kmust.seanlp.util.StringUtil;

/**
 * 核心停用词词典
 * 
 * @author Zhao Shiyu
 */
public class StopWordDictionary {
	static StopWord dictionary;
	static {
		ByteArray byteArray = ByteArray.createByteArray(Config.DictConf.dictionaryPath + Config.DictConf.stopWord + FileExtensions.BIN);
		if (byteArray == null) {
			try {
				dictionary = new StopWord(IOUtil.getInputStream(Config.DictConf.dictionaryPath + Config.DictConf.stopWord + FileExtensions.TXT));
				String binName = System.getProperty("user.dir") + "/src/main/resources" + Config.DictConf.dictionaryPath + Config.DictConf.stopWord + FileExtensions.BIN;
				DataOutputStream out = new DataOutputStream(new FileOutputStream(binName));
				Log.logger.info("开始缓存停用词：" + Config.DictConf.dictionaryPath + Config.DictConf.stopWord + FileExtensions.BIN);
				dictionary.save(out);
				out.close();
				Log.logger.info("停用词：" + Config.DictConf.dictionaryPath + Config.DictConf.stopWord +  FileExtensions.BIN + "缓存成功！" );
			} catch (Exception e) {
				System.err.println("载入停用词词典" + Config.DictConf.dictionaryPath + Config.DictConf.stopWord + FileExtensions.BIN + "失败" + StringUtil.exceptionToString(e));
			}
		} else {
			dictionary = new StopWord();
			dictionary.load(byteArray);
		}
	}

	public static boolean contains(String key) {
		return dictionary.contains(key);
	}

	/**
	 * 核心停用词典的核心过滤器，词性属于名词、动词、副词、形容词，并且不在停用词表中才不会被过滤
	 */
	public static Filter FILTER = new Filter() {
		public boolean shouldInclude(Term term) {
			// 除掉停用词
			String nature = term.getNature() != null ? term.getNature().toString() : "空";
			char firstChar = nature.charAt(0);
			switch (firstChar) {
			case 'm':
			case 'b':
			case 'c':
			case 'e':
			case 'o':
			case 'p':
			case 'q':
			case 'u':
			case 'y':
			case 'z':
			case 'r':
			case 'w': {
				return false;
			}
			default: {
				if (term.getWord().length() > 1
						&& !StopWordDictionary.contains(term.getWord())) {
					return true;
				}
			}
				break;
			}

			return false;
		}
	};

	/**
	 * 是否应当将这个term纳入计算
	 *
	 * @param term
	 * @return 是否应当
	 */
	public static boolean shouldInclude(Term term) {
		return FILTER.shouldInclude(term);
	}

	/**
	 * 是否应当去掉这个词
	 * 
	 * @param term
	 *            词
	 * @return 是否应当去掉
	 */
	public static boolean shouldRemove(Term term) {
		return !shouldInclude(term);
	}

	/**
	 * 加入停用词到停用词词典中
	 * 
	 * @param stopWord
	 *            停用词
	 * @return 词典是否发生了改变
	 */
	public static boolean add(String stopWord) {
		return dictionary.add(stopWord);
	}

	/**
	 * 从停用词词典中删除停用词
	 * 
	 * @param stopWord
	 *            停用词
	 * @return 词典是否发生了改变
	 */
	public static boolean remove(String stopWord) {
		return dictionary.remove(stopWord);
	}

	/**
	 * 对分词结果应用过滤
	 * 
	 * @param termList
	 */
	public static void apply(List<Term> termList) {
		ListIterator<Term> listIterator = termList.listIterator();
		while (listIterator.hasNext()) {
			if (shouldRemove(listIterator.next()))
				listIterator.remove();
		}
	}
}
