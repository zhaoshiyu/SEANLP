package cn.edu.kmust.seanlp.corpus.Thai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.edu.kmust.io.IOUtil;

/**
 * 
 * @author  Zhao Shiyu
 *
 */
public class Lexitron {
	
	public static Map<String,String> telex(String telexFile) {
		//String fileName = "data/corpus/lexitron-data/telex.utf-8";
		List<String>  lines = IOUtil.readLines(telexFile);
		boolean flag = false;
		String tsearch = "", tentry = "", eentry = "", tcat = "", tsyn = "", tsample = "", id = "";
		Map<String,String> map = new HashMap<String, String>();
		Map<String,String> treemap = new TreeMap<String, String>();
		Map<String,Integer> ne = new TreeMap<String, Integer>();
		Map<String,String> diff = new TreeMap<String, String>();
		int count = 0;
		for (String line : lines) {
			if (line.equals("<Doc>")) {
				tsearch = "";
				tentry = "";
				eentry = "";
				tcat = "";
				tsyn = "";
				tsample = "";
				id = "";
				flag = true;
			}
			if (line.matches("<tsearch>.*</tsearch>") && flag) {
				tsearch = line.replace("<tsearch>", "").replace("</tsearch>", "").trim().split(" ")[0];
			}
			if (line.matches("<tentry>.*</tentry>") && flag) {
				tentry = line.replace("<tentry>", "").replace("</tentry>", "").trim().split(" ")[0];
			}
			if (line.matches("<eentry>.*</eentry>") && flag) {
				eentry = line.replace("<eentry>", "").replace("</eentry>", "").trim();
			}
			if (line.matches("<tcat>.*</tcat>") && flag) {
				tcat = line.replace("<tcat>", "").replace("</tcat>", "").trim().toLowerCase();
			}
			if (line.matches("<tsyn>.*</tsyn>") && flag) {
				tsyn = line.replace("<tsyn>", "").replace("</tsyn>", "").trim();
			}
			if (line.matches("<tsample>.*</tsample>") && flag) {
				tsample = line.replace("<tsample>", "").replace("</tsample>", "").trim();
			}
			if (line.matches("<id>.*</id>") && flag) {
				id = line.replace("<id>", "").replace("</id>", "").trim();
			}
			if (line.equals("</Doc>") && flag && !id.trim().isEmpty() && !tentry.trim().isEmpty() && !tcat.trim().isEmpty() && tcat.matches("[a-zA-Z &]*")) { 
				
				//System.out.println(tsearch + " : " + tentry + " : " + eentry + " : " + tcat + " : " + tsyn + " : " + tsample + " : " + id );
				
				map.put(tsearch, tcat);
				map.put(tentry, tcat);
				if (tsearch.equals(tentry)) {
					if (treemap.get(tentry) != null) {
						String[] cats = treemap.get(tentry).split("\t");
						boolean catFlag = false;
						for (String cat : cats) {
							if (tcat.equals(cat)) {
								catFlag = true;
							}
						}
						if (!catFlag) {
							treemap.put(tentry, treemap.get(tentry) + "\t" + tcat + "\t" + "1");
						}
					} else {
						treemap.put(tentry, tcat + "\t" + "1");
					}
				} else {
					if (treemap.get(tentry) != null) {
						String[] cats = treemap.get(tentry).split("\t");
						boolean catFlag = false;
						for (String cat : cats) {
							if (tcat.equals(cat)) {
								catFlag = true;
							}
						}
						if (!catFlag) {
							treemap.put(tentry, treemap.get(tentry) + "\t" + tcat + "\t" + "1");
						}
					} else {
						treemap.put(tentry, tcat + "\t" + "1");
					}
					
					if (treemap.get(tsearch) != null) {
						String[] cats = treemap.get(tsearch).split("\t");
						boolean catFlag = false;
						for (String cat : cats) {
							if (tcat.equals(cat)) {
								catFlag = true;
							}
						}
						if (!catFlag) {
							treemap.put(tsearch, treemap.get(tsearch) + "\t" + tcat + "\t" + "1");
						}
					} else {
						treemap.put(tsearch, tcat + "\t" + "1");
					}
				}
				if (ne.get(tcat) != null) {
					ne.put(tcat, ne.get(tcat) + 1);
				} else {
					ne.put(tcat, 1);
				}
				
				if (tsearch.equals(tentry)) {
					count++;
				} else {
					diff.put(tsearch, tentry);
				}
				
				/*
				if (tcat.matches(".*[ &]+.*")) {
					System.out.println(tsearch + " : " + tentry + " : " + eentry + " : " + tcat + " : " + tsyn + " : " + tsample + " : " + id );
				}
				if (tcat.trim().isEmpty()) {
					System.err.println(tsearch + " : " + tentry + " : " + eentry + " : " + tcat + " : " + tsyn + " : " + tsample + " : " + id );
				}
				System.out.println(tsearch + " : " + tentry + " : " + eentry + " : " + tcat + " : " + tsyn + " : " + tsample + " : " + id );
				*/
				tsearch = "";
				tentry = "";
				eentry = "";
				tcat = "";
				tsyn = "";
				tsample = "";
				id = "";
				flag = false;
			}
		}
//		for (String key : map.keySet()) {
//			System.out.println("key = " + key + "; value = " + map.get(key));
//		}
//		
		
		int cc = 0;
		for (Map.Entry<String, Integer> entry : ne.entrySet()) {
			System.out.println("key = " + entry.getKey() + "; value = " + entry.getValue());
			cc += entry.getValue();
		}
		System.err.println("count = " + count + "cc = " + cc);
		
		for (Map.Entry<String, String> entry : diff.entrySet()) {
			System.out.println("key = " + entry.getKey() + "; value = " + entry.getValue());
		}
		System.out.println("map = " + map.size() +"; treemap = " + treemap.size() + "; diff = " + diff.size());
		return treemap;
	}
	
