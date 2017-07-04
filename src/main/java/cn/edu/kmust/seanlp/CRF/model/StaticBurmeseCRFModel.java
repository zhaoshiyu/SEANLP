package cn.edu.kmust.seanlp.CRF.model;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.CRF.CRFModel;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.Config.ModelConf;
import cn.edu.kmust.seanlp.Config.NGram;

/**
 * 缅甸语CRF静态模型
 * 
 * @author Zhao Shiyu
 *
 */
public class StaticBurmeseCRFModel {
	
	private static String buCRFModelFile = Config.ModelConf.CRFModelPath + Config.language.toString() + ModelConf.wordSegment + NGram.sevengram;
	
	public static CRFModel crfBurmeseSegmentModel;
	
	static {
		Log.logger.info("正在加载缅甸语CRF模型：" + buCRFModelFile);
		long start = System.currentTimeMillis();
		crfBurmeseSegmentModel = CRFModel.loadCRFModel(buCRFModelFile);
		if (crfBurmeseSegmentModel == null) {
			Log.logger.severe("加载缅甸语CRF模型：" + buCRFModelFile
					+ " 失败，耗时 " + (System.currentTimeMillis() - start) + " ms");
			System.exit(-1);
		} else {
			Log.logger.info("加载缅甸语CRF模型：" + buCRFModelFile
					+ " 成功，耗时 " + (System.currentTimeMillis() - start) + " ms");
		}
	}

}
