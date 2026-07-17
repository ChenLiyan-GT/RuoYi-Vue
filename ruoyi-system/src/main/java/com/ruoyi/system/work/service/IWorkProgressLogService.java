package com.ruoyi.system.work.service;

import java.util.List;
import com.ruoyi.system.work.domain.WorkProgressLog;

/**
 * 进度更新记录 服务层
 * 
 * @author ruoyi
 */
public interface IWorkProgressLogService
{
    /**
     * 查询进度更新记录列表
     * 
     * @param workProgressLog 进度更新记录信息
     * @return 进度更新记录信息集合
     */
    public List<WorkProgressLog> selectProgressLogList(WorkProgressLog workProgressLog);

    /**
     * 根据作业阶段 ID 查询进度更新记录列表
     * 
     * @param jobStageId 作业阶段 ID
     * @return 进度更新记录信息集合
     */
    public List<WorkProgressLog> selectProgressLogsByJobStageId(Long jobStageId);

    /**
     * 根据员工 ID 查询进度更新记录列表
     * 
     * @param employeeId 员工 ID
     * @return 进度更新记录信息集合
     */
    public List<WorkProgressLog> selectProgressLogsByEmployeeId(Long employeeId);

    /**
     * 通过进度更新记录 ID 查询进度更新记录信息
     * 
     * @param logId 进度更新记录 ID
     * @return 进度更新记录对象信息
     */
    public WorkProgressLog selectProgressLogById(Long logId);

    /**
     * 批量删除进度更新记录信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteProgressLogByIds(String ids);

    /**
     * 新增保存进度更新记录信息
     * 
     * @param workProgressLog 进度更新记录信息
     * @return 结果
     */
    public int insertProgressLog(WorkProgressLog workProgressLog);

    /**
     * 记录进度更新
     * 
     * @param jobStageId 作业阶段 ID
     * @param employeeId 员工 ID
     * @param progress 当前进度
     * @param workload 本次工时
     * @param content 工作内容
     * @param remark 备注
     * @param createBy 创建人
     * @return 结果
     */
    public int recordProgress(Long jobStageId, Long employeeId, Double progress, 
                               Double workload, String content, String remark, String createBy);

    /**
     * 查询作业阶段的最新进度
     * 
     * @param jobStageId 作业阶段 ID
     * @return 最新进度记录
     */
    public WorkProgressLog selectLatestProgress(Long jobStageId);
}