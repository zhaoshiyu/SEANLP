package cn.edu.kmust.seanlp.CRF;

/**
 * 给一个实例生成一个元素表
 * 
 */
public class Table {
	/**
	 * 真实值，请不要直接读取
	 */
	public String[][] sheet;
	static final String HEAD = "_B";

	@Override
	public String toString() {
		if (sheet == null)
			return "null";
		final StringBuilder sb = new StringBuilder(sheet.length * sheet[0].length * 2);
		for (String[] line : sheet) {
			for (String element : line) {
				sb.append(element).append('\t');
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * 获取表中某一个元素
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public String get(int x, int y) {
		if (x < 0)
			return HEAD + x;
		if (x >= sheet.length)
			return HEAD + "+" + (x - sheet.length + 1);

		return sheet[x][y];
	}

	public void setLast(int x, String t) {
		sheet[x][sheet[x].length - 1] = t;
	}

	public int size() {
		return sheet.length;
	}
	
	public String[] toArray() {
		if (sheet == null)
			return null;
		String[] strs = new String[sheet.length];
		for (int i = 0; i < sheet.length; i++) {
			strs[i] = sheet[i][0];
		}
		return strs;
	}
}
