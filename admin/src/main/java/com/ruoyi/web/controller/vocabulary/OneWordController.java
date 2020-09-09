package com.ruoyi.web.controller.vocabulary;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.constant.WordConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnOneWord;
import com.ruoyi.common.core.domain.entity.vocabulary.IdnWordDescription;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.IOneWordService;
import com.ruoyi.system.service.IWordDescriptionService;
import io.swagger.annotations.Api;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单个单词管理
 */
@RestController
@RequestMapping("/oneword/build")
@Api(tags = "单词管理")
public class OneWordController extends BaseController{

    @Resource
    IOneWordService oneWordService;
    @Resource
    IWordDescriptionService descriptionService;
    @Autowired
    TokenService tokenService;
    /** 查询单个单词信息*/
    @PreAuthorize("@ss.hasPermi('vocabulary:build:list')")
    @GetMapping("/list")
    public TableDataInfo list(IdnOneWord oneWord)
    {
        startPage();
        List<IdnOneWord> list = oneWordService.selectOneWordList(oneWord);
        return getDataTable(list);
    }

    /**
     * 根据单个单词ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:build:query')")
    @GetMapping(value = { "/", "/{oneWordId}" })
    public AjaxResult getInfo(@PathVariable(value = "oneWordId", required = false) Long oneWordId)
    {
        AjaxResult ajax = AjaxResult.success();
/*        IdnWordDescription idnWordDescription = new IdnWordDescription();
        idnWordDescription.setOneWordId(oneWordId);
        List<IdnWordDescription> list = descriptionService.selectByDescription(idnWordDescription);*/

        if (StringUtils.isNotNull(oneWordId))
        {
            ajax.put(AjaxResult.DATA_TAG, oneWordService.selectOneWordById(oneWordId));
        }
        return ajax;
    }

    /**
     * 新增单个单词
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:build:add')")
    @Log(title = "单个单词管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody IdnOneWord oneWord) {
        if (WordConstants.NOT_UNIQUE.equals(oneWordService.checkWordsNameUnique(oneWord.getOneWordName()))) {
            return AjaxResult.error("新增单词'" + oneWord.getOneWordName() + "'失败，单词名称已存在");
        }

        oneWord.setCreateBy(SecurityUtils.getUsername());
        //oneWord.setCreateTime(new Date());
        return toAjax(oneWordService.insertOneWord(oneWord));
    }

    /**
     * 修改单个单词
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:build:edit')")
    @Log(title = "单个单词管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody IdnOneWord oneWord) {
/*         if (WordConstants.NOT_UNIQUE.equals(oneWordService.checkWordsNameUnique(oneWord.getOneWordName()))) {
            return AjaxResult.error("修改单词'" + oneWord.getOneWordName() + "'失败，单词名称已存在");
        }*/
        oneWord.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(oneWordService.updateOneWord(oneWord));
    }

    /**
     * 删除单个单词
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:build:remove')")
    @Log(title = "单个单词管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{oneWordIds}")
    public AjaxResult remove(@PathVariable Long[] oneWordIds)
    {
        return toAjax(oneWordService.deleteOneWordByIds(oneWordIds));
    }


    @Log(title = "单个单词管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('vocabulary:build:export')")
    @GetMapping("/export")
    public AjaxResult export(IdnOneWord oneWord)
    {
        List<IdnOneWord> list = oneWordService.selectOneWordList(oneWord);
        ExcelUtil<IdnOneWord> util = new ExcelUtil<IdnOneWord>(IdnOneWord.class);
        AjaxResult r = util.exportExcel(list, "单词数据");
        return util.exportExcel(list, "单词数据");
    }

    @Log(title = "单个单词管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('vocabulary:build:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<IdnOneWord> util = new ExcelUtil<IdnOneWord>(IdnOneWord.class);
        List<IdnOneWord> oneWordList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = oneWordService.importOneWord(oneWordList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<IdnOneWord> util = new ExcelUtil<IdnOneWord>(IdnOneWord.class);
        return util.importTemplateExcel("单词数据");
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('vocabulary:build:edit')")
    @Log(title = "单个单词管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody IdnOneWord oneWord)
    {
        oneWord.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(oneWordService.updateOneWordStatus(oneWord));
    }

}
