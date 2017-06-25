package cn.edu.kmust.seanlp.dictionary;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.TreeMap;

import cn.edu.kmust.io.IOUtil;
import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.Config.FileExtensions;
import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.collection.trie.DATrie;
import cn.edu.kmust.seanlp.util.ByteArray;

/**
 * 词典，可以是单列，也可以是双列
 * 
 * @author Zhao Shiyu
 *
 */
public class CommonDictionary implements Dictionary<CommonDictionary> {
	
	public DATrie<String> dictionaryTrie ;

	public static CommonDictionary loadTxtDictionary(String path, CommonDictionary commonDictionary) {
		Log.logger.info("核心词典开始加载:" + path);
		CommonDictionary dictionary = commonDictionary;
		if (dictionary.loadDat(ByteArray.createByteArray(path + FileExtensions.BIN)))
			return dictionary;
		TreeMap<String, String> map = new TreeMap<String, String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(IOUtil.getInputStream(path + FileExtensions.TXT),"UTF-8"));
			String line;
			long start = System.currentTimeMillis();
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (!line.matches("^###.*")) {
					String[] param = line.split("\t");
					if (param.length  == 2) {
						map.put(param[0], param[1]);
					} else {
						map.put(line, line);
					}
				}
			}
			Log.logger.info("核心词典读入词条：" + map.size()	+ "，耗时" + (System.currentTimeMillis() - start) + "ms");
			br.close();
			dictionary.dictionaryTrie = new DATrie<String>();
			dictionary.dictionaryTrie.build(map);
			Log.logger.info("核心词典加载成功:" + dictionary.dictionaryTrie.size() + "个词条，下面将写入缓存……");
			String binName = System.getProperty("user.dir") + "/src/main/resources" + path + FileExtensions.BIN;
			if (Config.DEBUG) {
				System.out.println(binName);
			}
			try {
				DataOutputStream out = new DataOutputStream(new FileOutputStream(binName));
				Collection<String> wordList = map.values();
				out.writeInt(wordList.size());
				for (String word : wordList) {
					out.writeUTF(word);
				}
				dictionary.dictionaryTrie.save(out);
				out.close();
			} catch (Exception e) {
				Log.logger.warning("保存失败" + e);
				return null;
			}
			//dictionary.dictionaryTrie.save(path + FileExtensions.BIN);
		} catch (FileNotFoundException e) {
			Log.logger.warning("核心词典" + path + "不存在！" + e);
			return null;
		} catch (IOException e) {
			Log.logger.warning("核心词典" + path + "读取错误！" + e);
			return null;
		}
		return dictionary;
	}
	
	public static CommonDictionary loadBinDictionary(String path) {
		ByteArray byteArray = ByteArray.createByteArray(path + FileExtensions.BIN);
        if (byteArray == null) return null;
        CommonDictionary dictionary = new CommonDictionary();
        if (dictionary.loadDat(byteArray)) return dictionary;
        return null;
	}

	@Override
	public boolean loadDat(ByteArray byteArray) {
		dictionaryTrie = new DATrie<String>();
		try {
			int size = byteArray.nextInt();
			String[] words = new String[size];
			for (int i = 0; i < size; ++i) {
				words[i] = byteArray.nextUTF();
			}
			if (!dictionaryTrie.load(byteArray, words) || byteArray.hasMore())
				return false;
		} catch (Exception e) {
			Log.logger.warning("读取失败，问题发生在" + e);
			return false;
		}
		return true;
	}
	
	public static CommonDictionary loadDictionary(String path) {
		CommonDictionary dictionary = loadBinDictionary(path);
        if (dictionary != null) return dictionary;
        return loadTxtDictionary(path, new CommonDictionary());
    }

	public static CommonDictionary loadCommonDictionary(String path) {
		return loadDictionary(path);
	}

}
