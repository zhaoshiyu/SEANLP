package cn.edu.kmust.seanlp.segmenter.CRF;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.CRF.Table;
import cn.edu.kmust.seanlp.CRF.model.StaticKhmerCRFModel;
import cn.edu.kmust.seanlp.POS.KhmerPOS;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.segmenter.AbstractKhmerSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.util.StringUtil;

/**
 * 条件随机场高棉语语分词器
 * 
 * @author  Zhao Shiyu
 *
 */
public class KhmerCRFSegmenter  extends AbstractKhmerSegmenter {
	
	private final POS pos = new KhmerPOS();
	
	@Override
	protected List<Term> segmentSentence(String[] sentence) {
		if (sentence.length == 0)
			return Collections.emptyList();
		String v[][] = new String[sentence.length][2];
		int length = sentence.length;
		for (int i = 0; i < length; ++i) {
			v[i][0] = String.valueOf(sentence[i]);
		}
		Table table = new Table();
		table.sheet = v;
		StaticKhmerCRFModel.crfKhmerSegmentModel.tag(table);
		List<Term> termList = new LinkedList<Term>();
		if (Config.DEBUG) {
			System.out.println("CRF标注结果");
			System.out.println(table);
		}
		for (int i = 0; i < table.sheet.length; i++) {
			String[] line = table.sheet[i];
			switch (line[1].charAt(0)) {
			case 'B': {
				int begin = i;
				while (table.sheet[i][1].charAt(0) != 'E') {
					++i;
					if (i == table.sheet.length) {
						break;
					}
				}
				if (i == table.sheet.length) {
					termList.add(new Term(StringUtil.merge(sentence, begin, i - begin), null));
				} else {
					termList.add(new Term(StringUtil.merge(sentence, begin, i - begin+ 1), null));
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

	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		return null;
	}
	
}
