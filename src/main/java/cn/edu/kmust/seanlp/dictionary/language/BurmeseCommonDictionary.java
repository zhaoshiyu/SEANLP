package cn.edu.kmust.seanlp.dictionary.language;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.dictionary.CommonDictionary;

/**
 * 缅甸语词典
 * 
 * @author  Zhao Shiyu
 *
 */
public class BurmeseCommonDictionary {
	
public static CommonDictionary burmeseDictionary;
	
	// 自动加载词典
	static {
		long start = System.currentTimeMillis();
		burmeseDictionary = CommonDictionary.loadCommonDictionary(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.commonDictionary);
		if (burmeseDictionary == null) {
			System.err.printf("核心词典%s加载失败\n", Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.commonDictionary);
			System.exit(-1);
		} else {
			Log.logger.info(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.commonDictionary + "加载成功，" + burmeseDictionary.dictionaryTrie.size() + "个词条，耗时" + (System.currentTimeMillis() - start) + "ms");
		}
	}

}
