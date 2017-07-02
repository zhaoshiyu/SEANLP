package cn.edu.kmust.seanlp.CRF.model;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.CRF.CRFModel;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.Config.ModelConf;

/**
 * 高棉语CRF静态模型
 * 
 * @author Zhao Shiyu
 *
 */
public class StaticKhmerCRFModel {
	
	private static String kmCRFModelFile = Config.ModelConf.CRFModelPath + Config.language.toString() + ModelConf.khWordSegment;
	
	public static CRFModel crfKhmerSegmentModel;
	
	static {
		Log.logger.info("正在加载高棉语CRF模型：" + kmCRFModelFile);
		long start = System.currentTimeMillis();
		crfKhmerSegmentModel = CRFModel.loadCRFModel(kmCRFModelFile);
		if (crfKhmerSegmentModel == null) {
			Log.logger.severe("加载高棉语CRF模型：" + kmCRFModelFile
					+ " 失败，耗时 " + (System.currentTimeMillis() - start) + " ms");
			System.exit(-1);
		} else {
			Log.logger.info("加载高棉语CRF模型：" + kmCRFModelFile
					+ " 成功，耗时 " + (System.currentTimeMillis() - start) + " ms");
		}
	}

}
