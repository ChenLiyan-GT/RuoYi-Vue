package com.ruoyi.system.work.mapper;

import java.util.List;
import com.ruoyi.system.work.domain.WorkProgressLog;

/**
 * 进度更新记录 数据层
 * 
 * @author ruoyi
 */
public interface WorkProgressLogMapper
{
    /**
     * 查询进度更新记录列表
     * 
     * @param workProgressLog 进度更新记录信息
     * @return 进度更新记录数据集合
     */
    public List<WorkProgressLog> selectProgressLogList(WorkProgressLog workProgressLog);

    /**
     * 根据作业阶段 ID 查询进度更新记录列表
     * 
     * @param jobStageId 作业阶段 ID
     * @return 进度更新记录数据集合
     */
    public List<WorkProgressLog> selectProgressLogsByJobStageId(Long jobStageId);

    /**
     * 根据员工 ID 查询进度更新记录列表
     * 
     * @param employeeId 员工 ID
     * @return 进度更新记录数据集合
     */
    public List<WorkProgressLog> selectProgressLogsByEmployeeId(Long employeeId);

    /**
     * 查询进度更新记录信息
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
    public int deleteProgressLogByIds(Long[] ids);

    /**
     * 修改进度更新记录信息
     * 
     * @param workProgressLog 进度更新记录信息
     * @return 结果
     */
    public int updateProgressLog(WorkProgressLog workProgressLog);

    /**
     * 新增进度更新记录信息
     * 
     * @param workProgressLog 进度更新记录信息
     * @return 结果
     */
    public int insertProgressLog(WorkProgressLog workProgressLog);

    /**
     * 查询作业阶段的最新进度
     * 
     * @param jobStageId 作业阶段 ID
     * @return 最新进度记录
     */
    public WorkProgressLog selectLatestProgress(Long jobStageId);
}