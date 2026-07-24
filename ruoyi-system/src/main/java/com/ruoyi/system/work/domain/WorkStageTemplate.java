package com.ruoyi.system.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 作业阶段模板表 work_stage_template
 * 
 * @author ruoyi
 */
public class WorkStageTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板 ID */
    @Excel(name = "模板 ID")
    private Long templateId;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String templateName;

    /** 适用部门 ID(NULL 通用) */
    @Excel(name = "适用部门 ID")
    private Long deptId;

    /** 适用部门名(NULL 通用) */
    @Excel(name = "适用部门名")
    private String deptName;

    /** 阶段配置 (JSON) */
    @Excel(name = "阶段配置")
    private String stagesConfig;

    /** 是否默认 (0 否 1 是) */
    @Excel(name = "是否默认", readConverterExp = "0=否，1=是")
    private String isDefault;

    /** 版本号 */
    @Excel(name = "版本号")
    private Integer version;

    /** 使用次数 */
    @Excel(name = "使用次数")
    private Integer usageCount;

    /** 状态 (0 启用 1 禁用) */
    @Excel(name = "状态", readConverterExp = "0=启用，1=禁用")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 删除标志 */
    private String delFlag;

    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getStagesConfig()
    {
        return stagesConfig;
    }

    public void setStagesConfig(String stagesConfig)
    {
        this.stagesConfig = stagesConfig;
    }

    public String getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(String isDefault)
    {
        this.isDefault = isDefault;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public Integer getUsageCount()
    {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount)
    {
        this.usageCount = usageCount;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
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
            .append("templateId", getTemplateId())
            .append("templateName", getTemplateName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("stagesConfig", getStagesConfig())
            .append("isDefault", getIsDefault())
            .append("version", getVersion())
            .append("usageCount", getUsageCount())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
