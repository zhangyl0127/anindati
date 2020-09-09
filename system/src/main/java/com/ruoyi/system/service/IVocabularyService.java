package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnVocabulary;

import java.util.List;

public interface IVocabularyService extends IService<IdnVocabulary> {

    List<IdnVocabulary> selectWordsList(IdnVocabulary words);

    IdnVocabulary selectWordsById(Long wordsId);

    String checkWordsNameUnique(IdnVocabulary words);

    int insertWords(IdnVocabulary words);

    int selectNormalChildrenWordsById(Long wordsId);

    int updateWords(IdnVocabulary words);

    boolean hasChildByWordsId(Long wordsId);

    boolean checkWordsExistUser(Long wordsId);

    int deleteWordsById(Long wordsId);

    List<TreeSelect> buildWordsTreeSelect(List<IdnVocabulary> wordsList);

    public List<IdnVocabulary> buildWordsTree(List<IdnVocabulary> words);
}
