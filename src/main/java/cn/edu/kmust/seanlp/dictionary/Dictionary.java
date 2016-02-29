package cn.edu.kmust.seanlp.dictionary;

import cn.edu.kmust.seanlp.util.ByteArray;

/**
 * 词典接口
 * 
 * @author  Zhao Shiyu
 *
 */
public interface Dictionary<T> {
	
boolean loadDat(ByteArray byteArray);
	

}