	public static List<String> mapToList(Map<String, String> map) {
		List<String> rets = new ArrayList<String>(map.size());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.err.println("key = " + entry.getKey() + "; value = " + entry.getValue());
			rets.add(entry.getKey() + "\t" + entry.getValue());
		}
		//IOUtil.writeLines("data/corpus/lexitron-data/thai-dictionary.txt", rets);
		return rets;
	}
	
	public static Map<String,String> etlex(String etlexFile) {
		//String fileName = "data/corpus/lexitron-data/telex.utf-8";
		IOUtil io = new IOUtil();
		List<String>  lines = io.readLines(etlexFile);
		boolean flag = false;
		String esearch = "", tentry = "", eentry = "", ecat = "", tsyn = "", tsample = "",ethai="" , id = "";
		Map<String,String> map = new HashMap<String, String>();
		Map<String,String> treemap = new TreeMap<String, String>();
		Map<String,Integer> ne = new TreeMap<String, Integer>();
		Map<String,String> diff = new TreeMap<String, String>();
		int count = 0;
		for (String line : lines) {
			if (line.equals("<Doc>")) {
				esearch = "";
				tentry = "";
				eentry = "";
				ecat = "";
				tsyn = "";
				tsample = "";
				id = "";
				flag = true;
			}
			if (line.matches("<esearch>.*</esearch>") && flag) {
				esearch = line.replace("<esearch>", "").replace("</esearch>", "").trim().split(" ")[0];
			}
			if (line.matches("<tentry>.*</tentry>") && flag) {
				tentry = line.replace("<tentry>", "").replace("</tentry>", "").trim().split(" ")[0];
			}
			if (line.matches("<eentry>.*</eentry>") && flag) {
				eentry = line.replace("<eentry>", "").replace("</eentry>", "").trim();
			}
			if (line.matches("<ecat>.*</ecat>") && flag) {
				ecat = line.replace("<ecat>", "").replace("</ecat>", "").trim().toLowerCase();
			}
			if (line.matches("<esyn>.*</esyn>") && flag) {
				tsyn = line.replace("<esyn>", "").replace("</esyn>", "").trim();
			}
			if (line.matches("<tsample>.*</tsample>") && flag) {
				tsample = line.replace("<tsample>", "").replace("</tsample>", "").trim();
			}
			if (line.matches("<ethai>.*</ethai>") && flag) {
				ethai = line.replace("<ethai>", "").replace("</ethai>", "").trim();
			}
			if (line.matches("<id>.*</id>") && flag) {
				id = line.replace("<id>", "").replace("</id>", "").trim();
			}
			if (line.equals("</Doc>") && flag && !id.trim().isEmpty() && !tentry.trim().isEmpty() && !ecat.trim().isEmpty() && ecat.matches("[a-zA-Z &]*")) { 
				
				//System.out.println(tsearch + " : " + tentry + " : " + eentry + " : " + tcat + " : " + tsyn + " : " + tsample + " : " + id );
				
				map.put(esearch, ecat);
				map.put(tentry, ecat);
				
				String[] tentrys = tentry.split("[()]");
				for (String tentry1 : tentrys) {
					if (treemap.get(tentry1) != null) {
						String[] cats = treemap.get(tentry1).split("\t");
						boolean catFlag = false;
						for (String cat : cats) {
							if (ecat.equals(cat)) {
								catFlag = true;
							}
						}
						if (!catFlag) {
							treemap.put(tentry1, treemap.get(tentry1) + "\t" + ecat);
						}
					} else {
						treemap.put(tentry1, ecat);
					}
					
				}
				
				if (ne.get(ecat) != null) {
					ne.put(ecat, ne.get(ecat) + 1);
				} else {
					ne.put(ecat, 1);
				}
				
				if (esearch.equals(tentry)) {
					count++;
				} else {
					diff.put(esearch, tentry);
				}
				
				esearch = "";
				tentry = "";
				eentry = "";
				ecat = "";
				tsyn = "";
				tsample = "";
				id = "";
				flag = false;
				
			}
		}

		int cc = 0;
		for (Map.Entry<String, Integer> entry : ne.entrySet()) {
			System.out.println("key = " + entry.getKey() + "; value = " + entry.getValue());
			cc += entry.getValue();
		}
		System.err.println("count = " + count + "cc = " + cc);
		
		for (Map.Entry<String, String> entry : diff.entrySet()) {
			System.out.println("key = " + entry.getKey() + "; value = " + entry.getValue());
		}
		System.out.println("map = " + map.size() +"; treemap = " + treemap.size() + "; diff = " + diff.size());
		return treemap;
	}
	
	public static void main(String[] args) {
		String etlexFile = "data/corpus/lexitron-data/etlex.utf-8";
		String telexFile = "data/corpus/lexitron-data/telex.utf-8";
		Map<String,String> tMap = telex(telexFile);
		Map<String,String> eMap = etlex(etlexFile);
		System.out.println(tMap.size());
		IOUtil.appendLines("data/corpus/lexitron-data/thai-dictionary-telex.txt", mapToList(tMap));
//		IOUtil.writeLines("data/corpus/lexitron-data/thai-dictionary-etlex.txt", mapToList(eMap));
		Map<String, String> treemap = new TreeMap<String, String>();
		for (Map.Entry<String, String> entry : treemap.entrySet()) {
			String n = entry.getValue();
			String[] nnn = n.split("\t");
			for (String nn : nnn) {
				treemap.put(nn, "1");
			}
		}
		
		for (String key : treemap.keySet()) {
			System.out.println(key);
		}
		
	}
		

}
