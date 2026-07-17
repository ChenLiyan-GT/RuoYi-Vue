package com.ruoyi.system.work.service;

import java.util.List;
import com.ruoyi.system.work.domain.WorkJobStage;

/**
 * 作业阶段 服务层
 * 
 * @author ruoyi
 */
public interface IWorkJobStageService
{
    /**
     * 查询作业阶段列表
     * 
     * @param workJobStage 作业阶段信息
     * @return 作业阶段信息集合
     */
    public List<WorkJobStage> selectJobStageList(WorkJobStage workJobStage);

    /**
     * 根据作业 ID 查询作业阶段列表
     * 
     * @param jobId 作业 ID
     * @return 作业阶段信息集合
     */
    public List<WorkJobStage> selectJobStagesByJobId(Long jobId);

    /**
     * 通过作业阶段 ID 查询作业阶段信息
     * 
     * @param jobStageId 作业阶段 ID
     * @return 作业阶段对象信息
     */
    public WorkJobStage selectJobStageById(Long jobStageId);

    /**
     * 查询待分配的作业阶段列表
     * 
     * @param status 状态
     * @return 作业阶段信息集合
     */
    public List<WorkJobStage> selectStagesToAssign(String status);

    /**
     * 批量删除作业阶段信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteJobStageByIds(String ids);

    /**
     * 新增保存作业阶段信息
     * 
     * @param workJobStage 作业阶段信息
     * @return 结果
     */
    public int insertJobStage(WorkJobStage workJobStage);

    /**
     * 修改保存作业阶段信息
     * 
     * @param workJobStage 作业阶段信息
     * @return 结果
     */
    public int updateJobStage(WorkJobStage workJobStage);

    /**
     * 更新作业阶段状态
     * 
     * @param jobStageId 作业阶段 ID
     * @param status 状态
     * @return 结果
     */
    public int updateJobStageStatus(Long jobStageId, String status);

    /**
     * 更新作业阶段实际工时
     * 
     * @param jobStageId 作业阶段 ID
     * @param actualWorkload 实际工时
     * @return 结果
     */
    public int updateJobStageWorkload(Long jobStageId, Double actualWorkload);

    /**
     * 统计作业阶段的已分配工时
     * 
     * @param jobStageId 作业阶段 ID
     * @return 已分配工时
     */
    public Double sumAssignedWorkload(Long jobStageId);

    /**
     * 检查阶段是否可以开始
     * 
     * @param jobStageId 作业阶段 ID
     * @return true=可以开始，false=前置阶段未完成
     */
    public boolean canStartStage(Long jobStageId);
}