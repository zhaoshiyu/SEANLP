package cn.edu.kmust.seanlp.dictionary.language;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.dictionary.CoreDictionary;

/**
 * 高棉语核心词典
 * 
 * @author Zhao Shiyu
 *
 */
public class KhmerCoreDictionary {

public static CoreDictionary khmerDictionary;

	// 自动加载词典
	static {
		long start = System.currentTimeMillis();
		khmerDictionary = CoreDictionary.loadCoreDictionary(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary);
		if (khmerDictionary == null) {
			System.err.printf("核心词典%s加载失败\n", Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary);
			System.exit(-1);
		} else {
			Log.logger.info(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary + "加载成功，" + khmerDictionary.dictionaryTrie.size() + "个词条，耗时"
					+ (System.currentTimeMillis() - start) + "ms");
		}
	}

}
