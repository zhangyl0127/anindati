package com.ruoyi.system.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.constant.WordConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnOneWord;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnVocabulary;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnWordDescription;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.OneWordMapper;
import com.ruoyi.system.mapper.VocabularyMapper;
import com.ruoyi.system.mapper.WordDescriptionMapper;
import com.ruoyi.system.service.IOneWordService;
import com.ruoyi.system.service.IVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class OneWordServiceImpl extends ServiceImpl<OneWordMapper, IdnOneWord> implements IOneWordService{
    @Resource
    OneWordMapper oneWordMapper;
    @Resource
    VocabularyMapper vocabularyMapper;
    @Resource
    WordDescriptionMapper descriptionMapper;
    @Override
   // @DataScope(deptAlias = "d", userAlias = "u")
    public List<IdnOneWord> selectOneWordList(IdnOneWord oneWord) {
        List<IdnOneWord> list = oneWordMapper.selectOneWordList(oneWord);
        if (null!=list){
            IdnWordDescription idnWordDescription = null;
            for (IdnOneWord idnOneWord : list) {
                IdnVocabulary words = vocabularyMapper.selectWordsById(idnOneWord.getWordsId());
                idnOneWord.setWords(words);
                idnWordDescription = new IdnWordDescription();
                idnWordDescription.setOneWordId(idnOneWord.getOneWordId());
                List<IdnWordDescription> descriptions = descriptionMapper.selectByDescription(idnWordDescription);
                idnOneWord.setDescription(descriptions);
            }

        }
        return list;
    }

    @Override
    public IdnOneWord selectOneWordById(Long oneWordId)
    {
        IdnOneWord idnOneWord = oneWordMapper.selectOneWordById(oneWordId);
        return idnOneWord;
    }

    @Override
    public String checkWordsNameUnique(String oneWordName){
        return oneWordMapper.checkWordsNameUnique(oneWordName)>0?WordConstants.NOT_UNIQUE:WordConstants.UNIQUE;
    }

    @Transactional
    @Override
    public int insertOneWord(IdnOneWord oneWord) {
        // 新增单词信息
        return oneWordMapper.insertOneWord(oneWord);
    }

    @Override
    public int updateOneWord(IdnOneWord oneWord) {
        try {
            IdnWordDescription d = new IdnWordDescription();
            d.setOneWordId(oneWord.getOneWordId());
            List<IdnWordDescription> wordDescriptions = descriptionMapper.selectByDescription(d);
            IdnOneWord oldOneWord = oneWordMapper.selectOneWordById(oneWord.getOneWordId());
            if (null==wordDescriptions){
                d.setUpdateNum(1);
                d.setDescription(oldOneWord.getDescriptionNow());
                d.setCreateBy(oldOneWord.getCreateBy());
                d.setOneWordId(oldOneWord.getOneWordId());
                d.setOneWordName(oldOneWord.getOneWordName());
                d.setRemark(oldOneWord.getRemark());
            } else {
                d.setUpdateNum(wordDescriptions.size()+1);
                d.setDescription(oldOneWord.getDescriptionNow());
                d.setUpdateBy(SecurityUtils.getUsername());
                d.setOneWordId(oneWord.getOneWordId());
                d.setOneWordName(oldOneWord.getOneWordName());
                d.setRemark(oldOneWord.getRemark());
            }
            descriptionMapper.insertWordDescription(d);
            return oneWordMapper.updateOneWord(oneWord);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteOneWordByIds(Long[] oneWordIds) {
        descriptionMapper.deleteWordDescriptionByIds(oneWordIds);
        return oneWordMapper.deleteOneWordByIds(oneWordIds);
    }

    @Override
    public String importOneWord(List<IdnOneWord> oneWordList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(oneWordList) || oneWordList.size() == 0)
        {
            throw new CustomException("导入单词数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (IdnOneWord oneWord : oneWordList)
        {
            try
            {
                // 验证是否存在这个用户
                IdnOneWord u = oneWordMapper.selectOneWordByOneWordName(oneWord.getOneWordName());
                if (StringUtils.isNull(u))
                {
                    oneWord.setCreateBy(operName);
                    this.insertOneWord(oneWord);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、单词 " + oneWord.getOneWordName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    oneWord.setUpdateBy(operName);
                    this.updateOneWord(oneWord);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、单词 " + oneWord.getOneWordName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + oneWord.getOneWordName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + oneWord.getOneWordName()+ " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();

    }

    @Override
    public int updateOneWordStatus(IdnOneWord oneWord) {
        return oneWordMapper.updateOneWord(oneWord);
    }
}
