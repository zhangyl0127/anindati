package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnOneWord;
import java.util.List;

public interface OneWordMapper extends BaseMapper<IdnOneWord> {
    List<IdnOneWord> selectOneWordList(IdnOneWord oneWord);

    IdnOneWord selectOneWordById(Long oneWordId);

    int checkWordsNameUnique(String oneWordName);

    int insertOneWord(IdnOneWord oneWord);

    int updateOneWord(IdnOneWord oneWord);

    int deleteOneWordByIds(Long[] oneWordIds);

    IdnOneWord selectOneWordByOneWordName(String oneWordName);
}
