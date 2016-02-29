package cn.edu.kmust.seanlp.io;

import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.util.StringUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * 一些常用的IO操作
 *
 */
public class IOUtil {
	
	
	
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
}
