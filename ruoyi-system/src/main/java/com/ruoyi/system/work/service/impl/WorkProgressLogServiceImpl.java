package com.ruoyi.system.work.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.work.domain.WorkProgressLog;
import com.ruoyi.system.work.mapper.WorkProgressLogMapper;
import com.ruoyi.system.work.service.IWorkJobStageService;
import com.ruoyi.system.work.service.IWorkProgressLogService;

/**
 * 进度更新记录 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkProgressLogServiceImpl implements IWorkProgressLogService
{
    @Autowired
    private WorkProgressLogMapper progressLogMapper;

    @Autowired
    private IWorkJobStageService jobStageService;

    /**
     * 查询进度更新记录列表
     * 
     * @param workProgressLog 进度更新记录信息
     * @return 进度更新记录信息集合
     */
    @Override
    public List<WorkProgressLog> selectProgressLogList(WorkProgressLog workProgressLog)
    {
        return progressLogMapper.selectProgressLogList(workProgressLog);
    }

    /**
     * 根据作业阶段 ID 查询进度更新记录列表
     * 
     * @param jobStageId 作业阶段 ID
     * @return 进度更新记录信息集合
     */
    @Override
    public List<WorkProgressLog> selectProgressLogsByJobStageId(Long jobStageId)
    {
        return progressLogMapper.selectProgressLogsByJobStageId(jobStageId);
    }

    /**
     * 根据员工 ID 查询进度更新记录列表
     * 
     * @param employeeId 员工 ID
     * @return 进度更新记录信息集合
     */
    @Override
    public List<WorkProgressLog> selectProgressLogsByEmployeeId(Long employeeId)
    {
        return progressLogMapper.selectProgressLogsByEmployeeId(employeeId);
    }

    /**
     * 通过进度更新记录 ID 查询进度更新记录信息
     * 
     * @param logId 进度更新记录 ID
     * @return 进度更新记录对象信息
     */
    @Override
    public WorkProgressLog selectProgressLogById(Long logId)
    {
        return progressLogMapper.selectProgressLogById(logId);
    }

    /**
     * 批量删除进度更新记录信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    public int deleteProgressLogByIds(String ids)
    {
        Long[] progressLogIds = Convert.toLongArray(ids);
        return progressLogMapper.deleteProgressLogByIds(progressLogIds);
    }

    /**
     * 新增保存进度更新记录信息
     * 
     * @param workProgressLog 进度更新记录信息
     * @return 结果
     */
    @Override
    public int insertProgressLog(WorkProgressLog workProgressLog)
    {
        workProgressLog.setCreateTime(DateUtils.getNowDate());
        return progressLogMapper.insertProgressLog(workProgressLog);
    }

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
    @Override
    @Transactional
    public int recordProgress(Long jobStageId, Long employeeId, Double progress, 
                               Double workload, String content, String remark, String createBy)
    {
        // 获取上一阶段的进度
        WorkProgressLog latestLog = progressLogMapper.selectLatestProgress(jobStageId);
        BigDecimal prevProgress = latestLog != null ? latestLog.getProgress() : BigDecimal.ZERO;
        
        // 创建进度记录
        WorkProgressLog progressLog = new WorkProgressLog();
        progressLog.setJobStageId(jobStageId);
        progressLog.setEmployeeId(employeeId);
        progressLog.setProgress(BigDecimal.valueOf(progress));
        progressLog.setPrevProgress(prevProgress);
        progressLog.setRemark(remark);
        progressLog.setCreateBy(createBy);
        
        int result = progressLogMapper.insertProgressLog(progressLog);
        
        // 更新作业阶段的实际工时和进度
        if (result > 0)
        {
            jobStageService.updateJobStageWorkload(jobStageId, workload);
        }
        
        return result;
    }

    /**
     * 查询作业阶段的最新进度
     * 
     * @param jobStageId 作业阶段 ID
     * @return 最新进度记录
     */
    @Override
    public WorkProgressLog selectLatestProgress(Long jobStageId)
    {
        return progressLogMapper.selectLatestProgress(jobStageId);
    }
}