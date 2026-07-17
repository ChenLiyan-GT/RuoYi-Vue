package com.ruoyi.system.work.service;

import java.util.List;
import com.ruoyi.system.work.domain.WorkLevelRule;

/**
 * 级别规则 服务层
 * 
 * @author ruoyi
 */
public interface IWorkLevelRuleService
{
    /**
     * 查询级别规则列表
     * 
     * @param workLevelRule 级别规则信息
     * @return 级别规则信息集合
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
    public WorkLevelRule checkLevelUnique(Integer level);

    /**
     * 批量删除级别规则信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteLevelRuleByIds(String ids);

    /**
     * 新增保存级别规则信息
     * 
     * @param workLevelRule 级别规则信息
     * @return 结果
     */
    public int insertLevelRule(WorkLevelRule workLevelRule);

    /**
     * 修改保存级别规则信息
     * 
     * @param workLevelRule 级别规则信息
     * @return 结果
     */
    public int updateLevelRule(WorkLevelRule workLevelRule);

    /**
     * 校验职级是否唯一
     * 
     * @param workLevelRule 级别规则信息
     * @return 结果
     */
    public boolean checkLevelUnique(WorkLevelRule workLevelRule);

    /**
     * 根据职级获取可执行的作业阶段范围
     * 
     * @param level 职级
     * @return 阶段范围描述
     */
    public String getStageRangeByLevel(Integer level);
}