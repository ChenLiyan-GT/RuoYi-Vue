package com.ruoyi.system.work.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.work.domain.WorkTimesheet;
import com.ruoyi.system.work.mapper.WorkTimesheetMapper;
import com.ruoyi.system.work.service.IWorkTimesheetService;

/**
 * 工时记录 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkTimesheetServiceImpl implements IWorkTimesheetService
{
    @Autowired
    private WorkTimesheetMapper timesheetMapper;

    /**
     * 查询工时记录列表
     * 
     * @param workTimesheet 工时记录信息
     * @return 工时记录信息集合
     */
    @Override
    public List<WorkTimesheet> selectTimesheetList(WorkTimesheet workTimesheet)
    {
        return timesheetMapper.selectTimesheetList(workTimesheet);
    }

    /**
     * 根据分配 ID 查询工时记录列表
     * 
     * @param assignId 分配 ID
     * @return 工时记录信息集合
     */
    @Override
    public List<WorkTimesheet> selectTimesheetsByAssignId(Long assignId)
    {
        return timesheetMapper.selectTimesheetsByAssignId(assignId);
    }

    /**
     * 根据员工 ID 查询工时记录列表
     * 
     * @param employeeId 员工 ID
     * @return 工时记录信息集合
     */
    @Override
    public List<WorkTimesheet> selectTimesheetsByEmployeeId(Long employeeId)
    {
        return timesheetMapper.selectTimesheetsByEmployeeId(employeeId);
    }

    /**
     * 通过工时记录 ID 查询工时记录信息
     * 
     * @param timesheetId 工时记录 ID
     * @return 工时记录对象信息
     */
    @Override
    public WorkTimesheet selectTimesheetById(Long timesheetId)
    {
        return timesheetMapper.selectTimesheetById(timesheetId);
    }

    /**
     * 根据日期范围查询工时记录
     * 
     * @param employeeId 员工 ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工时记录信息集合
     */
    @Override
    public List<WorkTimesheet> selectTimesheetsByDateRange(Long employeeId, Date startDate, Date endDate)
    {
        return timesheetMapper.selectTimesheetsByDateRange(employeeId, startDate, endDate);
    }

    /**
     * 批量删除工时记录信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    public int deleteTimesheetByIds(String ids)
    {
        Long[] timesheetIds = Convert.toLongArray(ids);
        return timesheetMapper.deleteTimesheetByIds(timesheetIds);
    }

    /**
     * 新增保存工时记录信息
     * 
     * @param workTimesheet 工时记录信息
     * @return 结果
     */
    @Override
    public int insertTimesheet(WorkTimesheet workTimesheet)
    {
        workTimesheet.setCreateTime(DateUtils.getNowDate());
        return timesheetMapper.insertTimesheet(workTimesheet);
    }

    /**
     * 修改保存工时记录信息
     * 
     * @param workTimesheet 工时记录信息
     * @return 结果
     */
    @Override
    public int updateTimesheet(WorkTimesheet workTimesheet)
    {
        workTimesheet.setUpdateTime(DateUtils.getNowDate());
        return timesheetMapper.updateTimesheet(workTimesheet);
    }

    /**
     * 提交工时记录
     * 
     * @param timesheetId 工时记录 ID
     * @param submitBy 提交人
     * @return 结果
     */
    @Override
    @Transactional
    public int submitTimesheet(Long timesheetId, String submitBy)
    {
        WorkTimesheet timesheet = timesheetMapper.selectTimesheetById(timesheetId);
        if (timesheet == null)
        {
            throw new ServiceException("工时记录不存在");
        }
        
        if (!"0".equals(timesheet.getStatus()))
        {
            throw new ServiceException("只能提交草稿状态的工时记录");
        }
        
        timesheet.setStatus("1"); // 已提交
        timesheet.setSubmitBy(submitBy);
        timesheet.setSubmitTime(DateUtils.getNowDate());
        
        return timesheetMapper.updateTimesheet(timesheet);
    }

    /**
     * 审核工时记录
     * 
     * @param timesheetId 工时记录 ID
     * @param status 审核状态
     * @param auditComment 审核意见
     * @param auditBy 审核人
     * @return 结果
     */
    @Override
    @Transactional
    public int auditTimesheet(Long timesheetId, String status, String auditComment, String auditBy)
    {
        WorkTimesheet timesheet = timesheetMapper.selectTimesheetById(timesheetId);
        if (timesheet == null)
        {
            throw new ServiceException("工时记录不存在");
        }
        
        if (!"1".equals(timesheet.getStatus()))
        {
            throw new ServiceException("只能审核已提交的工时记录");
        }
        
        timesheet.setStatus(status); // 2=通过，3=驳回
        timesheet.setAuditBy(auditBy);
        timesheet.setAuditTime(DateUtils.getNowDate());
        timesheet.setAuditComment(auditComment);
        
        return timesheetMapper.updateTimesheet(timesheet);
    }

    /**
     * 统计员工指定日期范围内的工时总和
     * 
     * @param employeeId 员工 ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工时总和
     */
    @Override
    public Double sumWorkloadByDateRange(Long employeeId, Date startDate, Date endDate)
    {
        return timesheetMapper.sumWorkloadByDateRange(employeeId, startDate, endDate);
    }

    /**
     * 统计分配记录的已报工时总和
     * 
     * @param assignId 分配 ID
     * @return 工时总和
     */
    @Override
    public Double sumWorkloadByAssignId(Long assignId)
    {
        return timesheetMapper.sumWorkloadByAssignId(assignId);
    }
}