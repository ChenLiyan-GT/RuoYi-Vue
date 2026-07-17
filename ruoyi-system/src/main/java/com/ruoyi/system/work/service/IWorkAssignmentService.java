package com.ruoyi.system.work.service;

import java.util.List;
import com.ruoyi.system.work.domain.WorkAssignment;

/**
 * 作业分配 服务层
 * 
 * @author ruoyi
 */
public interface IWorkAssignmentService
{
    /**
     * 查询作业分配列表
     * 
     * @param workAssignment 作业分配信息
     * @return 作业分配信息集合
     */
    public List<WorkAssignment> selectAssignmentList(WorkAssignment workAssignment);

    /**
     * 根据作业阶段 ID 查询作业分配列表
     * 
     * @param jobStageId 作业阶段 ID
     * @return 作业分配信息集合
     */
    public List<WorkAssignment> selectAssignmentsByJobStageId(Long jobStageId);

    /**
     * 根据员工 ID 查询作业分配列表
     * 
     * @param employeeId 员工 ID
     * @return 作业分配信息集合
     */
    public List<WorkAssignment> selectAssignmentsByEmployeeId(Long employeeId);

    /**
     * 通过作业分配 ID 查询作业分配信息
     * 
     * @param assignId 作业分配 ID
     * @return 作业分配对象信息
     */
    public WorkAssignment selectAssignmentById(Long assignId);

    /**
     * 查询待开始的作业分配
     * 
     * @param employeeId 员工 ID
     * @return 作业分配信息集合
     */
    public List<WorkAssignment> selectPendingAssignments(Long employeeId);

    /**
     * 批量删除作业分配信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteAssignmentByIds(String ids);

    /**
     * 新增保存作业分配信息
     * 
     * @param workAssignment 作业分配信息
     * @return 结果
     */
    public int insertAssignment(WorkAssignment workAssignment);

    /**
     * 修改保存作业分配信息
     * 
     * @param workAssignment 作业分配信息
     * @return 结果
     */
    public int updateAssignment(WorkAssignment workAssignment);

    /**
     * 更新作业分配状态
     * 
     * @param assignId 作业分配 ID
     * @param status 状态
     * @return 结果
     */
    public int updateAssignmentStatus(Long assignId, String status);

    /**
     * 统计员工当前分配的工时总和
     * 
     * @param employeeId 员工 ID
     * @return 工时总和
     */
    public Double sumEmployeeWorkload(Long employeeId);

    /**
     * 统计员工已完成的工时总和
     * 
     * @param employeeId 员工 ID
     * @return 工时总和
     */
    public Double sumEmployeeCompletedWorkload(Long employeeId);

    /**
     * 智能分配作业阶段给员工
     * 
     * @param jobStageId 作业阶段 ID
     * @param employeeIds 员工 ID 列表
     * @param workloadPerEmployee 每人分配工时
     * @return 分配的记录 ID 列表
     */
    public List<Long> smartAssign(Long jobStageId, List<Long> employeeIds, Double workloadPerEmployee);

    /**
     * 取消分配
     * 
     * @param assignId 作业分配 ID
     * @return 结果
     */
    public int cancelAssignment(Long assignId);
}