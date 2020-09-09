package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnVocabulary;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.VocabularyMapper;
import com.ruoyi.system.service.IVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocabularyServiceImpl extends ServiceImpl<VocabularyMapper, IdnVocabulary> implements IVocabularyService {
    @Autowired
    VocabularyMapper vocabularyMapper;

    @Override
    public List<IdnVocabulary> selectWordsList(IdnVocabulary word) {
        return vocabularyMapper.selectWordsList(word);
    }

    @Override
    public IdnVocabulary selectWordsById(Long wordsId) {
       return vocabularyMapper.selectWordsById(wordsId);
    }

    @Override
    public String checkWordsNameUnique(IdnVocabulary words) {
        Long wordsId = StringUtils.isNull(words.getWordsId()) ? -1L : words.getWordsId();
        IdnVocabulary info = vocabularyMapper.checkWordsNameUnique(words.getWordsName(), words.getParentId());
        if (StringUtils.isNotNull(info) && info.getWordsId().longValue() != wordsId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertWords(IdnVocabulary words) {
        IdnVocabulary info = vocabularyMapper.selectWordsById(words.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new CustomException("部门停用，不允许新增");
        }
        words.setAncestors(info.getAncestors() + "," + words.getParentId());
        return vocabularyMapper.insertWords(words);
    }

    @Override
    public int selectNormalChildrenWordsById(Long wordsId) {
        return vocabularyMapper.selectNormalChildrenWordsById(wordsId);
    }

    @Override
    public int updateWords(IdnVocabulary words) {
        IdnVocabulary newParentWords = vocabularyMapper.selectWordsById(words.getParentId());
        IdnVocabulary oldWords = vocabularyMapper.selectWordsById(words.getWordsId());
        if (StringUtils.isNotNull(newParentWords) && StringUtils.isNotNull(oldWords))
        {
            String newAncestors = newParentWords.getAncestors() + "," + newParentWords.getWordsId();
            String oldAncestors = oldWords.getAncestors();
            words.setAncestors(newAncestors);
            updateWordsChildren(words.getWordsId(), newAncestors, oldAncestors);
        }
        int result = vocabularyMapper.updateWords(words);
        if (UserConstants.DEPT_NORMAL.equals(words.getStatus()))
        {
            // 如果该分类是启用状态，则启用该分类的所有上级分类
            updateParentWordsStatus(words);
        }
        return result;
    }

    @Override
    public boolean hasChildByWordsId(Long wordsId) {
        int result = vocabularyMapper.hasChildByWordsId(wordsId);
        return result > 0 ? true : false;
    }

    @Override
    public boolean checkWordsExistUser(Long wordsId) {
        int result = vocabularyMapper.checkWordsExistUser(wordsId);
        return result > 0 ? true : false;
    }

    @Override
    public int deleteWordsById(Long wordsId) {
        return vocabularyMapper.deleteWordsById(wordsId);
    }

    @Override
    public List<TreeSelect> buildWordsTreeSelect(List<IdnVocabulary> wordsList) {
        List<IdnVocabulary> wordsTrees = buildWordsTree(wordsList);
        return wordsTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public void updateWordsChildren(Long wordsId, String newAncestors, String oldAncestors) {
        List<IdnVocabulary> children = vocabularyMapper.selectChildrenWordsById(wordsId);
        for (IdnVocabulary child : children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            vocabularyMapper.updateWordsChildren(children);
        }
    }

    private void updateParentWordsStatus(IdnVocabulary words) {
        String updateBy = words.getUpdateBy();
        words = vocabularyMapper.selectWordsById(words.getWordsId());
        words.setUpdateBy(updateBy);
        vocabularyMapper.updateWordsStatus(words);
    }

    @Override
    public List<IdnVocabulary> buildWordsTree(List<IdnVocabulary> words)
    {
        List<IdnVocabulary> returnList = new ArrayList<IdnVocabulary>();
        List<Long> tempList = new ArrayList<Long>();
        for (IdnVocabulary word : words)
        {
            tempList.add(word.getWordsId());
        }
        for (Iterator<IdnVocabulary> iterator = words.iterator(); iterator.hasNext();)
        {
            IdnVocabulary word = (IdnVocabulary) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(word.getParentId()))
            {
                recursionFn(words, word);
                returnList.add(word);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = words;
        }
        return returnList;
    }

    private void recursionFn(List<IdnVocabulary> list, IdnVocabulary t)
    {
        // 得到子节点列表
        List<IdnVocabulary> childList = getChildList(list, t);
        t.setChildren(childList);
        for (IdnVocabulary tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<IdnVocabulary> it = childList.iterator();
                while (it.hasNext())
                {
                    IdnVocabulary n = (IdnVocabulary) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    private List<IdnVocabulary> getChildList(List<IdnVocabulary> list, IdnVocabulary t)
    {
        List<IdnVocabulary> tlist = new ArrayList<IdnVocabulary>();
        Iterator<IdnVocabulary> it = list.iterator();
        while (it.hasNext())
        {
            IdnVocabulary n = (IdnVocabulary) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getWordsId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    private boolean hasChild(List<IdnVocabulary> list, IdnVocabulary t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
