package cn.edu.kmust.seanlp.dictionary.language;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.dictionary.CoreDictionary;

/**
 * 越南语核心词典
 * 
 * @author Zhao Shiyu
 *
 */
public class VietnameseCoreDictionary {
	
	public static CoreDictionary vietnameseDictionary;
	
	// 自动加载词典
		static {
			long start = System.currentTimeMillis();
			vietnameseDictionary = CoreDictionary.loadCoreDictionary(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary);
			if (vietnameseDictionary == null) {
				System.err.printf("核心词典%s加载失败\n", Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary);
				System.exit(-1);
			} else {
				Log.logger.info(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.coreDictionary + "加载成功，" + vietnameseDictionary.dictionaryTrie.size() + "个词条，耗时"
						+ (System.currentTimeMillis() - start) + "ms");
			}
		}

}
