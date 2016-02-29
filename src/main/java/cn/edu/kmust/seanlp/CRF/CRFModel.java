package cn.edu.kmust.seanlp.CRF;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import cn.edu.kmust.seanlp.Config.FileExtensions;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.collection.trie.DATrie;
import cn.edu.kmust.seanlp.io.IOUtil;
import cn.edu.kmust.seanlp.util.ByteArray;
import cn.edu.kmust.seanlp.util.ICacheAble;
import cn.edu.kmust.seanlp.util.StringUtil;

public class CRFModel implements ICacheAble {
	/**
	 * 标签和id的相互转换
	 */
	Map<String, Integer> tag2id;
	protected String[] id2tag;
	DATrie<FeatureFunction> featureFunctionTrie;
	List<FeatureTemplate> featureTemplateList;
	/**
	 * tag的转移矩阵
	 */
	protected double[][] matrix;

	public CRFModel() {
	}
	
	/**
	 * 加载二进制模型
	 * @param path
	 * @return
	 */
	public static CRFModel loadBinModel(String path) {
		ByteArray byteArray = ByteArray.createByteArray(path + FileExtensions.BIN);
        if (byteArray == null) return null;
        CRFModel model = new CRFModel();
        if (model.load(byteArray)) return model;
        return null;
	}
	
	/**
	 * 加载压缩的二进制模型
	 * @param path
	 * @return
	 */
	public static CRFModel loadGzModel(String path) {
		ByteArray byteArray = ByteArray.createByteArrayByGz(path + FileExtensions.GZ);
        if (byteArray == null) return null;
        CRFModel model = new CRFModel();
        if (model.load(byteArray)) return model;
        return null;
	}

