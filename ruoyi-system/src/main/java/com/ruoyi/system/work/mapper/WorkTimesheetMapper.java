package com.ruoyi.system.work.mapper;

import java.util.Date;
import java.util.List;
import com.ruoyi.system.work.domain.WorkTimesheet;

/**
 * 工时记录 数据层
 * 
 * @author ruoyi
 */
public interface WorkTimesheetMapper
{
    /**
     * 查询工时记录列表
     * 
     * @param workTimesheet 工时记录信息
     * @return 工时记录数据集合
     */
    public List<WorkTimesheet> selectTimesheetList(WorkTimesheet workTimesheet);

    /**
     * 根据分配 ID 查询工时记录列表
     * 
     * @param assignId 分配 ID
     * @return 工时记录数据集合
     */
    public List<WorkTimesheet> selectTimesheetsByAssignId(Long assignId);

    /**
     * 根据员工 ID 查询工时记录列表
     * 
     * @param employeeId 员工 ID
     * @return 工时记录数据集合
     */
    public List<WorkTimesheet> selectTimesheetsByEmployeeId(Long employeeId);

    /**
     * 查询工时记录信息
     * 
     * @param timesheetId 工时记录 ID
     * @return 工时记录对象信息
     */
    public WorkTimesheet selectTimesheetById(Long timesheetId);

    /**
     * 根据日期范围查询工时记录
     * 
     * @param employeeId 员工 ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工时记录数据集合
     */
    public List<WorkTimesheet> selectTimesheetsByDateRange(Long employeeId, Date startDate, Date endDate);

    /**
     * 批量删除工时记录信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteTimesheetByIds(Long[] ids);

    /**
     * 修改工时记录信息
     * 
     * @param workTimesheet 工时记录信息
     * @return 结果
     */
    public int updateTimesheet(WorkTimesheet workTimesheet);

    /**
     * 新增工时记录信息
     * 
     * @param workTimesheet 工时记录信息
     * @return 结果
     */
    public int insertTimesheet(WorkTimesheet workTimesheet);

    /**
     * 统计员工指定日期范围内的工时总和
     * 
     * @param employeeId 员工 ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工时总和
     */
    public Double sumWorkloadByDateRange(Long employeeId, Date startDate, Date endDate);

    /**
     * 统计分配记录的已报工时总和
     * 
     * @param assignId 分配 ID
     * @return 工时总和
     */
    public Double sumWorkloadByAssignId(Long assignId);
}