package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnWordDescription;

import java.util.List;

public interface IWordDescriptionService extends IService<IdnWordDescription> {

    List<IdnWordDescription>  selectByDescription(IdnWordDescription idnWordDescription);
}
