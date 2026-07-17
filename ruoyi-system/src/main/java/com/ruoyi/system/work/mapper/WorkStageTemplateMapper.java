package com.ruoyi.system.work.mapper;

import java.util.List;
import com.ruoyi.system.work.domain.WorkStageTemplate;

/**
 * 作业阶段模板 数据层
 * 
 * @author ruoyi
 */
public interface WorkStageTemplateMapper
{
    /**
     * 查询作业阶段模板列表
     * 
     * @param workStageTemplate 作业阶段模板信息
     * @return 作业阶段模板数据集合
     */
    public List<WorkStageTemplate> selectStageTemplateList(WorkStageTemplate workStageTemplate);

    /**
     * 查询所有作业阶段模板
     * 
     * @return 作业阶段模板列表
     */
    public List<WorkStageTemplate> selectStageTemplateAll();

    /**
     * 通过作业阶段模板 ID 查询作业阶段模板信息
     * 
     * @param templateId 作业阶段模板 ID
     * @return 作业阶段模板对象信息
     */
    public WorkStageTemplate selectStageTemplateById(Long templateId);

    /**
     * 根据部门 ID 查询模板列表
     * 
     * @param deptId 部门 ID
     * @return 作业阶段模板数据集合
     */
    public List<WorkStageTemplate> selectTemplatesByDeptId(Long deptId);

    /**
     * 查询默认模板
     * 
     * @return 默认作业阶段模板
     */
    public WorkStageTemplate selectDefaultTemplate();

    /**
     * 根据模板名称查询作业阶段模板信息
     * 
     * @param templateName 模板名称
     * @return 作业阶段模板对象信息
     */
    public WorkStageTemplate checkTemplateNameUnique(String templateName);

    /**
     * 批量删除作业阶段模板信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteStageTemplateByIds(Long[] ids);

    /**
     * 修改作业阶段模板信息
     * 
     * @param workStageTemplate 作业阶段模板信息
     * @return 结果
     */
    public int updateStageTemplate(WorkStageTemplate workStageTemplate);

    /**
     * 新增作业阶段模板信息
     * 
     * @param workStageTemplate 作业阶段模板信息
     * @return 结果
     */
    public int insertStageTemplate(WorkStageTemplate workStageTemplate);

    /**
     * 更新模板使用次数
     * 
     * @param templateId 模板 ID
     * @return 结果
     */
    public int incrementUsageCount(Long templateId);
}