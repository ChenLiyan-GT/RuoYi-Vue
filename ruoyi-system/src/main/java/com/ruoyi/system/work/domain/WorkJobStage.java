package com.ruoyi.system.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业阶段表 work_job_stage
 * 
 * @author ruoyi
 */
public class WorkJobStage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 作业阶段 ID */
    @Excel(name = "作业阶段 ID", cellType = ColumnType.NUMERIC)
    private Long jobStageId;

    /** 作业 ID */
    @Excel(name = "作业 ID", cellType = ColumnType.NUMERIC)
    private Long jobId;

    /** 阶段编码 */
    @Excel(name = "阶段编码")
    private String stageCode;

    /** 阶段名称 */
    @Excel(name = "阶段名称")
    private String stageName;

    /** 阶段顺序 */
    @Excel(name = "阶段顺序", cellType = ColumnType.NUMERIC)
    private Integer stageOrder;

    /** 计划工时 */
    @Excel(name = "计划工时", cellType = ColumnType.NUMERIC)
    private BigDecimal plannedWorkload;

    /** 实际工时 */
    @Excel(name = "实际工时", cellType = ColumnType.NUMERIC)
    private BigDecimal actualWorkload;

    /** 最低级别要求 */
    @Excel(name = "最低级别要求", cellType = ColumnType.NUMERIC)
    private Integer minLevel;

    /** 技能要求 (JSON) */
    @Excel(name = "技能要求")
    private String requiredSkills;

    /** 状态 (0 未开始 1 进行中 2 已完成) */
    @Excel(name = "状态", readConverterExp = "0=未开始，1=进行中，2=已完成")
    private String status;

    /** 已分配员工 (JSON) */
    @Excel(name = "已分配员工")
    private String assignedEmployees;

    /** 开始日期 */
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 进度 (%) */
    @Excel(name = "进度", cellType = ColumnType.NUMERIC)
    private BigDecimal progress;

    public Long getJobStageId()
    {
        return jobStageId;
    }

    public void setJobStageId(Long jobStageId)
    {
        this.jobStageId = jobStageId;
    }

    public Long getJobId()
    {
        return jobId;
    }

    public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }

    public String getStageCode()
    {
        return stageCode;
    }

    public void setStageCode(String stageCode)
    {
        this.stageCode = stageCode;
    }

    public String getStageName()
    {
        return stageName;
    }

    public void setStageName(String stageName)
    {
        this.stageName = stageName;
    }

    public Integer getStageOrder()
    {
        return stageOrder;
    }

    public void setStageOrder(Integer stageOrder)
    {
        this.stageOrder = stageOrder;
    }

    public BigDecimal getPlannedWorkload()
    {
        return plannedWorkload;
    }

    public void setPlannedWorkload(BigDecimal plannedWorkload)
    {
        this.plannedWorkload = plannedWorkload;
    }

    public BigDecimal getActualWorkload()
    {
        return actualWorkload;
    }

    public void setActualWorkload(BigDecimal actualWorkload)
    {
        this.actualWorkload = actualWorkload;
    }

    public Integer getMinLevel()
    {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel)
    {
        this.minLevel = minLevel;
    }

    public String getRequiredSkills()
    {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills)
    {
        this.requiredSkills = requiredSkills;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getAssignedEmployees()
    {
        return assignedEmployees;
    }

    public void setAssignedEmployees(String assignedEmployees)
    {
        this.assignedEmployees = assignedEmployees;
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

    public BigDecimal getProgress()
    {
        return progress;
    }

    public void setProgress(BigDecimal progress)
    {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobStageId", getJobStageId())
            .append("jobId", getJobId())
            .append("stageCode", getStageCode())
            .append("stageName", getStageName())
            .append("stageOrder", getStageOrder())
            .append("plannedWorkload", getPlannedWorkload())
            .append("actualWorkload", getActualWorkload())
            .append("minLevel", getMinLevel())
            .append("requiredSkills", getRequiredSkills())
            .append("status", getStatus())
            .append("assignedEmployees", getAssignedEmployees())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("progress", getProgress())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}