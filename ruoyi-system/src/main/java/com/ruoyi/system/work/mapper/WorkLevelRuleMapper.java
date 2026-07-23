package com.ruoyi.system.work.mapper;

import java.util.List;
import com.ruoyi.system.work.domain.WorkLevelRule;

/**
 * 级别规则 数据层
 * 
 * @author ruoyi
 */
public interface WorkLevelRuleMapper
{
    /**
     * 查询级别规则列表
     * 
     * @param workLevelRule 级别规则信息
     * @return 级别规则数据集合
     */
    public List<WorkLevelRule> selectLevelRuleList(WorkLevelRule workLevelRule);

    /**
     * 查询所有级别规则
     * 
     * @return 级别规则列表
     */
    public List<WorkLevelRule> selectLevelRuleAll();

    /**
     * 通过级别规则 ID 查询级别规则信息
     * 
     * @param ruleId 级别规则 ID
     * @return 级别规则对象信息
     */
    public WorkLevelRule selectLevelRuleById(Long ruleId);

    /**
     * 根据职级查询级别规则
     * 
     * @param level 职级
     * @return 级别规则对象信息
     */
    public WorkLevelRule selectLevelRuleByLevel(Integer level);

    /**
     * 根据职能类型 ID 和职级查询级别规则（用于唯一性校验）
     * 
     * @param workLevelRule 级别规则信息（含 positionTypeId 和 level）
     * @return 级别规则对象信息
     */
    public WorkLevelRule selectLevelRuleByTypeAndLevel(WorkLevelRule workLevelRule);

    /**
     * 批量删除级别规则信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteLevelRuleByIds(Long[] ids);

    /**
     * 修改级别规则信息
     * 
     * @param workLevelRule 级别规则信息
     * @return 结果
     */
    public int updateLevelRule(WorkLevelRule workLevelRule);

    /**
     * 新增级别规则信息
     * 
     * @param workLevelRule 级别规则信息
     * @return 结果
     */
    public int insertLevelRule(WorkLevelRule workLevelRule);
}