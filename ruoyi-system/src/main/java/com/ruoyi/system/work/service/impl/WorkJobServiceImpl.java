package com.ruoyi.system.work.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.work.domain.WorkJob;
import com.ruoyi.system.work.domain.WorkJobStage;
import com.ruoyi.system.work.domain.WorkStageTemplate;
import com.ruoyi.system.work.mapper.WorkJobMapper;
import com.ruoyi.system.work.mapper.WorkJobStageMapper;
import com.ruoyi.system.work.service.IWorkJobService;
import com.ruoyi.system.work.service.IWorkStageTemplateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 作业 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkJobServiceImpl implements IWorkJobService
{
    @Autowired
    private WorkJobMapper jobMapper;

    @Autowired
    private WorkJobStageMapper jobStageMapper;

    @Autowired
    private IWorkStageTemplateService stageTemplateService;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 查询作业列表
     * 
     * @param workJob 作业信息
     * @return 作业信息集合
     */
    @Override
    public List<WorkJob> selectJobList(WorkJob workJob)
    {
        return jobMapper.selectJobList(workJob);
    }

    /**
     * 查询所有作业
     * 
     * @return 作业列表
     */
    @Override
    public List<WorkJob> selectJobAll()
    {
        return jobMapper.selectJobAll();
    }

    /**
     * 通过作业 ID 查询作业信息
     * 
     * @param jobId 作业 ID
     * @return 作业对象信息
     */
    @Override
    public WorkJob selectJobById(Long jobId)
    {
        return jobMapper.selectJobById(jobId);
    }

    /**
     * 根据作业编号查询作业信息
     * 
     * @param jobNo 作业编号
     * @return 作业对象信息
     */
    @Override
    public WorkJob checkJobNoUnique(String jobNo)
    {
        return jobMapper.checkJobNoUnique(jobNo);
    }

    /**
     * 根据部门 ID 查询作业列表
     * 
     * @param deptId 部门 ID
     * @return 作业数据集合
     */
    @Override
    public List<WorkJob> selectJobsByDeptId(Long deptId)
    {
        return jobMapper.selectJobsByDeptId(deptId);
    }

    /**
     * 根据状态查询作业列表
     * 
     * @param status 状态
     * @return 作业数据集合
     */
    @Override
    public List<WorkJob> selectJobsByStatus(String status)
    {
        return jobMapper.selectJobsByStatus(status);
    }

    /**
     * 批量删除作业信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteJobByIds(String ids)
    {
        Long[] jobIds = Convert.toLongArray(ids);
        // 先删除关联的阶段
        for (Long jobId : jobIds)
        {
            jobStageMapper.deleteJobStageByJobId(jobId);
        }
        return jobMapper.deleteJobByIds(jobIds);
    }

    /**
     * 新增保存作业信息
     * 
     * @param workJob 作业信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertJob(WorkJob workJob)
    {
        // 校验作业编号唯一性
        if (!checkJobNoUnique(workJob))
        {
            throw new ServiceException("新增作业'" + workJob.getJobName() + "'失败，作业编号已存在");
        }
        
        // 生成作业编号
        if (StringUtils.isEmpty(workJob.getJobNo()))
        {
            workJob.setJobNo(generateJobNo(workJob.getDeptId()));
        }
        
        // 插入作业主表
        int result = jobMapper.insertJob(workJob);
        
        // 根据模板生成作业阶段
        if (workJob.getTemplateId() != null)
        {
            createJobStagesFromTemplate(workJob.getJobId(), workJob.getTemplateId(), workJob.getTotalWorkload());
        }
        
        return result;
    }

    /**
     * 修改保存作业信息
     * 
     * @param workJob 作业信息
     * @return 结果
     */
    @Override
    public int updateJob(WorkJob workJob)
    {
        // 校验作业编号唯一性
        if (!checkJobNoUnique(workJob))
        {
            throw new ServiceException("修改作业'" + workJob.getJobName() + "'失败，作业编号已存在");
        }
        return jobMapper.updateJob(workJob);
    }

    /**
     * 更新作业进度
     * 
     * @param jobId 作业 ID
     * @param progress 进度
     * @return 结果
     */
    @Override
    public int updateJobProgress(Long jobId, BigDecimal progress)
    {
        return jobMapper.updateJobProgress(jobId, progress);
    }

    /**
     * 更新作业状态
     * 
     * @param jobId 作业 ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateJobStatus(Long jobId, String status)
    {
        return jobMapper.updateJobStatus(jobId, status);
    }

    /**
     * 校验作业编号是否唯一
     * 
     * @param workJob 作业信息
     * @return 结果
     */
    @Override
    public boolean checkJobNoUnique(WorkJob workJob)
    {
        Long jobId = StringUtils.isNull(workJob.getJobId()) ? -1L : workJob.getJobId();
        WorkJob info = jobMapper.checkJobNoUnique(workJob.getJobNo());
        if (StringUtils.isNotNull(info) && info.getJobId().longValue() != jobId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 生成作业编号
     * 
     * @param deptId 部门 ID
     * @return 作业编号
     */
    @Override
    public String generateJobNo(Long deptId)
    {
        String dateStr = dateFormat.format(new Date());
        String deptStr = String.format("%04d", deptId != null ? deptId : 0);
        
        // 获取当天该部门的作业数量
        int count = jobMapper.countJobsByDeptAndStatus(deptId, null);
        String seqStr = String.format("%04d", count + 1);
        
        return "JOB" + dateStr + deptStr + seqStr;
    }

    /**
     * 统计部门作业数量
     * 
     * @param deptId 部门 ID
     * @param status 状态
     * @return 数量
     */
    @Override
    public int countJobsByDeptAndStatus(Long deptId, String status)
    {
        return jobMapper.countJobsByDeptAndStatus(deptId, status);
    }

    /**
     * 根据模板创建作业阶段
     * 
     * @param jobId 作业 ID
     * @param templateId 模板 ID
     * @param totalWorkload 总工时
     */
    private void createJobStagesFromTemplate(Long jobId, Long templateId, BigDecimal totalWorkload)
    {
        WorkStageTemplate template = stageTemplateService.selectStageTemplateById(templateId);
        if (template == null)
        {
            throw new ServiceException("模板不存在");
        }
        
        try
        {
            JsonNode stagesArray = objectMapper.readTree(template.getStagesConfig());
            int order = 0;
            
            for (JsonNode stageNode : stagesArray)
            {
                WorkJobStage jobStage = new WorkJobStage();
                jobStage.setJobId(jobId);
                jobStage.setStageCode(stageNode.get("stage_code").asText());
                jobStage.setStageName(stageNode.get("stage_name").asText());
                jobStage.setStageOrder(++order);
                
                // 计算阶段工时
                BigDecimal ratio = stageNode.has("workload_ratio") ? 
                    new BigDecimal(stageNode.get("workload_ratio").asText()) : BigDecimal.ZERO;
                jobStage.setPlannedWorkload(totalWorkload.multiply(ratio));
                
                // 获取最低级别要求
                jobStage.setMinLevel(stageNode.has("min_level") ? 
                    stageNode.get("min_level").asInt() : 1);
                
                // 获取技能要求
                if (stageNode.has("required_skills"))
                {
                    jobStage.setRequiredSkills(objectMapper.writeValueAsString(
                        stageNode.get("required_skills")));
                }
                
                jobStageMapper.insertJobStage(jobStage);
            }
            
            // 更新模板使用次数
            stageTemplateService.selectStageTemplateById(templateId);
        }
        catch (Exception e)
        {
            throw new ServiceException("创建作业阶段失败：" + e.getMessage());
        }
    }
}