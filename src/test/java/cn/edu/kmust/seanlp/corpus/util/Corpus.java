package cn.edu.kmust.seanlp.corpus.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Corpus {
	
	/**
	 * 词性数目排序，冒泡排序(这里不考虑效率，简单实现一下排序)
	 * @param wnmap
	 * @return
	 */
	public static Map<String, String> NatureSort(Map<String, String> wnmap) {
		for (String key : wnmap.keySet()) {
			String n = wnmap.get(key);
			String[] value = n.split("\t");
			int len = value.length;
			if (len > 2) {
				String tempNum = ""; 
				String tempStr = ""; 
				for (int i = 1; i < len; i = i + 2) {
					for(int j = 1; j <len -1 - i; j = j + 2){
						int numj = Integer.parseInt(value[j]);
						int numjj = Integer.parseInt(value[j + 2]);
						if(numj < numjj){
							tempNum = value[j];
							tempStr = value[j - 1];
							value[j] = value[j + 2];
							value[j - 1] = value[j + 1];
							value[j + 2] = tempNum;
							value[j + 1] = tempStr;
						}
					}
				}
				String newNature = "";
				for (int k = 0; k < value.length; k++) {
					if (k == 0) {
						newNature = value[k];
					} else{
						newNature += "\t" + value[k];
					}
				}
				System.out.println(newNature);
				wnmap.put(key, newNature);
			}
			
		}
		return wnmap;
	}
	
	public static List<String> mapToList(Map<String, String> map) {
		List<String> rets = new ArrayList<String>();
		for (String key : map.keySet()) {
			rets.add(key + "\t" + map.get(key));
		}
		return rets;
	}

}