	/**
	 * 加载文本文件模型
	 * @param path
	 * @param model
	 * @return
	 */
	public static CRFModel loadTxtModel(String path, CRFModel model) {

		CRFModel CRFModel = model;
		// 先尝试从bin加载
		if (CRFModel.load(ByteArray.createByteArrayByGz(path + FileExtensions.GZ)))
			return CRFModel;
//		IOUtil.LineIterator lineIterator = new IOUtil.LineIterator(path + FileExtensions.TXT);
		InputStream is = IOUtil.getInputStream(path + FileExtensions.TXT);
		IOUtil.LineIterator lineIterator = new IOUtil.LineIterator(is);
		if (!lineIterator.hasNext())
			return null;
		Log.logger.info(lineIterator.next()); // verson
		Log.logger.info(lineIterator.next()); // cost-factor
		@SuppressWarnings("unused")
		int maxid = Integer.parseInt(lineIterator.next()
				.substring("maxid:".length()).trim());
		Log.logger.info(lineIterator.next()); // xsize
		lineIterator.next(); // blank
		String line;
		int id = 0;
		CRFModel.tag2id = new HashMap<String, Integer>();
		while ((line = lineIterator.next()).length() != 0) {
			CRFModel.tag2id.put(line, id);
			++id;
		}
		CRFModel.id2tag = new String[CRFModel.tag2id.size()];
		final int size = CRFModel.id2tag.length;
		for (Map.Entry<String, Integer> entry : CRFModel.tag2id.entrySet()) {
			CRFModel.id2tag[entry.getValue()] = entry.getKey();
		}
		TreeMap<String, FeatureFunction> featureFunctionMap = new TreeMap<String, FeatureFunction>(); // 构建trie树的时候用
		List<FeatureFunction> featureFunctionList = new LinkedList<FeatureFunction>(); // 读取权值的时候用
		CRFModel.featureTemplateList = new LinkedList<FeatureTemplate>();
		while ((line = lineIterator.next()).length() != 0) {
			if (!"B".equals(line)) {
				FeatureTemplate featureTemplate = FeatureTemplate.create(line);
				CRFModel.featureTemplateList.add(featureTemplate);
			} else {
				CRFModel.matrix = new double[size][size];
			}
		}

		if (CRFModel.matrix != null) {
			lineIterator.next(); // 0 B
		}

		while ((line = lineIterator.next()).length() != 0) {
			String[] args = line.split(" ", 2);
			char[] charArray = args[1].toCharArray();
			FeatureFunction featureFunction = new FeatureFunction(charArray,
					size);
			featureFunctionMap.put(args[1], featureFunction);
			featureFunctionList.add(featureFunction);
		}

		if (CRFModel.matrix != null) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					CRFModel.matrix[i][j] = Double.parseDouble(lineIterator
							.next());
				}
			}
		}

		for (FeatureFunction featureFunction : featureFunctionList) {
			for (int i = 0; i < size; i++) {
				featureFunction.w[i] = Double.parseDouble(lineIterator.next());
			}
		}
		if (lineIterator.hasNext()) {
			Log.logger.warning("文本读取有残留，可能会出问题！" + path);
		}
		lineIterator.close();
		Log.logger.info("开始构建双数组trie树");
		CRFModel.featureFunctionTrie = new DATrie<FeatureFunction>();
		CRFModel.featureFunctionTrie.build(featureFunctionMap);
		// 缓存gz
		String gzName = System.getProperty("user.dir") + "/src/main/resources" + path + FileExtensions.GZ;
		try {
			Log.logger.info("开始缓存" + gzName);
//			DataOutputStream out = new DataOutputStream(new FileOutputStream(path + FileExtensions.BIN));
//			CRFModel.save(out);
//			out.close();
			
			DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(gzName)));
			CRFModel.save(out);
			out.close();
		} catch (Exception e) {
			Log.logger.warning("在缓存" + path + FileExtensions.BIN + "时发生错误" + StringUtil.exceptionToString(e));
		}
		return CRFModel;
	}

	/**
	 * 维特比后向算法标注
	 *
	 * @param table
	 */
	public void tag(Table table) {
		int size = table.size();
		if (size == 1) {
			table.setLast(0, "S");
			return;
		}
		double bestScore = 0;
		int bestTag = 0;
		int tagSize = id2tag.length;
		LinkedList<double[]> scoreList = computeScoreList(table, 0); // 0位置命中的特征函数
		// 0位置只可能是B或者S
		{
			bestScore = computeScore(scoreList, 0);
			bestTag = 0;
			double curScore = computeScore(scoreList, 3);
			if (curScore > bestScore) {
				bestTag = 3;
			}
		}
		table.setLast(0, id2tag[bestTag]);
		int preTag = bestTag;
		// 0位置打分完毕，接下来打剩下的
		for (int i = 1; i < size - 1; ++i) {
			scoreList = computeScoreList(table, i); // i位置命中的特征函数
			bestScore = Double.MIN_VALUE;
			for (int j = 0; j < tagSize; ++j) // i位置的标签遍历
			{
				double curScore = computeScore(scoreList, j);
				if (matrix != null) {
					curScore += matrix[preTag][j];
				}
				if (curScore > bestScore) {
					bestScore = curScore;
					bestTag = j;
				}
			}
			table.setLast(i, id2tag[bestTag]);
			preTag = bestTag;
		}
		// size - 1位置只可能是E或者S，其实从最终合并逻辑上看，S就足够
		table.setLast(size - 1, "S");
		// {
		// bestScore = computeScore(scoreList, 1);
		// bestTag = 0;
		// double curScore = computeScore(scoreList, 3);
		// if (curScore > bestScore) {
		// bestTag = 3;
		// }
		// }
		// table.setLast(size - 1, id2tag[bestTag]);
	}

	public LinkedList<double[]> computeScoreList(Table table, int current) {
		LinkedList<double[]> scoreList = new LinkedList<double[]>();
		for (FeatureTemplate featureTemplate : featureTemplateList) {
			char[] o = featureTemplate.generateParameter(table, current);
			FeatureFunction featureFunction = featureFunctionTrie.get(o);
			if (featureFunction == null)
				continue;
			scoreList.add(featureFunction.w);
		}

		return scoreList;
	}

	/**
	 * 给一系列特征函数结合tag打分
	 *
	 * @param scoreList
	 * @param tag
	 * @return
	 */
	protected static double computeScore(LinkedList<double[]> scoreList, int tag) {
		double score = 0;
		for (double[] w : scoreList) {
			score += w[tag];
		}
		return score;
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(id2tag.length);
		for (String tag : id2tag) {
			out.writeUTF(tag);
		}
		FeatureFunction[] valueArray = featureFunctionTrie
				.getValueArray(new FeatureFunction[0]);
		out.writeInt(valueArray.length);
		for (FeatureFunction featureFunction : valueArray) {
			featureFunction.save(out);
		}
		featureFunctionTrie.save(out);
		out.writeInt(featureTemplateList.size());
		for (FeatureTemplate featureTemplate : featureTemplateList) {
			featureTemplate.save(out);
		}
		if (matrix != null) {
			out.writeInt(matrix.length);
			for (double[] line : matrix) {
				for (double v : line) {
					out.writeDouble(v);
				}
			}
		} else {
			out.writeInt(0);
		}
	}
	
	public void save(ObjectOutputStream oos) throws Exception {
		oos.writeInt(id2tag.length);
		for (String tag : id2tag) {
			oos.writeUTF(tag);
		}
		FeatureFunction[] valueArray = featureFunctionTrie.getValueArray(new FeatureFunction[0]);
		oos.writeInt(valueArray.length);
		for (FeatureFunction featureFunction : valueArray) {
			featureFunction.save(oos);
		}
		featureFunctionTrie.save(oos);
		oos.writeInt(featureTemplateList.size());
		for (FeatureTemplate featureTemplate : featureTemplateList) {
			featureTemplate.save(oos);
		}
		if (matrix != null) {
			oos.writeInt(matrix.length);
			for (double[] line : matrix) {
				for (double v : line) {
					oos.writeDouble(v);
				}
			}
		} else {
			oos.writeInt(0);
		}
	}
	
	public boolean load(ByteArray byteArray) {
		if (byteArray == null)
			return false;
		int size = byteArray.nextInt();
		id2tag = new String[size];
		tag2id = new HashMap<String, Integer>(size);
		for (int i = 0; i < id2tag.length; i++) {
			id2tag[i] = byteArray.nextUTF();
			tag2id.put(id2tag[i], i);
		}
		FeatureFunction[] valueArray = new FeatureFunction[byteArray.nextInt()];
		for (int i = 0; i < valueArray.length; i++) {
			valueArray[i] = new FeatureFunction();
			valueArray[i].load(byteArray);
		}
		featureFunctionTrie = new DATrie<FeatureFunction>();
		featureFunctionTrie.load(byteArray, valueArray);
		size = byteArray.nextInt();
		featureTemplateList = new ArrayList<FeatureTemplate>(size);
		for (int i = 0; i < size; ++i) {
			FeatureTemplate featureTemplate = new FeatureTemplate();
			featureTemplate.load(byteArray);
			featureTemplateList.add(featureTemplate);
		}
		size = byteArray.nextInt();
		if (size == 0)
			return true;
		matrix = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				matrix[i][j] = byteArray.nextDouble();
			}
		}

		return true;
	}
	
	
	 public static CRFModel loadModel(String path) {
//	        CRFModel model = loadBinModel(path);
	        CRFModel model = loadGzModel(path);
	        
	        if (model != null) return model;
	        return loadTxtModel(path, new CRFModel());
	    }

	public static CRFModel loadCRFModel(String path) {
		return loadModel(path);
	}
	
}
