package cn.edu.kmust.seanlp.POS;

import java.util.List;

import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.domain.Vertex;

/**
 * 词性标注接口
 * 
 * @author Zhao Shiyu
 *
 */
public interface POS {
	
	List<Term> speechTagging(List<Term> termList);
	
	public List<Vertex> toVertexList(List<Term> termList, boolean appendStart);
	

}
