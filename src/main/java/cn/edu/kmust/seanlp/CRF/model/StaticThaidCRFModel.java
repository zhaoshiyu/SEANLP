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
public class StaticThaidCRFModel {
	
	private static String thSyllableSegmentModelFile = Config.ModelConf.CRFModelPath + Config.language.toString() + ModelConf.syllableSegment + NGram.fivegram;
	private static String thSyllableMergeModelFile = Config.ModelConf.CRFModelPath + Config.language.toString() + ModelConf.syllableMerge + NGram.trigram;
	
	public static CRFModel crfThaiSyllableSegmentModel;
	public static CRFModel crfThaiSyllableMergeModel;
	
	static {
		Log.logger.info("正在加载泰语CRF模型：" + thSyllableSegmentModelFile
				+ "\n" + thSyllableMergeModelFile);
		long start = System.currentTimeMillis();
		crfThaiSyllableSegmentModel = CRFModel.loadCRFModel(thSyllableSegmentModelFile);
		crfThaiSyllableMergeModel = CRFModel.loadCRFModel(thSyllableMergeModelFile);
		if (crfThaiSyllableSegmentModel == null || crfThaiSyllableMergeModel == null) {
			Log.logger.severe("加载泰语CRF模型：" + thSyllableSegmentModelFile
					+ "\n" + thSyllableMergeModelFile
					+ " 失败，耗时 " + (System.currentTimeMillis() - start) + " ms");
			System.exit(-1);
		} else {
			Log.logger.info("加载泰语CRF模型：" + thSyllableSegmentModelFile
					+ "\n" + thSyllableMergeModelFile
					+ " 成功，耗时 " + (System.currentTimeMillis() - start) + " ms");
		}
	}
	
}
