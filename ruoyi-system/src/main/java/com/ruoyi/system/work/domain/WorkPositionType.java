package com.ruoyi.system.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 职能类型表 work_position_type
 * 
 * @author ruoyi
 */
public class WorkPositionType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 类型 ID */
    @Excel(name = "类型 ID")
    private Long typeId;

    /** 类型编码 */
    @Excel(name = "类型编码")
    private String typeCode;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String typeName;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortOrder;

    /** 状态 (0 正常 1 停用) */
    @Excel(name = "状态", readConverterExp = "0=正常，1=停用")
    private String status;

    public Long getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public String getTypeCode()
    {
        return typeCode;
    }

    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("typeId", getTypeId())
            .append("typeCode", getTypeCode())
            .append("typeName", getTypeName())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}