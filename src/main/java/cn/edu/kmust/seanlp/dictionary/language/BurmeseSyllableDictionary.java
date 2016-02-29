package cn.edu.kmust.seanlp.dictionary.language;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.dictionary.CommonDictionary;

/**
 * 缅甸语音节词典
 * 
 * @author  Zhao Shiyu
 *
 */
public class BurmeseSyllableDictionary {
	
public static CommonDictionary burmeseSyllableDictionary;
	
	// 自动加载词典
	static {
		long start = System.currentTimeMillis();
		burmeseSyllableDictionary = CommonDictionary.loadCommonDictionary(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.syllableDictionary);
		if (burmeseSyllableDictionary == null) {
			System.err.printf("核心词典%s加载失败\n", Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.syllableDictionary);
			System.exit(-1);
		} else {
			Log.logger.info(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.syllableDictionary + "加载成功，" + burmeseSyllableDictionary.dictionaryTrie.size() + "个词条，耗时" + (System.currentTimeMillis() - start) + "ms");
		}
	}

}
