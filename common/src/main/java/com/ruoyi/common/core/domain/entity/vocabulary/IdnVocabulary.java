package com.ruoyi.common.core.domain.entity.vocabulary;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysDept;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class IdnVocabulary extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 词汇ID */
    private Long wordsId;

    /** 父部门ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 单词名称 */
    private String wordsName;

    /** 显示顺序 */
    private String orderNum;

    /** 状态:0正常,1停用 */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 父部门名称 */
    private String parentName;

    /** 子部门 */
    private List<IdnVocabulary> children = new ArrayList<IdnVocabulary>();

    public Long getWordsId() {
        return wordsId;
    }

    public void setWordsId(Long wordsId) {
        this.wordsId = wordsId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "单词名称不能为空")
    @Size(min = 0, max = 30, message = "单词名称长度不能超过30个字符")
    public String getWordsName() {
        return wordsName;
    }

    public void setWordsName(String wordsName) {
        this.wordsName = wordsName;
    }

    @NotBlank(message = "显示顺序不能为空")
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<IdnVocabulary> getChildren() {
        return children;
    }

    public void setChildren(List<IdnVocabulary> children) {
        this.children = children;
    }
}
