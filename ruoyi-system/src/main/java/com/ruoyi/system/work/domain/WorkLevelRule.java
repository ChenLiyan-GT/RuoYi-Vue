package com.ruoyi.system.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 级别规则表 work_level_rule
 * 
 * @author ruoyi
 */
public class WorkLevelRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 规则 ID */
    @Excel(name = "规则 ID")
    private Long ruleId;

    /** 职能类型 ID */
    @Excel(name = "职能类型 ID")
    private Long positionTypeId;

    /** 职能类型名称 */
    @Excel(name = "职能类型")
    private String typeName;

    /** 职级 */
    @Excel(name = "职级")
    private Integer level;

    /** 级别名称 */
    @Excel(name = "级别名称")
    private String levelName;

    /** 可执行最低阶段 */
    @Excel(name = "可执行最低阶段")
    private String minStage;

    /** 可执行最高阶段 */
    @Excel(name = "可执行最高阶段")
    private String maxStage;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 状态 (0 正常 1 停用) */
    @Excel(name = "状态")
    private String status;

    /** 删除标志 */
    private String delFlag;

    public Long getRuleId()
    {
        return ruleId;
    }

    public void setRuleId(Long ruleId)
    {
        this.ruleId = ruleId;
    }

    public Long getPositionTypeId()
    {
        return positionTypeId;
    }

    public void setPositionTypeId(Long positionTypeId)
    {
        this.positionTypeId = positionTypeId;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getLevelName()
    {
        return levelName;
    }

    public void setLevelName(String levelName)
    {
        this.levelName = levelName;
    }

    public String getMinStage()
    {
        return minStage;
    }

    public void setMinStage(String minStage)
    {
        this.minStage = minStage;
    }

    public String getMaxStage()
    {
        return maxStage;
    }

    public void setMaxStage(String maxStage)
    {
        this.maxStage = maxStage;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ruleId", getRuleId())
            .append("positionTypeId", getPositionTypeId())
            .append("typeName", getTypeName())
            .append("level", getLevel())
            .append("levelName", getLevelName())
            .append("minStage", getMinStage())
            .append("maxStage", getMaxStage())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}