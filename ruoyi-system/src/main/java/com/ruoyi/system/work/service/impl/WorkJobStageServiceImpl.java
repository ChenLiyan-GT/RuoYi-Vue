package com.ruoyi.system.work.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.work.domain.WorkJobStage;
import com.ruoyi.system.work.mapper.WorkJobStageMapper;
import com.ruoyi.system.work.service.IWorkJobStageService;

/**
 * 作业阶段 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkJobStageServiceImpl implements IWorkJobStageService
{
    @Autowired
    private WorkJobStageMapper jobStageMapper;

    /**
     * 查询作业阶段列表
     * 
     * @param workJobStage 作业阶段信息
     * @return 作业阶段信息集合
     */
    @Override
    public List<WorkJobStage> selectJobStageList(WorkJobStage workJobStage)
    {
        return jobStageMapper.selectJobStageList(workJobStage);
    }

    /**
     * 根据作业 ID 查询作业阶段列表
     * 
     * @param jobId 作业 ID
     * @return 作业阶段信息集合
     */
    @Override
    public List<WorkJobStage> selectJobStagesByJobId(Long jobId)
    {
        return jobStageMapper.selectJobStagesByJobId(jobId);
    }

    /**
     * 通过作业阶段 ID 查询作业阶段信息
     * 
     * @param jobStageId 作业阶段 ID
     * @return 作业阶段对象信息
     */
    @Override
    public WorkJobStage selectJobStageById(Long jobStageId)
    {
        return jobStageMapper.selectJobStageById(jobStageId);
    }

    /**
     * 查询待分配的作业阶段列表
     * 
     * @param status 状态
     * @return 作业阶段信息集合
     */
    @Override
    public List<WorkJobStage> selectStagesToAssign(String status)
    {
        return jobStageMapper.selectStagesToAssign(status);
    }

    /**
     * 批量删除作业阶段信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    public int deleteJobStageByIds(String ids)
    {
        Long[] jobStageIds = Convert.toLongArray(ids);
        return jobStageMapper.deleteJobStageByIds(jobStageIds);
    }

    /**
     * 新增保存作业阶段信息
     * 
     * @param workJobStage 作业阶段信息
     * @return 结果
     */
    @Override
    public int insertJobStage(WorkJobStage workJobStage)
    {
        return jobStageMapper.insertJobStage(workJobStage);
    }

    /**
     * 修改保存作业阶段信息
     * 
     * @param workJobStage 作业阶段信息
     * @return 结果
     */
    @Override
    public int updateJobStage(WorkJobStage workJobStage)
    {
        return jobStageMapper.updateJobStage(workJobStage);
    }

    /**
     * 更新作业阶段状态
     * 
     * @param jobStageId 作业阶段 ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateJobStageStatus(Long jobStageId, String status)
    {
        return jobStageMapper.updateJobStageStatus(jobStageId, status);
    }

    /**
     * 更新作业阶段实际工时
     * 
     * @param jobStageId 作业阶段 ID
     * @param actualWorkload 实际工时
     * @return 结果
     */
    @Override
    public int updateJobStageWorkload(Long jobStageId, Double actualWorkload)
    {
        return jobStageMapper.updateJobStageWorkload(jobStageId, actualWorkload);
    }

    /**
     * 统计作业阶段的已分配工时
     * 
     * @param jobStageId 作业阶段 ID
     * @return 已分配工时
     */
    @Override
    public Double sumAssignedWorkload(Long jobStageId)
    {
        return jobStageMapper.sumAssignedWorkload(jobStageId);
    }

    /**
     * 检查阶段是否可以开始
     * 
     * @param jobStageId 作业阶段 ID
     * @return true=可以开始，false=前置阶段未完成
     */
    @Override
    public boolean canStartStage(Long jobStageId)
    {
        WorkJobStage currentStage = jobStageMapper.selectJobStageById(jobStageId);
        if (currentStage == null)
        {
            return false;
        }
        
        // 查询同一作业中顺序在当前阶段之前的所有阶段
        List<WorkJobStage> previousStages = jobStageMapper.selectJobStagesByJobId(currentStage.getJobId());
        for (WorkJobStage stage : previousStages)
        {
            if (stage.getStageOrder() < currentStage.getStageOrder())
            {
                // 前置阶段必须已完成（状态为 2）
                if (!"2".equals(stage.getStatus()))
                {
                    return false;
                }
            }
        }
        
        return true;
    }
}