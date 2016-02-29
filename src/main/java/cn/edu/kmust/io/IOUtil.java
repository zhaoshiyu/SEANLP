package cn.edu.kmust.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
	
	public static InputStream getInputStream(String name) {
		return IOUtil.class.getResourceAsStream(name);
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
		List charList = new ArrayList();
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
		List<String> fileContent = new ArrayList<String>();
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
		List<String> fileContent = new ArrayList<String>();
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
		List<String> lines = new ArrayList<String>();
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
		List<String> lines = new ArrayList<String>();
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
