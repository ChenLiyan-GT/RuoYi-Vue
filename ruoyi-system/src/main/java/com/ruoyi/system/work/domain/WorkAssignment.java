package com.ruoyi.system.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业分配表 work_assignment
 * 
 * @author ruoyi
 */
public class WorkAssignment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分配 ID */
    @Excel(name = "分配 ID", cellType = ColumnType.NUMERIC)
    private Long assignId;

    /** 作业阶段 ID */
    @Excel(name = "作业阶段 ID", cellType = ColumnType.NUMERIC)
    private Long jobStageId;

    /** 员工 ID */
    @Excel(name = "员工 ID", cellType = ColumnType.NUMERIC)
    private Long employeeId;

    /** 分配工时 */
    @Excel(name = "分配工时", cellType = ColumnType.NUMERIC)
    private BigDecimal workload;

    /** 状态 (0 进行中 1 已完成 2 已取消) */
    @Excel(name = "状态", readConverterExp = "0=进行中，1=已完成，2=已取消")
    private String status;

    /** 分配人 */
    @Excel(name = "分配人")
    private String assignBy;

    /** 分配时间 */
    @Excel(name = "分配时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date assignTime;

    /** 完成时间 */
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 删除标志 */
    private String delFlag;

    public Long getAssignId()
    {
        return assignId;
    }

    public void setAssignId(Long assignId)
    {
        this.assignId = assignId;
    }

    public Long getJobStageId()
    {
        return jobStageId;
    }

    public void setJobStageId(Long jobStageId)
    {
        this.jobStageId = jobStageId;
    }

    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    public BigDecimal getWorkload()
    {
        return workload;
    }

    public void setWorkload(BigDecimal workload)
    {
        this.workload = workload;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getAssignBy()
    {
        return assignBy;
    }

    public void setAssignBy(String assignBy)
    {
        this.assignBy = assignBy;
    }

    public Date getAssignTime()
    {
        return assignTime;
    }

    public void setAssignTime(Date assignTime)
    {
        this.assignTime = assignTime;
    }

    public Date getCompleteTime()
    {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime)
    {
        this.completeTime = completeTime;
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
            .append("assignId", getAssignId())
            .append("jobStageId", getJobStageId())
            .append("employeeId", getEmployeeId())
            .append("workload", getWorkload())
            .append("status", getStatus())
            .append("assignBy", getAssignBy())
            .append("assignTime", getAssignTime())
            .append("completeTime", getCompleteTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}