package cn.edu.kmust.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.util.StringUtil;

/**
 *IO常用操作工具
 *
 * @author Zhao Shiyu
 *
 */
public class IOUtil {

	public static final String UTF8 = "utf-8";
	
	public static final boolean system_type = System.getProperty("os.name").toLowerCase().startsWith("win");
	public static final String line_separator = System.getProperty("line.separator");
	
	public static InputStream getInputStream(String path) {
		return IOUtil.class.getResourceAsStream(path);
	}
	
	/**
	 * 序列化对象
	 *
	 * @param o
	 * @param path
	 * @return
	 */
	public static boolean saveObjectTo(Object o, String path) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(path));
			oos.writeObject(o);
			oos.close();
		} catch (IOException e) {
			Log.logger.warning("在保存对象" + o + "到" + path + "时发生异常" + e);
			return false;
		}

		return true;
	}

	/**
	 * 反序列化对象
	 *
	 * @param path
	 * @return
	 */
	public static Object readObjectFrom(String path) {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(path));
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (Exception e) {
			Log.logger.warning("在从" + path + "读取对象时发生异常" + e);
		}

		return null;
	}

	/**
	 * 一次性读入纯文本
	 *
	 * @param path
	 * @return
	 */
	public static String readTxt(String path) {
		if (path == null)
			return null;
		File file = new File(path);
		Long fileLength = file.length();
		byte[] fileContent = new byte[fileLength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(fileContent);
			in.close();
		} catch (FileNotFoundException e) {
			Log.logger.warning("找不到" + path + e);
			return null;
		} catch (IOException e) {
			Log.logger.warning("读取" + path + "发生IO异常" + e);
			return null;
		}

		return new String(fileContent, Charset.forName("UTF-8"));
	}

	public static LinkedList<String[]> readCsv(String path) {
		LinkedList<String[]> resultList = new LinkedList<String[]>();
		LinkedList<String> lineList = readLineList(path);
		for (String line : lineList) {
			resultList.add(line.split(","));
		}
		return resultList;
	}

	/**
	 * 快速保存
	 *
	 * @param path
	 * @param content
	 * @return
	 */
	@SuppressWarnings("resource")
	public static boolean saveTxt(String path, String content) {
		try {
			FileChannel fc = new FileOutputStream(path).getChannel();
			fc.write(ByteBuffer.wrap(content.getBytes()));
			fc.close();
		} catch (Exception e) {
			Log.logger.throwing("IOUtil", "saveTxt", e);
			Log.logger.warning("IOUtil saveTxt 到" + path + "失败" + e.toString());
			return false;
		}
		return true;
	}

	public static boolean saveTxt(String path, StringBuilder content) {
		return saveTxt(path, content.toString());
	}

	public static <T> boolean saveCollectionToTxt(Collection<T> collection,
			String path) {
		StringBuilder sb = new StringBuilder();
		for (Object o : collection) {
			sb.append(o);
			sb.append('\n');
		}
		return saveTxt(path, sb.toString());
	}

	/**
	 * 将整个文件读取为字节数组
	 *
	 * @param path
	 * @return
	 */
	public static byte[] readBytes(String path) {
		try {
			FileInputStream fis = new FileInputStream(path);
			FileChannel channel = fis.getChannel();
			int fileSize = (int) channel.size();
			ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);
			channel.read(byteBuffer);
			byteBuffer.flip();
			byte[] bytes = byteBuffer.array();
			byteBuffer.clear();
			channel.close();
			fis.close();
			return bytes;
		} catch (Exception e) {
			Log.logger.warning("读取" + path + "时发生异常" + e.getMessage());
		}
		return null;
	}
	
	public static byte[] readBytesByMapped(String path) {
		FileChannel fc = null;
		try {
			fc = new RandomAccessFile(path, "r").getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
			byte[] bytes = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				byteBuffer.get(bytes, 0, byteBuffer.remaining());
			}
			fc.close();
			return bytes;
		} catch (Exception e) {
			Log.logger.warning("读取" + path + "时发生异常" + e.getMessage());
		}
		return null;
	}
	
	public static byte[] readBytes(InputStream is) {
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int len = -1;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = is.read(buffer, 0, bufferSize)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			is.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			Log.logger.warning("读取输入流时发生异常" + e);
		}
		return null;
	}
	 
	/**
	 * 将整个gzip文件读取为字节数组
	 *
	 * @param path
	 * @return
	 */
	public static byte[] readGzBytes(InputStream in) {
		int bufferSize = 1024;
		byte[] byteBuffer = new byte[bufferSize];
		int len = -1;
		try {
			DataInputStream dos = new DataInputStream(new GZIPInputStream(in));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = dos.read(byteBuffer, 0, bufferSize)) != -1) {
				baos.write(byteBuffer, 0, len);
			}
			baos.close();
			dos.close();
			in.close();
			return baos.toByteArray();
		} catch (Exception e) {
			Log.logger.warning("读取时发生异常" + e);
		}
		return null;
	}

	public static LinkedList<String> readLineList(String path) {
		LinkedList<String> result = new LinkedList<String>();
		String txt = readTxt(path);
		if (txt == null)
			return result;
		StringTokenizer tokenizer = new StringTokenizer(txt, "\n");
		while (tokenizer.hasMoreTokens()) {
			result.add(tokenizer.nextToken());
		}

		return result;
	}

	/**
	 * 用省内存的方式读取大文件
	 *
	 * @param path
	 * @return
	 */
	public static LinkedList<String> readLineListWithLessMemory(String path) {
		LinkedList<String> result = new LinkedList<String>();
		String line = null;
		try {
			BufferedReader bw = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "UTF-8"));
			while ((line = bw.readLine()) != null) {
				result.add(line);
			}
			bw.close();
		} catch (Exception e) {
			Log.logger.warning("加载" + path + "失败，" + e);
		}

		return result;
	}

	public static boolean saveMapToTxt(Map<Object, Object> map, String path) {
		return saveMapToTxt(map, path, "=");
	}

	public static boolean saveMapToTxt(Map<Object, Object> map, String path,
			String separator) {
		map = new TreeMap<Object, Object>(map);
		return saveEntrySetToTxt(map.entrySet(), path, separator);
	}

	public static boolean saveEntrySetToTxt(
			Set<Map.Entry<Object, Object>> entrySet, String path,
			String separator) {
		StringBuilder sbOut = new StringBuilder();
		for (Map.Entry<Object, Object> entry : entrySet) {
			sbOut.append(entry.getKey());
			sbOut.append(separator);
			sbOut.append(entry.getValue());
			sbOut.append('\n');
		}
		return saveTxt(path, sbOut.toString());
	}

	public static LineIterator readLine(String path) {
		return new LineIterator(path);
	}

	/**
	 * 方便读取按行读取大文件
	 */
	public static class LineIterator implements Iterator<String> {
		BufferedReader bw;
		String line;

		public LineIterator(String path) {
			try {
				bw = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
				line = bw.readLine();
			} catch (FileNotFoundException e) {
				Log.logger.warning("文件" + path + "不存在，接下来的调用会返回null"
						+ StringUtil.exceptionToString(e));
			} catch (IOException e) {
				Log.logger.warning("在读取过程中发生错误" + StringUtil.exceptionToString(e));
			}
		}
		
		public LineIterator(InputStream is) {
			try {
				bw = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				line = bw.readLine();
			} catch (FileNotFoundException e) {
				Log.logger.warning("文件不存在，接下来的调用会返回null" + StringUtil.exceptionToString(e));
			} catch (IOException e) {
				Log.logger.warning("在读取过程中发生错误" + StringUtil.exceptionToString(e));
			}
		}

		public void close() {
			if (bw == null)
				return;
			try {
				bw.close();
				bw = null;
			} catch (IOException e) {
				Log.logger.warning("关闭文件失败" + StringUtil.exceptionToString(e));
			}
			return;
		}

		public boolean hasNext() {
			if (bw == null)
				return false;
			if (line == null) {
				try {
					bw.close();
					bw = null;
				} catch (IOException e) {
					Log.logger.warning("关闭文件失败" + StringUtil.exceptionToString(e));
				}
				return false;
			}

			return true;
		}

		public String next() {
			String preLine = line;
			try {
				if (bw != null) {
					line = bw.readLine();
					if (line == null && bw != null) {
						try {
							bw.close();
							bw = null;
						} catch (IOException e) {
							Log.logger.warning("关闭文件失败"
									+ StringUtil.exceptionToString(e));
						}
					}
				} else {
					line = null;
				}
			} catch (IOException e) {
				Log.logger.warning("在读取过程中发生错误" + StringUtil.exceptionToString(e));
			}
			return preLine;
		}

		public void remove() {
			throw new UnsupportedOperationException("只读，不可写！");
		}
	}
	
	/**
	 * 追加文件：使用FileWriter 以行为单位追加文件,每次追加一行
	 * 
	 * @param fileName 文件名
	 * @param line 追加的内容
	 */
	public static void appendLine(File file, String line) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(file, true);
			writer.write(line);
			writer.write(line_separator); // 写入换行的码
