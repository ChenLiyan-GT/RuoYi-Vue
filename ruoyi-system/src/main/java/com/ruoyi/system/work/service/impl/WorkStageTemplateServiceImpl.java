package com.ruoyi.system.work.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        // 先将所有模板设为非默认
        WorkStageTemplate allTemplate = new WorkStageTemplate();
        allTemplate.setIsDefault("0");
        stageTemplateMapper.updateStageTemplate(allTemplate);
        
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
}