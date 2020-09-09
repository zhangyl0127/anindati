package com.ruoyi.common.core.domain.entity.vocabulary;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.ArrayList;
import java.util.List;

public class  IdnOneWord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 单个单词ID */
    @Excel(name = "单词编号",cellType = Excel.ColumnType.NUMERIC, prompt = "单词编号",type = Excel.Type.EXPORT)
    private Long oneWordId;

    /** 单词名称*/
    @Excel(name = "单词名称")
    private String oneWordName;

    /** 分类 */
    @Excel(name = "所属分类", targetAttr = "wordsName", type = Excel.Type.EXPORT)
    private IdnVocabulary words;

    /** 词汇分类ID */
    @Excel(name = "所属分类编号",type = Excel.Type.IMPORT)
    private Long wordsId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 当前描述内容 */
    @Excel(name = "详情描述",width = 50)
    private String descriptionNow;

    /** 状态:0正常,1停用 */
    @Excel(name = "审核状态", readConverterExp = "0=已审核,1=未审核,2=未知", type = Excel.Type.EXPORT)
    private String status;



    /** 每个单词所对应的之前修改过的内容 */
    private List<IdnWordDescription> description = new ArrayList<>();

    public Long getOneWordId() {
        return oneWordId;
    }

    public IdnOneWord setOneWordId(Long oneWordId) {
        this.oneWordId = oneWordId;
        return this;
    }

    public String getOneWordName() {
        return oneWordName;
    }

    public IdnOneWord setOneWordName(String oneWordName) {
        this.oneWordName = oneWordName;
        return this;
    }
    public Long getWordsId() {
        return wordsId;
    }

    public IdnOneWord setWordsId(Long wordsId) {
        this.wordsId = wordsId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public IdnOneWord setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public IdnOneWord setDelFlag(String delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public String getDescriptionNow() {
        return descriptionNow;
    }

    public IdnOneWord setDescriptionNow(String descriptionNow) {
        this.descriptionNow = descriptionNow;
        return this;
    }

    public IdnVocabulary getWords() {
        return words;
    }

    public IdnOneWord setWords(IdnVocabulary words) {
        this.words = words;
        return this;
    }

    public List<IdnWordDescription> getDescription() {
        return description;
    }

    public IdnOneWord setDescription(List<IdnWordDescription> description) {
        this.description = description;
        return this;
    }
}
