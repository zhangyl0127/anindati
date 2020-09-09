package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnVocabulary;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface VocabularyMapper extends BaseMapper<IdnVocabulary> {
    List<IdnVocabulary> selectWordsList(IdnVocabulary words);

    IdnVocabulary selectWordsById(Long wordsId);

    IdnVocabulary checkWordsNameUnique(@Param("wordsName") String wordsName, @Param("parentId") Long parentId);

    int insertWords(IdnVocabulary words);

    int selectNormalChildrenWordsById(Long wordsId);

    List<IdnVocabulary> selectChildrenWordsById(Long wordsId);

    void updateWordsChildren(@Param("words") List<IdnVocabulary> words);

    int updateWords(IdnVocabulary words);

    void updateWordsStatus(IdnVocabulary words);

    int deleteWordsById(Long wordsId);

    int hasChildByWordsId(Long wordsId);

    int checkWordsExistUser(Long wordsId);
}
