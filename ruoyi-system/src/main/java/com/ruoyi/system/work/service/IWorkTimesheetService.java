package com.ruoyi.system.work.service;

import java.util.Date;
import java.util.List;
import com.ruoyi.system.work.domain.WorkTimesheet;

/**
 * 工时记录 服务层
 * 
 * @author ruoyi
 */
public interface IWorkTimesheetService
{
    /**
     * 查询工时记录列表
     * 
     * @param workTimesheet 工时记录信息
     * @return 工时记录信息集合
     */
    public List<WorkTimesheet> selectTimesheetList(WorkTimesheet workTimesheet);

    /**
     * 根据分配 ID 查询工时记录列表
     * 
     * @param assignId 分配 ID
     * @return 工时记录信息集合
     */
    public List<WorkTimesheet> selectTimesheetsByAssignId(Long assignId);

    /**
     * 根据员工 ID 查询工时记录列表
     * 
     * @param employeeId 员工 ID
     * @return 工时记录信息集合
     */
    public List<WorkTimesheet> selectTimesheetsByEmployeeId(Long employeeId);

    /**
     * 通过工时记录 ID 查询工时记录信息
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
     * @return 工时记录信息集合
     */
    public List<WorkTimesheet> selectTimesheetsByDateRange(Long employeeId, Date startDate, Date endDate);

    /**
     * 批量删除工时记录信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteTimesheetByIds(String ids);

    /**
     * 新增保存工时记录信息
     * 
     * @param workTimesheet 工时记录信息
     * @return 结果
     */
    public int insertTimesheet(WorkTimesheet workTimesheet);

    /**
     * 修改保存工时记录信息
     * 
     * @param workTimesheet 工时记录信息
     * @return 结果
     */
    public int updateTimesheet(WorkTimesheet workTimesheet);

    /**
     * 提交工时记录
     * 
     * @param timesheetId 工时记录 ID
     * @param submitBy 提交人
     * @return 结果
     */
    public int submitTimesheet(Long timesheetId, String submitBy);

    /**
     * 审核工时记录
     * 
     * @param timesheetId 工时记录 ID
     * @param status 审核状态
     * @param auditComment 审核意见
     * @param auditBy 审核人
     * @return 结果
     */
    public int auditTimesheet(Long timesheetId, String status, String auditComment, String auditBy);

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