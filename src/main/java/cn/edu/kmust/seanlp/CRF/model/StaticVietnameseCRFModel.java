package cn.edu.kmust.seanlp.CRF.model;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.CRF.CRFModel;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.Config.ModelConf;

/**
 * 越南语静态CRF分词模型
 * @author Zhao Shiyu
 *
 */
public class StaticVietnameseCRFModel {
	
	private final static String viCRFModelFile = Config.ModelConf.CRFModelPath + Config.language.toString() + ModelConf.viWordSegment;
	
	public static CRFModel crfVietnameseSegmentModel;
	
	static {
		Log.logger.info("正在加载越南语CRF模型：" + viCRFModelFile);
		long start = System.currentTimeMillis();
		crfVietnameseSegmentModel = CRFModel.loadCRFModel(viCRFModelFile);
		if (crfVietnameseSegmentModel == null) {
			Log.logger.severe("加载越南语CRF模型：" + viCRFModelFile
					+ " 失败，耗时 " + (System.currentTimeMillis() - start) + " ms");
			System.exit(-1);
		} else {
			Log.logger.info("加载越南语CRF模型：" + viCRFModelFile
					+ " 成功，耗时 " + (System.currentTimeMillis() - start) + " ms");
		}
	}

}
