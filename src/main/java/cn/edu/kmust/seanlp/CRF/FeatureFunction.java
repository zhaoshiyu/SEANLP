package cn.edu.kmust.seanlp.CRF;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import cn.edu.kmust.seanlp.util.ByteArray;
import cn.edu.kmust.seanlp.util.ICacheAble;

/**
 * 特征函数，其实是tag.size个特征函数的集合
 * 
 */
public class FeatureFunction implements ICacheAble {
	/**
	 * 环境参数
	 */
	char[] o;
	/**
	 * 标签参数
	 */
	// String s;

	/**
	 * 权值，按照index对应于tag的id
	 */
	double[] w;

	public FeatureFunction(char[] o, int tagSize) {
		this.o = o;
		w = new double[tagSize];
	}

	public FeatureFunction() {
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(o.length);
		for (char c : o) {
			out.writeChar(c);
		}
		out.writeInt(w.length);
		for (double v : w) {
			out.writeDouble(v);
		}
	}
	
	public void save(ObjectOutputStream oos) throws Exception {
		oos.writeInt(o.length);
		for (char c : o) {
			oos.writeChar(c);
		}
		oos.writeInt(w.length);
		for (double v : w) {
			oos.writeDouble(v);
		}
	}
	
	public boolean load(ObjectInputStream ois) throws IOException {
		int size = ois.read();
		o = new char[size];
		for (int i = 0; i < size; ++i) {
			o[i] = ois.readChar();
		}
		size = ois.read();
		w = new double[size];
		for (int i = 0; i < size; ++i) {
			w[i] = ois.readDouble();
		}
		return true;
	}
	
	public boolean load(ByteArray byteArray) {
		int size = byteArray.nextInt();
		o = new char[size];
		for (int i = 0; i < size; ++i) {
			o[i] = byteArray.nextChar();
		}
		size = byteArray.nextInt();
		w = new double[size];
		for (int i = 0; i < size; ++i) {
			w[i] = byteArray.nextDouble();
		}
		return true;
	}
	
}
