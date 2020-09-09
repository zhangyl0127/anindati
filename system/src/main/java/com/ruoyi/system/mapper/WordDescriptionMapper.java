package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnWordDescription;

import java.util.List;

public interface WordDescriptionMapper extends BaseMapper<IdnWordDescription> {
    List<IdnWordDescription>  selectByDescription(IdnWordDescription idnWordDescription);

    int insertWordDescription(IdnWordDescription wordDescription);

    int deleteWordDescriptionByIds(Long[] oneWordIds);
}
