package cn.edu.kmust.seanlp;

/**
 * 语言
 * @author  Zhao Shiyu
 *
 */
public enum Language {
	
	/**
	 * 泰语
	 */
	Thai("th"),
	/**
	 * 越南语
	 */
	Vietnamese("vi"),
	/**
	 * 柬埔寨语
	 */
	Khmer("km"),
	/**
	 * 老挝语
	 */
	Lao("lo"),
	/**
	 * 缅甸语
	 */
	Burmese("bu"),
	/**
	 * 缅甸语
	 */
	Myanmar("my"),
	/**
	 * 汉语
	 */
	Chinese("zh"),
	/**
	 * 英语
	 */
	English("en");

	private String label;

	private Language(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

}
