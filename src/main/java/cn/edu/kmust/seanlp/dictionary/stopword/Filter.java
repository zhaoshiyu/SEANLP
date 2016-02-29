package cn.edu.kmust.seanlp.dictionary.stopword;

import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 停用词过滤器接口
 * @author Zhao Shiyu
 */
public interface Filter
{
    /**
     * 是否应当将这个term纳入计算
     *
     * @param term
     * @return 是否应当
     */
    boolean shouldInclude(Term term);
}
