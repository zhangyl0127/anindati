package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnOneWord;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnWordDescription;
import com.ruoyi.system.mapper.WordDescriptionMapper;
import com.ruoyi.system.service.IWordDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WordDescriptionServiceImpl extends ServiceImpl<WordDescriptionMapper, IdnWordDescription> implements IWordDescriptionService {

    @Resource
    WordDescriptionMapper descriptionMapper;

    @Override
    public List<IdnWordDescription> selectByDescription(IdnWordDescription idnWordDescription)
    {
        List<IdnWordDescription> IdnWordDescription = descriptionMapper.selectByDescription(idnWordDescription);
        return IdnWordDescription;
    }
}
