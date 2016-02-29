package cn.edu.kmust.seanlp.dictionary.language;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.dictionary.CoreDictionary;

/**
 * 泰语核心词典
 * 
 * @author  Zhao Shiyu
 *
 */
public class ThaiCoreDictionary {
	
	public static CoreDictionary thaiDictionary;
	
	// 自动加载词典
	static {
		long start = System.currentTimeMillis();
		thaiDictionary = CoreDictionary.loadCoreDictionary(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary);
		if (thaiDictionary == null) {
			System.err.printf("核心词典%s加载失败\n", Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary);
			System.exit(-1);
		} else {
			Log.logger.info(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary + "加载成功，" + thaiDictionary.dictionaryTrie.size() + "个词条，耗时"
					+ (System.currentTimeMillis() - start) + "ms");
		}
	}
	
}
