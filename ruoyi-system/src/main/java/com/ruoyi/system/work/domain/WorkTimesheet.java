package com.ruoyi.system.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 工时记录表 work_timesheet
 * 
 * @author ruoyi
 */
public class WorkTimesheet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工时记录 ID */
    @Excel(name = "工时记录 ID", cellType = ColumnType.NUMERIC)
    private Long timesheetId;

    /** 分配 ID */
    @Excel(name = "分配 ID", cellType = ColumnType.NUMERIC)
    private Long assignId;

    /** 工作日期 */
    @Excel(name = "工作日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workDate;

    /** 工时数 */
    @Excel(name = "工时数", cellType = ColumnType.NUMERIC)
    private BigDecimal workload;

    /** 工作内容 */
    @Excel(name = "工作内容")
    private String content;

    /** 提交人 */
    @Excel(name = "提交人")
    private String submitBy;

    /** 提交时间 */
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 状态 (0 草稿 1 已提交 2 已审核) */
    @Excel(name = "状态", readConverterExp = "0=草稿，1=已提交，2=已审核")
    private String status;

    public Long getTimesheetId()
    {
        return timesheetId;
    }

    public void setTimesheetId(Long timesheetId)
    {
        this.timesheetId = timesheetId;
    }

    public Long getAssignId()
    {
        return assignId;
    }

    public void setAssignId(Long assignId)
    {
        this.assignId = assignId;
    }

    public Date getWorkDate()
    {
        return workDate;
    }

    public void setWorkDate(Date workDate)
    {
        this.workDate = workDate;
    }

    public BigDecimal getWorkload()
    {
        return workload;
    }

    public void setWorkload(BigDecimal workload)
    {
        this.workload = workload;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getSubmitBy()
    {
        return submitBy;
    }

    public void setSubmitBy(String submitBy)
    {
        this.submitBy = submitBy;
    }

    public Date getSubmitTime()
    {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
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
            .append("timesheetId", getTimesheetId())
            .append("assignId", getAssignId())
            .append("workDate", getWorkDate())
            .append("workload", getWorkload())
            .append("content", getContent())
            .append("submitBy", getSubmitBy())
            .append("submitTime", getSubmitTime())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}