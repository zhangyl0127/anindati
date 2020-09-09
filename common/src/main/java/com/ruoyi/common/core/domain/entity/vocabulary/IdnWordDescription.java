package com.ruoyi.common.core.domain.entity.vocabulary;

import com.ruoyi.common.core.domain.BaseEntity;
public class IdnWordDescription extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 单词描述ID */
    private Long descriptionId;

    /** 一个单词id（idn_one_word主键) */
    private Long oneWordId;

    /** 第N次修改的单词名称 */
    private String oneWordName;

    /** 第N次修改的单词 */
    private Integer updateNum;

    /** 状态:0正常,1停用 */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 当前描述内容 */
    private String description;

    public Long getDescriptionId() {
        return descriptionId;
    }

    public IdnWordDescription setDescriptionId(Long descriptionId) {
        this.descriptionId = descriptionId;
        return this;
    }

    public Long getOneWordId() {
        return oneWordId;
    }

    public IdnWordDescription setOneWordId(Long oneWordId) {
        this.oneWordId = oneWordId;
        return this;
    }

    public Integer getUpdateNum() {
        return updateNum;
    }

    public IdnWordDescription setUpdateNum(Integer updateNum) {
        this.updateNum = updateNum;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public IdnWordDescription setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public IdnWordDescription setDelFlag(String delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public String getOneWordName() {
        return oneWordName;
    }

    public IdnWordDescription setOneWordName(String oneWordName) {
        this.oneWordName = oneWordName;
        return this;
    }
    public String getDescription() {
        return description;
    }

    public IdnWordDescription setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "IdnWordDescription{" +
                "descriptionId=" + descriptionId +
                ", oneWordId=" + oneWordId +
                ", updateNum=" + updateNum +
                ", status='" + status + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
