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
import cn.edu.kmust.seanlp.dictionary.Dictionary;
import cn.edu.kmust.seanlp.segmenter.domain.Nature;
import cn.edu.kmust.seanlp.util.ByteArray;
import cn.edu.kmust.seanlp.util.StringUtil;

/**
 * 使用DoubleArrayTrie实现的核心词典
 * 
 * @author  Zhao Shiyu
 *
 */
public class CoreDictionary implements Dictionary<CoreDictionary> {
	public static int totalFrequency = 221894;
	
	public DATrie<Attribute> dictionaryTrie;

	public static CoreDictionary loadTxtDictionary(String path, CoreDictionary coreDictionary) {
		Log.logger.info("核心词典开始加载:" + path);
		CoreDictionary dictionary = coreDictionary;
		if (dictionary.loadDat(ByteArray.createByteArray(path + FileExtensions.BIN)))
			return dictionary;
		TreeMap<String, CoreDictionary.Attribute> map = new TreeMap<String, Attribute>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(IOUtil.getInputStream(path + FileExtensions.TXT),"UTF-8"));
			String line;
			int MAX_FREQUENCY = 0;
			long start = System.currentTimeMillis();
			while ((line = br.readLine()) != null) {
				String param[] = line.split("\t");
				int natureCount = (param.length - 1) / 2;
				CoreDictionary.Attribute attribute = new CoreDictionary.Attribute(natureCount);
				for (int i = 0; i < natureCount; ++i) {
					attribute.nature[i] = Enum.valueOf(Nature.class,param[1 + 2 * i]);
					attribute.frequency[i] = Integer.parseInt(param[2 + 2 * i]);
					attribute.totalFrequency += attribute.frequency[i];
				}
				map.put(param[0], attribute);
				MAX_FREQUENCY += attribute.totalFrequency;
			}
			Log.logger.info("核心词典读入词条" + map.size() + " 全部频次" + MAX_FREQUENCY
					+ "，耗时" + (System.currentTimeMillis() - start) + "ms");
			br.close();
			dictionary.dictionaryTrie = new DATrie<Attribute>();
			dictionary.dictionaryTrie.build(map);
			Log.logger.info("核心词典加载成功:" + dictionary.dictionaryTrie.size() + "个词条，下面将写入缓存……");
			String binName = System.getProperty("user.dir") + "/src/main/resources" + path + FileExtensions.BIN;
			if (Config.DEBUG) {
				System.out.println(binName);
			}
			try {
				DataOutputStream out = new DataOutputStream(new FileOutputStream(binName));
				Collection<CoreDictionary.Attribute> attributeList = map.values();
				out.writeInt(attributeList.size());
				for (CoreDictionary.Attribute attribute : attributeList) {
					out.writeInt(attribute.totalFrequency);
					out.writeInt(attribute.nature.length);
					for (int i = 0; i < attribute.nature.length; ++i) {
						out.writeInt(attribute.nature[i].ordinal());
						out.writeInt(attribute.frequency[i]);
					}
				}
				dictionary.dictionaryTrie.save(out);
				out.close();
			} catch (Exception e) {
				Log.logger.warning("保存失败" + e);
				return null;
			}
		} catch (FileNotFoundException e) {
			Log.logger.warning("核心词典" + path + "不存在！" + e);
			return null;
		} catch (IOException e) {
			Log.logger.warning("核心词典" + path + "读取错误！" + e);
			return null;
		}
		return dictionary;
	}

	public static CoreDictionary loadBinDictionary(String path) {
		ByteArray byteArray = ByteArray.createByteArray(path + FileExtensions.BIN);
        if (byteArray == null) return null;
        CoreDictionary dictionary = new CoreDictionary();
        if (dictionary.loadDat(byteArray)) return dictionary;
        return null;
	}

	/**
	 * 从ByteArrays加载双数组
	 *
	 * @param path
	 * @return
	 */
	public boolean loadDat(ByteArray byteArray) {
		dictionaryTrie = new DATrie<Attribute>();
		try {
			int size = byteArray.nextInt();
			CoreDictionary.Attribute[] attributes = new CoreDictionary.Attribute[size];
			final Nature[] natureIndexArray = Nature.values();
			for (int i = 0; i < size; ++i) {
				// 第一个是全部频次，第二个是词性个数
				int currentTotalFrequency = byteArray.nextInt();
				int length = byteArray.nextInt();
				attributes[i] = new CoreDictionary.Attribute(length);
				attributes[i].totalFrequency = currentTotalFrequency;
				for (int j = 0; j < length; ++j) {
					attributes[i].nature[j] = natureIndexArray[byteArray.nextInt()];
					attributes[i].frequency[j] = byteArray.nextInt();
				}
			}
			if (!dictionaryTrie.load(byteArray, attributes) || byteArray.hasMore())
				return false;
		} catch (Exception e) {
			Log.logger.warning("读取失败，问题发生在" + e);
			return false;
		}
		return true;
	}

	/**
	 * 获取条目
	 * 
	 * @param key
	 * @return
	 */
	public static Attribute get(DATrie<Attribute> dictionaryTrie, String key) {
		return dictionaryTrie.get(key);
	}

	/**
	 * 获取条目
	 * 
	 * @param wordID
	 * @return
	 */
	public static Attribute get(DATrie<Attribute> dictionaryTrie, int wordID) {
		return dictionaryTrie.getValue(wordID);
	}

	/**
	 * 获取词频
	 *
	 * @param term
	 * @return
	 */
	public static int getTermFrequency(DATrie<Attribute> dictionaryTrie, String term) {
		Attribute attribute = get(dictionaryTrie, term);
		if (attribute == null)
			return 0;
		return attribute.totalFrequency;
	}

	/**
	 * 是否包含词语
	 * 
	 * @param key
	 * @return
	 */
	public static boolean contains(DATrie<Attribute> dictionaryTrie, String key) {
		return dictionaryTrie.get(key) != null;
	}

	/**
	 * 核心词典中的词属性
	 */
	public static class Attribute {
		/**
		 * 词性列表
		 */
		public Nature nature[];
		/**
		 * 词性对应的词频
		 */
		public int frequency[];

		public int totalFrequency;

		public Attribute(int size) {
			nature = new Nature[size];
			frequency = new int[size];
		}

		public Attribute(Nature[] nature, int[] frequency) {
			this.nature = nature;
			this.frequency = frequency;
		}

		public Attribute(Nature nature, int frequency) {
			this(1);
			this.nature[0] = nature;
			this.frequency[0] = frequency;
			totalFrequency = frequency;
		}

		public Attribute(Nature[] nature, int[] frequency, int totalFrequency) {
			this.nature = nature;
			this.frequency = frequency;
			this.totalFrequency = totalFrequency;
		}

		/**
		 * 使用单个词性，默认词频1000构造
		 *
		 * @param nature
		 */
		public Attribute(Nature nature) {
			this(nature, 1000);
		}

		public static Attribute create(String natureWithFrequency) {
			try {
				String param[] = natureWithFrequency.split(" ");
				int natureCount = param.length / 2;
				Attribute attribute = new Attribute(natureCount);
				for (int i = 0; i < natureCount; ++i) {
					attribute.nature[i] = Enum.valueOf(Nature.class,
							param[2 * i]);
					attribute.frequency[i] = Integer.parseInt(param[1 + 2 * i]);
					attribute.totalFrequency += attribute.frequency[i];
				}
				return attribute;
			} catch (Exception e) {
				Log.logger.warning("使用字符串" + natureWithFrequency + "创建词条属性失败！" + StringUtil.exceptionToString(e));
				return null;
			}
		}

		/**
		 * 获取词性的词频
		 *
		 * @param nature
		 *            字符串词性
		 * @return 词频
		 * @deprecated 推荐使用Nature参数！
		 */
		public int getNatureFrequency(String nature) {
			try {
				Nature pos = Enum.valueOf(Nature.class, nature);
				return getNatureFrequency(pos);
			} catch (IllegalArgumentException e) {
				return 0;
			}
		}

		/**
		 * 获取词性的词频
		 *
		 * @param nature
		 *            词性
		 * @return 词频
		 */
		public int getNatureFrequency(final Nature nature) {
			int result = 0;
			int i = 0;
			for (Nature pos : this.nature) {
				if (nature == pos) {
					return frequency[i];
				}
				++i;
			}
			return result;
		}

		/**
		 * 是否有某个词性
		 * 
		 * @param nature
		 * @return
		 */
		public boolean hasNature(Nature nature) {
			return getNatureFrequency(nature) > 0;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < nature.length; ++i) {
				sb.append(nature[i]).append(' ').append(frequency[i])
						.append(' ');
			}
			return sb.toString();
		}
	}

	/**
	 * 获取词语的ID
	 * 
	 * @param a
	 * @return
	 */
	public static int getWordID(DATrie<CoreDictionary.Attribute> dictionaryTrie, String key) {
		return dictionaryTrie.exactMatchSearch(key);
	}
	
	public static CoreDictionary loadDictionary(String path) {
		CoreDictionary dictionary = loadBinDictionary(path);
        if (dictionary != null) return dictionary;
        return loadTxtDictionary(path, new CoreDictionary());
    }

	public static CoreDictionary loadCoreDictionary(String path) {
		return loadDictionary(path);
	}
}
