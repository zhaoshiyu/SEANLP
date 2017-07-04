package cn.edu.kmust.seanlp.CRF.model;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.CRF.CRFModel;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.Config.ModelConf;
import cn.edu.kmust.seanlp.Config.NGram;

/**
 * 泰语静态CRF分词模型
 * @author Zhao Shiyu
 *
 */
public class StaticThaigCRFModel {
	
	private static String thCRFWordSegmentModelFile = Config.ModelConf.CRFModelPath + Config.language.toString() + ModelConf.wordSegment + NGram.sevengram;
	
	public static CRFModel crfThaiWordSegmentModel;
	
	static {
		Log.logger.info("正在加载泰语CRF模型：" + thCRFWordSegmentModelFile);
		long start = System.currentTimeMillis();
		crfThaiWordSegmentModel = CRFModel.loadCRFModel(thCRFWordSegmentModelFile);
		if (crfThaiWordSegmentModel == null) {
			Log.logger.severe("加载泰语CRF模型：" + thCRFWordSegmentModelFile
					+ " 失败，耗时 " + (System.currentTimeMillis() - start) + " ms");
			System.exit(-1);
		} else {
			Log.logger.info("加载泰语CRF模型：" + thCRFWordSegmentModelFile
					+ " 成功，耗时 " + (System.currentTimeMillis() - start) + " ms");
		}
	}
	
}
