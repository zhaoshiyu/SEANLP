package cn.edu.kmust.seanlp.segmenter.CRF;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.CRF.Table;
import cn.edu.kmust.seanlp.CRF.model.StaticThaidCRFModel;
import cn.edu.kmust.seanlp.CRF.model.StaticThaigCRFModel;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.POS.ThaiPOS;
import cn.edu.kmust.seanlp.segmenter.AbstractThaiSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.util.RadicalMap;
import cn.edu.kmust.seanlp.util.StringUtil;

/**
 * 条件随机场泰语分词器
 * 
 * @author  Zhao Shiyu
 *
 */
public class ThaiCRFSegmenter extends AbstractThaiSegmenter {
	
	private final POS pos = new ThaiPOS();

	/**
	 * 音节切分
	 */
	@Override
	protected StringBuffer syllableSegment(char[] chars) {
		if (chars.length == 0)
			return null;
		String v[][] = new String[chars.length][3];
		int length = chars.length;
		for (int i = 0; i < length; ++i) {
			v[i][0] = String.valueOf(chars[i]);
			v[i][1] = RadicalMap.getThaiCharType(chars[i]);
		}
		Table table = new Table();
		table.sheet = v;
		StaticThaidCRFModel.crfThaiSyllableSegmentModel.tag(table);
		StringBuffer sb = new StringBuffer();
		if (Config.DEBUG) {
			System.out.println("CRF标注结果");
			System.out.println(table);
		}
		for (int i = 0; i < table.sheet.length; i++) {
			String[] line = table.sheet[i];
			switch (line[2].charAt(0)) {
			case 'B': {
				int begin = i;
				while (table.sheet[i][2].charAt(0) != 'E') {
					++i;
					if (i == table.sheet.length) {
						break;
					}
				}
				if (i == table.sheet.length) {
					sb.append(new String(chars, begin, i - begin) + "|");
				} else
					sb.append(new String(chars, begin, i - begin+ 1) + "|");
			}
				break;
			default: {
				sb.append(line[0] + "|");
			}
				break;
			}
		}
		return sb;
	}

	/**
	 * 音节合并
	 */
	@Override
	protected List<Term> syllableMerging(String[] syllables) {
		if (syllables.length == 0)
			return Collections.emptyList();
		String v[][] = new String[syllables.length][3];
		int length = syllables.length;
		for (int i = 0; i < length; ++i) {
			v[i][0] = String.valueOf(syllables[i]);
			v[i][1] = RadicalMap.getThaiSyllableType(syllables[i]);
		}
		Table table = new Table();
		table.sheet = v;
		StaticThaidCRFModel.crfThaiSyllableMergeModel.tag(table);
		List<Term> termList = new LinkedList<Term>();
		if (Config.DEBUG) {
			System.out.println("CRF标注结果");
			System.out.println(table);
		}
		for (int i = 0; i < table.sheet.length; i++) {
			String[] line = table.sheet[i];
			switch (line[2].charAt(0)) {
			case 'B': {
				int begin = i;
				while (table.sheet[i][2].charAt(0) != 'E') {
					++i;
					if (i == table.sheet.length) {
						break;
					}
				}
				if (i == table.sheet.length) {
					termList.add(new Term(StringUtil.merge(syllables, begin, i - begin), null));
//					termList.add(new Term(syllableMerge(syllables, begin, i - begin), null));
				} else {
					termList.add(new Term(StringUtil.merge(syllables, begin, i - begin+ 1), null));
					//termList.add(new Term(syllableMerge(syllables, begin, i - begin+ 1), null));
				}
			}
				break;
			default: {
				termList.add(new Term(line[0], null));
			}
				break;
			}
		}
		//词性标注
		if (Config.BaseConf.speechTagging) {
			termList = pos.speechTagging(termList);
        }
		return termList;
	}
	/**
	 * 层叠条件随机场分词
	 */
	@Override
	protected List<Term> segment(char[] chars) {
		if (chars.length == 0)
			return null;
		Table table = new Table();
		table.sheet = createThaiCharSheet(chars);
		StaticThaidCRFModel.crfThaiSyllableSegmentModel.tag(table);
		String[] syllables = mergeChar(chars, table).replaceAll("\\|$", "").split("\\|");
		Table syllableTable = new Table();
		syllableTable.sheet = createThaiSyllableSheet(syllables);
		StaticThaidCRFModel.crfThaiSyllableMergeModel.tag(syllableTable);
		List<Term> termList = mergeSyllable(syllables, syllableTable);
		//词性标注
		if (Config.BaseConf.speechTagging) {
			termList = pos.speechTagging(termList);
        }
		return termList;
	}

	/**
	 * 单层条件随机场分词
	 */
	@Override
	protected List<Term> gCRFWordSegment(char[] chars) {
		if (chars.length == 0)
			return Collections.emptyList();
		String v[][] = new String[chars.length][3];
		int length = chars.length;
		for (int i = 0; i < length; ++i) {
			v[i][0] = String.valueOf(chars[i]);
			v[i][1] = RadicalMap.getThaiCharType(chars[i]);
		}
		Table table = new Table();
		table.sheet = v;
		StaticThaigCRFModel.crfThaiWordSegmentModel.tag(table);
		List<Term> termList = new LinkedList<Term>();
		if (Config.DEBUG) {
			System.out.println("CRF标注结果");
			System.out.println(table);
		}
		for (int i = 0; i < table.sheet.length; i++) {
			String[] line = table.sheet[i];
			switch (line[2].charAt(0)) {
			case 'B': {
				int begin = i;
				while (table.sheet[i][2].charAt(0) != 'E') {
					++i;
					if (i == table.sheet.length) {
						break;
					}
				}
				if (i == table.sheet.length) {
					termList.add(new Term(new String(chars, begin, i - begin), null));
				} else
					termList.add(new Term(new String(chars, begin, i - begin+ 1), null));
			}
				break;
			default: {
				termList.add(new Term(line[0], null));
			}
				break;
			}
		}
		//词性标注
		if (Config.BaseConf.speechTagging) {
			termList = pos.speechTagging(termList);
        }
		return termList;
	}
	