//			if (!line.trim().isEmpty()) {
//				writer.write(line);
//			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用FileWriter 以行为单位追加文件,每次追加一行
	 * 
	 * @param fileName 文件名
	 * @param line 追加的内容
	 */
	public static void appendLine(String fileName, String line) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			writer.write(line);
			writer.write(line_separator); // 写入换行的码
//			if (!line.trim().isEmpty()) {
//				writer.write(line);
//			}
//			writer.write(line_separator); // 写入换行的码

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用FileWriter 以行为单位追加文件,每次追加一行
	 * 
	 * @param file 文件
	 * @param line 追加的内容
	 */
	public static void appendLine(File file, String line, String encoding) {
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(line);
			writer.write(line_separator); // 写入换行的码

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用BufferedWriter 以行为单位追加文件,每次追加一行
	 * 
	 * @param fileName 文件名
	 * @param line 追加的内容
	 * * @param encoding 编码
	 */
	public static void appendLine(String fileName, String line, String encoding) {
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(fileName), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(line);
			writer.write(line_separator); // 写入换行的码

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用FileWriter写入
	 * @param fileName 文件名
	 * @param line 写入的内容
	 */
	public static void overwriteLine(String fileName, String line) {
		FileWriter writer = null;
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			writer.write(line);
			writer.write(line_separator); // 写入换行的码
//			if (!line.trim().isEmpty()) {
//				writer.write(line);
//			}
//			writer.write(line_separator); // 写入换行的码

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用BufferedWriter写入
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLine(String fileName, String line, String encoding) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(line);
			writer.write(line_separator); // 写入换行的码

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用FileWriter写入
	 * @param fileName 文件名
	 * @param line 写入的内容
	 */
	public static void overwriteLine(File file, String line) {
		FileWriter writer = null;
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(file, true);
			writer.write(line);
			writer.write(line_separator); // 写入换行的码
//			if (!line.trim().isEmpty()) {
//				writer.write(line);
//			}
//			writer.write(line_separator); // 写入换行的码

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用BufferedWriter写入
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLine(File file, String line, String encoding) {
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(line);
			writer.write(line_separator); // 写入换行的码

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用FileWriter 以行为单位用FileWriter追加内容,每次追加多行
	 * @param fileName 文件名
	 * @param lines 写入的内容
	 */
	public static void appendLines(String fileName, List<String> lines) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
//				if (!line.trim().isEmpty()) {
//				writer.write(line);
			}
//			writer.write(line_separator); // 写入换行的码
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用FileWriter 以行为单位用FileWriter追加内容,每次追加多行
	 * @param fileName 文件名
	 * @param lines 写入的内容
	 */
	public static void appendLines(String fileName, Iterable<String> lines) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
//				if (!line.trim().isEmpty()) {
//				writer.write(line);
			}
//			writer.write(line_separator); // 写入换行的码
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用BufferedWriter 以行为单位用BufferedWriter追加内容,每次追加多行
	 * @param fileName 文件名
	 * @param lines 写入的内容
	 * @param encoding 编码
	 */
	public static void appendLines(String fileName, List<String> lines, String encoding) {
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(fileName), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用BufferedWriter 以行为单位用BufferedWriter追加内容,每次追加多行
	 * @param fileName 文件名
	 * @param lines 写入的内容
	 * @param encoding 编码
	 */
	public static void appendLines(String fileName, Iterable<String> lines, String encoding) {
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(fileName), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用FileWriter 以行为单位追加文件,每次追加多行
	 * @param fileName 文件名
	 * @param lines 写入的内容
	 */
	public static void appendLines(File file, List<String> lines) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(file, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
//				if (!line.trim().isEmpty()) {
//				writer.write(line);
//			}
//			writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 追加文件：使用FileWriter 以行为单位追加文件,每次追加多行
	 * @param fileName 文件名
	 * @param lines 写入的内容
	 */
	public static void appendLines(File file, Iterable<String> lines) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(file, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
//				if (!line.trim().isEmpty()) {
//				writer.write(line);
//			}
//			writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用BufferedWriter 以行为单位用BufferedWriter追加内容,每次追加多行
	 * @param fileName 文件名
	 * @param lines 写入的内容
	 * @param encoding 编码
	 */
	public static void appendLines(File file, List<String> lines, String encoding) {
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加文件：使用BufferedWriter 以行为单位用BufferedWriter追加内容,每次追加多行
	 * @param fileName 文件名
	 * @param lines 写入的内容
	 * @param encoding 编码
	 */
	public static void appendLines(File file, Iterable<String> lines, String encoding) {
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用FileWriter写入内容，一次写入多行
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLines(String fileName, List<String> lines) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			FileWriter writer = new FileWriter(fileName, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 删除原来的内容从新用FileWriter写入内容，一次写入多行
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLines(String fileName, Iterable<String> lines) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			FileWriter writer = new FileWriter(fileName, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用FileWriter写入内容，一次写入多行
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLines(File file, List<String> lines) {
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			FileWriter writer = new FileWriter(file, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用FileWriter写入内容，一次写入多行
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLines(File file, Iterable<String> lines) {
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			FileWriter writer = new FileWriter(file, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用BufferedWriter写入内容，一次写入多行
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLines(String fileName, List<String> lines, String encoding) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(fileName), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 删除原来的内容从新用BufferedWriter写入内容，一次写入多行
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLines(String fileName, Iterable<String> lines, String encoding) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(fileName), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用BufferedWriter写入内容，一次写入多行
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLines(File file, List<String> lines, String encoding) {
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除原来的内容从新用BufferedWriter写入内容，一次写入多行
	 * @param fileName 文件名
	 * @param line 写入的内容
	 * @param encoding 编码
	 */
	public static void overwriteLines(File file, Iterable<String> lines, String encoding) {
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
			BufferedWriter writer = new BufferedWriter(write);
			for (String line : lines) {
				writer.write(line);
				writer.write(line_separator); // 写入换行的码
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 写文件，为泰语编码 "ISO-8859-11"写入
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendThaiFile(String fileName, String content) {
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(fileName), "ISO-8859-11");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 追加写，为泰语编码 "ISO-8859-11"写入
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendThaiFile(File file, String content) {
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-11");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除内容重新写入，为泰语编码 "ISO-8859-11"写入
	 * 
	 * @param fileName
	 * @param fileContent
	 */
	public static void overwriteThaiFile(String fileName, String content) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-11");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 追加文件：使用RandomAccessFile
	 */
	public static void appendMethodA(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件 以字符为单位读取文件内容，一次读一个字节
	 * 
	 * @param fileName
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List readFileByChar(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		List charList = new LinkedList();
		try {
			System.out.println("以字符为单位读取文件内容，一次读一个字节：");
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(file), "utf-8");
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				// 对于windows下，\r\n这两个字符在一起时，表示一个换行。
				// 但如果这两个字符分开显示时，会换两次行。
				// 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
				if (((char) tempchar) != '\r') {
					charList.add((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charList;
	}

	/**
	 * 读取文件 返回整篇文档
	 * 
	 * @param fileName
	 * @return String
	 */
	public static String readFile(String fileName) {
		String fileContent = "";
		try {
			File f = new File(fileName);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(f), "utf-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent += line;
				}
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent;
	}
	
	/**
	 * 以行为单位读取文件
	 * 
	 * @param file
	 * @return List<String>
	 */
	public static List<String> readLines(File file) {
		List<String> fileContent = new LinkedList<String>();
		try {
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent.add(line);
				}
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent;
	}

	/**
	 * 以行为单位读取文件
	 * 
	 * @param fileName
	 * @return List<String>
	 */
	public static List<String> readLines(String fileName) {
		File file = new File(fileName);
		return readLines(file);
	}
	
	/**
	 * 以行为单位读取文件
	 * 
	 * @param is
	 * @return
	 */
	public static List<String> readLines(InputStream is) {
		List<String> fileContent = new LinkedList<String>();
		try {
			InputStreamReader read = new InputStreamReader(is, "utf-8");
			BufferedReader reader = new BufferedReader(read);
			String line;
			while ((line = reader.readLine()) != null) {
				fileContent.add(line);
			}
			read.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent;
	}
	
	/**
	 * 以行为单位读取文件
	 * 
	 * @param file
	 * @return List<String>
	 */
	public static List<String> readLines(File file, String encoding) {
		List<String> lines = new LinkedList<String>();
		try {
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * 以行为单位读取文件
	 * 
	 * @param fileName
	 * @return List<String>
	 */
	public static List<String> readLines(String fileName, String encoding) {
		File file = new File(fileName);
		return readLines(file, encoding);
	}
	
	/**
	 * 以行为单位读取文件，使用泰语编码"ISO-8859-11"读取
	 * 
	 * @param file
	 * @return List<String>
	 */
	public static List<String> readThaiLines(File file) {
		List<String> lines = new LinkedList<String>();
		try {
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), "ISO-8859-11");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * 以行为单位读取文件，使用泰语编码"ISO-8859-11"读取
	 * 
	 * @param fileName
	 * @return List<String>
	 */
	public static List<String> readThaiLines(String fileName) {
		File file = new File(fileName);
		return readThaiLines(file);
	}
	
	/**
	 * 将读取文件 返回整篇文档
	 * 
	 * @param file
	 * @param encoding
	 * @return String
	 */
	public static String readFile(File file, String encoding) {
		String fileContent = "";
		try {
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent += line;
				}
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent;
	}

	/**
	 * 将读取文件 返回整篇文档
	 * 
	 * @param fileName
	 * @param encoding
	 * @return String
	 */
	public static String readFile(String fileName, String encoding) {
		File file = new File(fileName);
		return readFile(file, encoding);
	}
	
}
