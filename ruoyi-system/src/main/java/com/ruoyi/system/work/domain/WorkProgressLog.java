package com.ruoyi.system.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;

/**
 * 进度更新记录表 work_progress_log
 * 
 * @author ruoyi
 */
public class WorkProgressLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志 ID */
    @Excel(name = "日志 ID", cellType = ColumnType.NUMERIC)
    private Long logId;

    /** 作业阶段 ID */
    @Excel(name = "作业阶段 ID", cellType = ColumnType.NUMERIC)
    private Long jobStageId;

    /** 员工 ID */
    @Excel(name = "员工 ID", cellType = ColumnType.NUMERIC)
    private Long employeeId;

    /** 进度 (%) */
    @Excel(name = "进度", cellType = ColumnType.NUMERIC)
    private BigDecimal progress;

    /** 上次进度 */
    @Excel(name = "上次进度", cellType = ColumnType.NUMERIC)
    private BigDecimal prevProgress;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public Long getLogId()
    {
        return logId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
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

    public BigDecimal getProgress()
    {
        return progress;
    }

    public void setProgress(BigDecimal progress)
    {
        this.progress = progress;
    }

    public BigDecimal getPrevProgress()
    {
        return prevProgress;
    }

    public void setPrevProgress(BigDecimal prevProgress)
    {
        this.prevProgress = prevProgress;
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
            .append("logId", getLogId())
            .append("jobStageId", getJobStageId())
            .append("employeeId", getEmployeeId())
            .append("progress", getProgress())
            .append("prevProgress", getPrevProgress())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .toString();
    }
}