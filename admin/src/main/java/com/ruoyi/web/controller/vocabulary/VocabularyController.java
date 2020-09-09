package com.ruoyi.web.controller.vocabulary;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnVocabulary;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IVocabularyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * 词汇分类管理
 */
@RestController
@RequestMapping("/vocabulary")
@Api(tags = "词汇管理")
public class VocabularyController extends BaseController {

    @Autowired
    IVocabularyService vocabularyService;
    /**
     * 获取词汇分类表列表
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:handle:list')")
    @GetMapping("/handle/list")
    @ApiOperation(value = "获取词汇分类表列表")
    public AjaxResult list(IdnVocabulary word)
    {
        List<IdnVocabulary> words= vocabularyService.selectWordsList(word);
        return AjaxResult.success(words);
    }

    /**
     * 根据词汇分类编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:handle:query')")
    @GetMapping(value = "/handle/{wordsId}")
    @ApiOperation(value = "根据词汇分类编号获取详细信息")
    public AjaxResult getInfo(@PathVariable Long wordsId)
    {
        return AjaxResult.success(vocabularyService.selectWordsById(wordsId));
    }

    /**
     * 查询词汇分类列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:handle:list')")
    @GetMapping("/handle/list/exclude/{wordsId}")
    @ApiOperation(value = "查询词汇分类列表（排除节点）")
    public AjaxResult excludeChild(@PathVariable(value = "wordsId", required = false) Long wordsId)
    {
        List<IdnVocabulary> wordsList = vocabularyService.selectWordsList(new IdnVocabulary());
        Iterator<IdnVocabulary> it = wordsList.iterator();
        while (it.hasNext())
        {
            IdnVocabulary d = (IdnVocabulary) it.next();
            if (d.getWordsId().intValue() == wordsId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), wordsId + ""))
            {
                it.remove();
            }
        }
        return AjaxResult.success(wordsList);
    }

    /**
     * 新增分类
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:handle:add')")
    @Log(title = "分类管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增分类")
    public AjaxResult add(@Validated @RequestBody IdnVocabulary words)
    {
        if (UserConstants.NOT_UNIQUE.equals(vocabularyService.checkWordsNameUnique(words)))
        {
            return AjaxResult.error("新增分类'" + words.getWordsName() + "'失败，分类名称已存在");
        }
        words.setCreateBy(SecurityUtils.getUsername());
        return toAjax(vocabularyService.insertWords(words));
    }

    /**
     * 修改分类
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:handle:update')")
    @Log(title = "分类管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改分类")
    public AjaxResult edit(@Validated @RequestBody IdnVocabulary words)
    {
        if (UserConstants.NOT_UNIQUE.equals(vocabularyService.checkWordsNameUnique(words)))
        {
            return AjaxResult.error("修改分类'" + words.getWordsName() + "'失败，分类名称已存在");
        }
        else if (words.getParentId().equals(words.getWordsId()))
        {
            return AjaxResult.error("修改分类'" + words.getWordsName() + "'失败，上级分类不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, words.getStatus())
                && vocabularyService.selectNormalChildrenWordsById(words.getWordsId()) > 0)
        {
            return AjaxResult.error("该分类包含未停用的子分类！");
        }
        words.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(vocabularyService.updateWords(words));
    }

    /**
     * 删除分类
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:handle:remove')")
    @Log(title = "分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/handle/{wordsId}")
    @ApiOperation(value = "删除分类")
    public AjaxResult remove(@PathVariable Long wordsId)
    {
        if (vocabularyService.hasChildByWordsId(wordsId))
        {
            return AjaxResult.error("存在下级分类,不允许删除");
        }
/*        if (vocabularyService.checkWordsExistUser(wordsId))
        {
            return AjaxResult.error("分类存在用户,不允许删除");
        }*/
        return toAjax(vocabularyService.deleteWordsById(wordsId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/handle/treeselect")
    public AjaxResult treeselect(IdnVocabulary words)
    {
        List<IdnVocabulary> wordsList = vocabularyService.selectWordsList(words);
        return AjaxResult.success(vocabularyService.buildWordsTreeSelect(wordsList));
    }

    /**
     * 加载对应角色部门列表树
     */
/*    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }*/
}
