package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnOneWord;
import java.util.List;

public interface IOneWordService extends IService<IdnOneWord> {
    List<IdnOneWord> selectOneWordList(IdnOneWord oneWord);

    IdnOneWord selectOneWordById(Long oneWordId);

    String checkWordsNameUnique(String oneWordName);

    int insertOneWord(IdnOneWord oneWord);

    int updateOneWord(IdnOneWord oneWord);

    int deleteOneWordByIds(Long[] oneWordIds);

    String importOneWord(List<IdnOneWord> oneWordList, boolean isUpdateSupport, String operName);

    int updateOneWordStatus(IdnOneWord oneWord);
}
