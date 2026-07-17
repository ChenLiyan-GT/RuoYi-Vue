package com.ruoyi.system.work.domain;

import jakarta.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;

/**
 * 员工信息表 work_employee
 * 
 * @author ruoyi
 */
public class WorkEmployee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工 ID */
    @Excel(name = "员工 ID", cellType = ColumnType.NUMERIC)
    private Long employeeId;

    /** 员工工号 */
    @Excel(name = "员工工号")
    private String employeeNo;

    /** 员工姓名 */
    @Excel(name = "员工姓名")
    private String employeeName;

    /** 部门 ID */
    @Excel(name = "部门 ID", cellType = ColumnType.NUMERIC)
    private Long deptId;

    /** 关联用户 ID */
    @Excel(name = "关联用户 ID", cellType = ColumnType.NUMERIC)
    private Long userId;

    /** 职能类型 */
    @Excel(name = "职能类型")
    private String positionType;

    /** 职级 (1-10) */
    @Excel(name = "职级", cellType = ColumnType.NUMERIC)
    private Integer positionLevel;

    /** 技能标签 (JSON) */
    @Excel(name = "技能标签")
    private String skills;

    /** 状态 (0 空闲 1 工作中 2 过载) */
    @Excel(name = "状态", readConverterExp = "0=空闲，1=工作中，2=过载")
    private String status;

    /** 当前工时负载 */
    @Excel(name = "当前工时负载", cellType = ColumnType.NUMERIC)
    private BigDecimal currentWorkload;

    /** 最大工时容量 */
    @Excel(name = "最大工时容量", cellType = ColumnType.NUMERIC)
    private BigDecimal maxWorkload;

    /** 删除标志 */
    private String delFlag;

    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    @NotBlank(message = "员工工号不能为空")
    @Size(min = 0, max = 32, message = "员工工号长度不能超过 32 个字符")
    public String getEmployeeNo()
    {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo)
    {
        this.employeeNo = employeeNo;
    }

    @NotBlank(message = "员工姓名不能为空")
    @Size(min = 0, max = 64, message = "员工姓名长度不能超过 64 个字符")
    public String getEmployeeName()
    {
        return employeeName;
    }

    public void setEmployeeName(String employeeName)
    {
        this.employeeName = employeeName;
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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    @NotBlank(message = "职能类型不能为空")
    @Size(min = 0, max = 32, message = "职能类型长度不能超过 32 个字符")
    public String getPositionType()
    {
        return positionType;
    }

    public void setPositionType(String positionType)
    {
        this.positionType = positionType;
    }

    @NotNull(message = "职级不能为空")
    @Min(value = 1, message = "职级最小值为 1")
    @Max(value = 10, message = "职级最大值为 10")
    public Integer getPositionLevel()
    {
        return positionLevel;
    }

    public void setPositionLevel(Integer positionLevel)
    {
        this.positionLevel = positionLevel;
    }

    public String getSkills()
    {
        return skills;
    }

    public void setSkills(String skills)
    {
        this.skills = skills;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public BigDecimal getCurrentWorkload()
    {
        return currentWorkload;
    }

    public void setCurrentWorkload(BigDecimal currentWorkload)
    {
        this.currentWorkload = currentWorkload;
    }

    public BigDecimal getMaxWorkload()
    {
        return maxWorkload;
    }

    public void setMaxWorkload(BigDecimal maxWorkload)
    {
        this.maxWorkload = maxWorkload;
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
            .append("employeeId", getEmployeeId())
            .append("employeeNo", getEmployeeNo())
            .append("employeeName", getEmployeeName())
            .append("deptId", getDeptId())
            .append("userId", getUserId())
            .append("positionType", getPositionType())
            .append("positionLevel", getPositionLevel())
            .append("skills", getSkills())
            .append("status", getStatus())
            .append("currentWorkload", getCurrentWorkload())
            .append("maxWorkload", getMaxWorkload())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}