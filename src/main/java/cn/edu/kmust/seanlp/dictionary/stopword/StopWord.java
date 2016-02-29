package cn.edu.kmust.seanlp.dictionary.stopword;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import cn.edu.kmust.seanlp.collection.MDAG.MDAGSet;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
/**
 * 
 * @author Zhao Shiyu
 *
 */
public class StopWord extends MDAGSet implements Filter {
	
	public StopWord() {
	}
	
	public StopWord(File file) throws IOException {
		super(file);
	}
	
	public StopWord(InputStream is) throws IOException {
		super(is);
	}

	public StopWord(Collection<String> strCollection) {
		super(strCollection);
	}

	@Override
	public boolean shouldInclude(Term term) {
		return contains(term.getWord());
	}
}