	public String[][] createThaiCharSheet(char[] chars) {
		int length = chars.length;
		if (length == 0)
			return null;
		String sheet[][] = new String[length][3];
		for (int i = 0; i < length; ++i) {
			sheet[i][0] = String.valueOf(chars[i]);
			sheet[i][1] = RadicalMap.getThaiCharType(chars[i]);
		}
		return sheet;
	}
	
	public String[][] createThaiSyllableSheet(String[] syllables) {
		int length = syllables.length;
		if (length == 0)
			return null;
		String sheet[][] = new String[length][3];
		for (int i = 0; i < length; ++i) {
			String syllable = syllables[i];
			sheet[i][0] = syllable;
			sheet[i][1] = RadicalMap.getThaiSyllableType(syllable);
		}
		return sheet;
	}
	
	public String mergeChar(char[] chars, Table table) {
		StringBuffer sb = new StringBuffer();
		if (Config.DEBUG) {
			System.out.println("CRF标注结果");
			System.out.println(table);
		}
		for (int i = 0; i < table.sheet.length; i++) {
			String[] line = table.sheet[i];
			switch (line[2].charAt(0)) {
			case 'B': {
				int begin = i;
				while (table.sheet[i][2].charAt(0) != 'E') {
					++i;
					if (i == table.sheet.length) {
						break;
					}
				}
				if (i == table.sheet.length) {
					sb.append(new String(chars, begin, i - begin) + "|");
				} else
					sb.append(new String(chars, begin, i - begin+ 1) + "|");
			}
				break;
			default: {
				sb.append(line[0] + "|");
			}
				break;
			}
		}
		return sb.toString();
	}
	
	
	public List<Term> mergeSyllable(String[] syllables, Table table) {
		List<Term> termList = new LinkedList<Term>();
		if (Config.DEBUG) {
			System.out.println("CRF标注结果");
			System.out.println(table);
		}
		for (int i = 0; i < table.sheet.length; i++) {
			String[] line = table.sheet[i];
			switch (line[2].charAt(0)) {
			case 'B': {
				int begin = i;
				while (table.sheet[i][2].charAt(0) != 'E') {
					++i;
					if (i == table.sheet.length) {
						break;
					}
				}
				if (i == table.sheet.length) {
					termList.add(new Term(StringUtil.merge(syllables, begin, i - begin), null));
				} else {
					termList.add(new Term(StringUtil.merge(syllables, begin, i - begin+ 1), null));
				}
			}
				break;
			default: {
				termList.add(new Term(line[0], null));
			}
				break;
			}
		}
		return termList;
	}

	@Override
	protected String syllableSegment(String[] sentences) {
		StringBuffer sb = new StringBuffer();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			sb.append(syllableSegment(sentences[i].toCharArray()));
		}
		return sb.toString();
	}
	
	@Override
	protected String[] sentenceTosyllables(char[] chars) {		
		return syllableSegment(chars).toString().replaceAll("\\|$", "").split("\\|");
	}
	
	@Override
	protected String[] sentenceTosyllables(String sentence) {
		return sentenceTosyllables(sentence.toCharArray());
	}
	
	@Override
	protected List<Term> syllableMerge(String sentence) {
		return syllableMerging(sentenceTosyllables(sentence));
	}

	@Override
	protected List<Term> sentenceMerge(String[] sentences) {
		List<Term> termList = new LinkedList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			termList.addAll(syllableMerge(sentences[i]));
			//termList.add(new Term(" ",  Nature.PUNC));
		}
		return termList;
	}
	
	@Override
	protected List<Term> syllableMerge(String[] sentences) {
		List<Term> termList = new LinkedList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			termList.addAll(syllableMerge(sentences[i]));
			//termList.add(new Term(" ",  Nature.PUNC));
		}
		return termList;
	}

	@Override
	protected List<Term> gCRFWordSegment(String[] sentences) {
		List<Term> termList = new LinkedList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			termList.addAll(gCRFWordSegment(sentences[i].toCharArray()));
			//termList.add(new Term(" ",  Nature.PUNC));
		}
		return termList;
	}

	@Override
	protected List<Term> segment(String[] sentences) {
		return sentenceMerge(sentences);
	}

	@Override
	public String syllableSegment(String text) {
		return syllableSegment(sentenceSegment(text));
	}

	@Override
	public List<Term> dCRFWordSegment(String text) {
		return syllableMerge(sentenceSegment(text));
	}

	@Override
	public List<Term> gCRFWordSegment(String text) {
		return gCRFWordSegment(sentenceSegment(text));
	}

	@Override
	public List<Term> seg(String text) {
		return segment(text.toCharArray());
	}
	
	
}
