package com.ruoyi.system.work.domain;

import jakarta.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业主表 work_job
 * 
 * @author ruoyi
 */
public class WorkJob extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 作业 ID */
    @Excel(name = "作业 ID", cellType = ColumnType.NUMERIC)
    private Long jobId;

    /** 作业编号 */
    @Excel(name = "作业编号")
    private String jobNo;

    /** 作业名称 */
    @Excel(name = "作业名称")
    private String jobName;

    /** 部门 ID */
    @Excel(name = "部门 ID", cellType = ColumnType.NUMERIC)
    private Long deptId;

    /** 阶段模板 ID */
    @Excel(name = "阶段模板 ID", cellType = ColumnType.NUMERIC)
    private Long templateId;

    /** 总工时 */
    @Excel(name = "总工时", cellType = ColumnType.NUMERIC)
    private BigDecimal totalWorkload;

    /** 状态 (0 未开始 1 进行中 2 已完成 3 已取消) */
    @Excel(name = "状态", readConverterExp = "0=未开始，1=进行中，2=已完成，3=已取消")
    private String status;

    /** 计划开始日期 */
    @Excel(name = "计划开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 计划结束日期 */
    @Excel(name = "计划结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 实际结束日期 */
    @Excel(name = "实际结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualEndDate;

    /** 整体进度 (%) */
    @Excel(name = "整体进度", cellType = ColumnType.NUMERIC)
    private BigDecimal progress;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public Long getJobId()
    {
        return jobId;
    }

    public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }

    @NotBlank(message = "作业编号不能为空")
    @Size(min = 0, max = 32, message = "作业编号长度不能超过 32 个字符")
    public String getJobNo()
    {
        return jobNo;
    }

    public void setJobNo(String jobNo)
    {
        this.jobNo = jobNo;
    }

    @NotBlank(message = "作业名称不能为空")
    @Size(min = 0, max = 128, message = "作业名称长度不能超过 128 个字符")
    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    @NotNull(message = "部门 ID 不能为空")
    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @NotNull(message = "阶段模板 ID 不能为空")
    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    @NotNull(message = "总工时不能为空")
    @DecimalMin(value = "0.01", message = "总工时必须大于 0")
    public BigDecimal getTotalWorkload()
    {
        return totalWorkload;
    }

    public void setTotalWorkload(BigDecimal totalWorkload)
    {
        this.totalWorkload = totalWorkload;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getActualEndDate()
    {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate)
    {
        this.actualEndDate = actualEndDate;
    }

    public BigDecimal getProgress()
    {
        return progress;
    }

    public void setProgress(BigDecimal progress)
    {
        this.progress = progress;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobId", getJobId())
            .append("jobNo", getJobNo())
            .append("jobName", getJobName())
            .append("deptId", getDeptId())
            .append("templateId", getTemplateId())
            .append("totalWorkload", getTotalWorkload())
            .append("status", getStatus())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("actualEndDate", getActualEndDate())
            .append("progress", getProgress())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}