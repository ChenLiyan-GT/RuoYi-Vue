package com.ruoyi.system.work.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.work.domain.WorkStageTemplate;
import com.ruoyi.system.work.mapper.WorkStageTemplateMapper;
import com.ruoyi.system.work.service.IWorkStageTemplateService;

/**
 * 作业阶段模板 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkStageTemplateServiceImpl implements IWorkStageTemplateService
{
    @Autowired
    private WorkStageTemplateMapper stageTemplateMapper;

    /**
     * 查询作业阶段模板列表
     * 
     * @param workStageTemplate 作业阶段模板信息
     * @return 作业阶段模板信息集合
     */
    @Override
    public List<WorkStageTemplate> selectStageTemplateList(WorkStageTemplate workStageTemplate)
    {
        return stageTemplateMapper.selectStageTemplateList(workStageTemplate);
    }

    /**
     * 查询所有作业阶段模板
     * 
     * @return 作业阶段模板列表
     */
    @Override
    public List<WorkStageTemplate> selectStageTemplateAll()
    {
        return stageTemplateMapper.selectStageTemplateAll();
    }

    /**
     * 通过作业阶段模板 ID 查询作业阶段模板信息
     * 
     * @param templateId 作业阶段模板 ID
     * @return 作业阶段模板对象信息
     */
    @Override
    public WorkStageTemplate selectStageTemplateById(Long templateId)
    {
        return stageTemplateMapper.selectStageTemplateById(templateId);
    }

    /**
     * 根据部门 ID 查询模板列表
     * 
     * @param deptId 部门 ID
     * @return 作业阶段模板数据集合
     */
    @Override
    public List<WorkStageTemplate> selectTemplatesByDeptId(Long deptId)
    {
        return stageTemplateMapper.selectTemplatesByDeptId(deptId);
    }

    /**
     * 查询默认模板
     * 
     * @return 默认作业阶段模板
     */
    @Override
    public WorkStageTemplate selectDefaultTemplate()
    {
        return stageTemplateMapper.selectDefaultTemplate();
    }

    /**
     * 根据模板名称查询作业阶段模板信息
     * 
     * @param templateName 模板名称
     * @return 作业阶段模板对象信息
     */
    @Override
    public WorkStageTemplate checkTemplateNameUnique(String templateName)
    {
        return stageTemplateMapper.checkTemplateNameUnique(templateName);
    }

    /**
     * 批量删除作业阶段模板信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    public int deleteStageTemplateByIds(String ids)
    {
        Long[] templateIds = Convert.toLongArray(ids);
        return stageTemplateMapper.deleteStageTemplateByIds(templateIds);
    }

    /**
     * 新增保存作业阶段模板信息
     * 
     * @param workStageTemplate 作业阶段模板信息
     * @return 结果
     */
    @Override
    public int insertStageTemplate(WorkStageTemplate workStageTemplate)
    {
        // 校验模板名称唯一性
        if (!checkTemplateNameUnique(workStageTemplate))
        {
            throw new ServiceException("新增模板'" + workStageTemplate.getTemplateName() + "'失败，模板名称已存在");
        }
        // 验证阶段配置 JSON
        validateStagesConfig(workStageTemplate.getStagesConfig());
        return stageTemplateMapper.insertStageTemplate(workStageTemplate);
    }

    /**
     * 修改保存作业阶段模板信息
     * 
     * @param workStageTemplate 作业阶段模板信息
     * @return 结果
     */
    @Override
    public int updateStageTemplate(WorkStageTemplate workStageTemplate)
    {
        // 校验模板名称唯一性
        if (!checkTemplateNameUnique(workStageTemplate))
        {
            throw new ServiceException("修改模板'" + workStageTemplate.getTemplateName() + "'失败，模板名称已存在");
        }
        // 验证阶段配置 JSON
        validateStagesConfig(workStageTemplate.getStagesConfig());
        return stageTemplateMapper.updateStageTemplate(workStageTemplate);
    }

    /**
     * 复制模板
     * 
     * @param templateId 模板 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int copyTemplate(Long templateId)
    {
        WorkStageTemplate template = stageTemplateMapper.selectStageTemplateById(templateId);
        if (template == null)
        {
            throw new ServiceException("模板不存在");
        }
        
        // 创建新模板
        WorkStageTemplate newTemplate = new WorkStageTemplate();
        newTemplate.setTemplateName(template.getTemplateName() + "_副本");
        newTemplate.setDeptId(template.getDeptId());
        newTemplate.setStagesConfig(template.getStagesConfig());
        newTemplate.setVersion(1);
        newTemplate.setUsageCount(0);
        newTemplate.setStatus("0");
        newTemplate.setIsDefault("0");
        
        return stageTemplateMapper.insertStageTemplate(newTemplate);
    }

    /**
     * 设为默认模板
     * 
     * @param templateId 模板 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int setDefaultTemplate(Long templateId)
    {
        if (templateId == null)
        {
            throw new ServiceException("模板 ID 不能为空");
        }
        
        // 先取消当前默认模板（如果有）
        WorkStageTemplate currentDefault = selectDefaultTemplate();
        if (currentDefault != null && !currentDefault.getTemplateId().equals(templateId))
        {
            WorkStageTemplate updateTemplate = new WorkStageTemplate();
            updateTemplate.setTemplateId(currentDefault.getTemplateId());
            updateTemplate.setIsDefault("0");
            stageTemplateMapper.updateStageTemplate(updateTemplate);
        }
        
        // 再将指定模板设为默认
        WorkStageTemplate template = new WorkStageTemplate();
        template.setTemplateId(templateId);
        template.setIsDefault("1");
        return stageTemplateMapper.updateStageTemplate(template);
    }

    /**
     * 校验模板名称是否唯一
     * 
     * @param workStageTemplate 作业阶段模板信息
     * @return 结果
     */
    @Override
    public boolean checkTemplateNameUnique(WorkStageTemplate workStageTemplate)
    {
        Long templateId = StringUtils.isNull(workStageTemplate.getTemplateId()) ? -1L : workStageTemplate.getTemplateId();
        WorkStageTemplate info = stageTemplateMapper.checkTemplateNameUnique(workStageTemplate.getTemplateName());
        if (StringUtils.isNotNull(info) && info.getTemplateId().longValue() != templateId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 验证阶段配置 JSON
     * 
     * @param stagesConfig 阶段配置 JSON 字符串
     */
    private void validateStagesConfig(String stagesConfig)
    {
        if (StringUtils.isEmpty(stagesConfig))
        {
            throw new ServiceException("阶段配置不能为空");
        }
        try
        {
            JSONArray stages = JSON.parseArray(stagesConfig);
            if (stages == null || stages.isEmpty())
            {
                throw new ServiceException("阶段配置不能为空");
            }
            
            double totalRatio = 0;
            Set<String> stageCodes = new HashSet<>();
            
            for (int i = 0; i < stages.size(); i++)
            {
                JSONObject stage = stages.getJSONObject(i);
                
                // 验证必填字段
                if (!stage.containsKey("stage_code") || !stage.containsKey("stage_name"))
                {
                    throw new ServiceException("阶段配置缺少必填字段：stage_code, stage_name");
                }
                
                // 检查 stage_code 重复
                String stageCode = stage.getString("stage_code");
                if (stageCodes.contains(stageCode))
                {
                    throw new ServiceException("阶段编码重复：" + stageCode);
                }
                stageCodes.add(stageCode);
                
                // 累加工时占比
                Double ratio = stage.getDouble("workload_ratio");
                if (ratio == null)
                {
                    ratio = 0.0;
                }
                totalRatio += ratio;
            }
            
            // 验证工时占比总和（允许 0.01 的误差）
            if (Math.abs(totalRatio - 1.0) > 0.01)
            {
                throw new ServiceException("工时占比总和必须为 100%，当前为" + String.format("%.2f", totalRatio * 100) + "%");
            }
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ServiceException("阶段配置格式错误：" + e.getMessage());
        }
    }
}
