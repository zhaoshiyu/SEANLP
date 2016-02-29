package cn.edu.kmust.seanlp.util;

import java.io.DataOutputStream;

/**
 * 可写入或读取二进制
 * 
 */
public interface ICacheAble {
	/**
	 * 写入
	 * 
	 * @param out
	 * @throws Exception
	 */
	public void save(DataOutputStream out) throws Exception;

	/**
	 *  加载
	 * @param byteArray
	 * @return
	 */
	public boolean load(ByteArray byteArray);
}
