package cn.edu.kmust.seanlp.POS;

import java.util.ArrayList;
import java.util.List;

import cn.edu.kmust.seanlp.algoritm.Viterbi;
import cn.edu.kmust.seanlp.dictionary.CoreDictionary;
import cn.edu.kmust.seanlp.dictionary.language.ThaiCoreDictionary;
import cn.edu.kmust.seanlp.dictionary.language.ThaiNatureTransMatrix;
import cn.edu.kmust.seanlp.segmenter.domain.Nature;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.domain.Vertex;
import cn.edu.kmust.seanlp.util.Predefine;

/**
 * 泰语词性标注
 * 
 * @author Zhao Shiyu
 *
 */
public class ThaiPOS implements POS {
	
	/**
	 * 词性标注
	 * @param termList 分好词，待标注
	 * @return 标注结果
	 */
	public List<Term> speechTagging(List<Term> termList) {
		List<Vertex> vertexList = toVertexList(termList, true);
        Viterbi.compute(vertexList, ThaiNatureTransMatrix.thaiTransMatrix);
        int i = 0;
        for (Term term : termList) {
            if (term.getNature() != null) term.setNature(vertexList.get(i + 1).getNature());
            else term.setNature(Nature.UN);
            ++i;
        }
		return termList;
	}
	
	public List<Vertex> toVertexList(List<Term> termList, boolean appendStart) {
        ArrayList<Vertex> vertexList = new ArrayList<Vertex>(termList.size() + 1);
        //if (appendStart) vertexList.add(Vertex.B);
        Vertex.B  =  new Vertex(Predefine.TAG_BIGIN, " ", new CoreDictionary.Attribute(Nature.BEGIN, Predefine.MAX_FREQUENCY / 10), CoreDictionary.getWordID(ThaiCoreDictionary.thaiDictionary.dictionaryTrie, Predefine.TAG_BIGIN));
        if (appendStart) vertexList.add(Vertex.B);
        for (Term term : termList) {
            CoreDictionary.Attribute attribute = CoreDictionary.get(ThaiCoreDictionary.thaiDictionary.dictionaryTrie,term.getWord());
            if (attribute == null) {
                if (term.getWord().trim().length() == 0) attribute = new CoreDictionary.Attribute(Nature.PUNC);
                else attribute = new CoreDictionary.Attribute(Nature.NPRP);
            }
            else term.setNature(attribute.nature[0]);;
            Vertex vertex = new Vertex(term.getWord(), attribute);
            vertexList.add(vertex);
        }
        return vertexList;
    }

}
