package cn.edu.kmust.seanlp.segmenter.CRF;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.CRF.Table;
import cn.edu.kmust.seanlp.CRF.model.StaticBurmeseCRFModel;
import cn.edu.kmust.seanlp.POS.BurmesePOS;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.segmenter.AbstractBurmeseSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.util.RadicalMap;

/**
 * 条件随机场缅甸语分词器
 * 
 * @author  Zhao Shiyu
 *
 */
public class BurmeseCRFSegmenter extends AbstractBurmeseSegmenter {
	
	private final POS pos = new BurmesePOS();

	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		if (sentence.length == 0)
			return Collections.emptyList();
		String v[][] = new String[sentence.length][3];
		int length = sentence.length;
		for (int i = 0; i < length; ++i) {
			v[i][0] = String.valueOf(sentence[i]);
			v[i][1] = RadicalMap.getBurmeseCharType(sentence[i]);
		}
		Table table = new Table();
		table.sheet = v;
		StaticBurmeseCRFModel.crfBurmeseSegmentModel.tag(table);
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
					termList.add(new Term(new String(sentence, begin, i - begin), null));
				} else
					termList.add(new Term(new String(sentence, begin, i - begin+ 1), null));
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

	
	
}
