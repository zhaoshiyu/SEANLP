package cn.edu.kmust.seanlp.dictionary.language;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.dictionary.NatureTransitionMatrix;
import cn.edu.kmust.seanlp.segmenter.domain.Nature;

/**
 * 泰语词性转移矩阵
 * 
 * @author Zhao Shiyu
 *
 */
public class ThaiNatureTransMatrix {
	
public static NatureTransitionMatrix<Nature> thaiTransMatrix;
	
	static {
		thaiTransMatrix = new NatureTransitionMatrix<Nature>(Nature.class);
		long start = System.currentTimeMillis();
		if (!thaiTransMatrix.load(Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.natureTransitionMatrix)) {
			System.err.println("加载核心词典词性转移矩阵" + Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.natureTransitionMatrix + "失败");
			System.exit(-1);
		} else {
			Log.logger.info("加载核心词典词性转移矩阵" + Config.DictConf.dictionaryPath + Config.language.toString() + Config.DictConf.natureTransitionMatrix + "成功，耗时：" + (System.currentTimeMillis() - start) + " ms");
		}
	}

}
