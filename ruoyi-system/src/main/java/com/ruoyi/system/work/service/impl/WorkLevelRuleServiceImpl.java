package com.ruoyi.system.work.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.work.domain.WorkLevelRule;
import com.ruoyi.system.work.mapper.WorkLevelRuleMapper;
import com.ruoyi.system.work.service.IWorkLevelRuleService;

/**
 * 级别规则 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkLevelRuleServiceImpl implements IWorkLevelRuleService
{
    @Autowired
    private WorkLevelRuleMapper levelRuleMapper;

    /**
     * 查询级别规则列表
     * 
     * @param workLevelRule 级别规则信息
     * @return 级别规则信息集合
     */
    @Override
    public List<WorkLevelRule> selectLevelRuleList(WorkLevelRule workLevelRule)
    {
        return levelRuleMapper.selectLevelRuleList(workLevelRule);
    }

    /**
     * 查询所有级别规则
     * 
     * @return 级别规则列表
     */
    @Override
    public List<WorkLevelRule> selectLevelRuleAll()
    {
        return levelRuleMapper.selectLevelRuleAll();
    }

    /**
     * 通过级别规则 ID 查询级别规则信息
     * 
     * @param ruleId 级别规则 ID
     * @return 级别规则对象信息
     */
    @Override
    public WorkLevelRule selectLevelRuleById(Long ruleId)
    {
        return levelRuleMapper.selectLevelRuleById(ruleId);
    }

    /**
     * 根据职级查询级别规则
     * 
     * @param level 职级
     * @return 级别规则对象信息
     */
    @Override
    public WorkLevelRule selectLevelRuleByLevel(Integer level)
    {
        return levelRuleMapper.selectLevelRuleByLevel(level);
    }

    /**
     * 批量删除级别规则信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    public int deleteLevelRuleByIds(String ids)
    {
        Long[] ruleIds = Convert.toLongArray(ids);
        return levelRuleMapper.deleteLevelRuleByIds(ruleIds);
    }

    /**
     * 新增保存级别规则信息
     * 
     * @param workLevelRule 级别规则信息
     * @return 结果
     */
    @Override
    public int insertLevelRule(WorkLevelRule workLevelRule)
    {
        return levelRuleMapper.insertLevelRule(workLevelRule);
    }

    /**
     * 修改保存级别规则信息
     * 
     * @param workLevelRule 级别规则信息
     * @return 结果
     */
    @Override
    public int updateLevelRule(WorkLevelRule workLevelRule)
    {
        return levelRuleMapper.updateLevelRule(workLevelRule);
    }

    /**
     * 校验职级是否唯一
     * 
     * @param workLevelRule 级别规则信息
     * @return 结果
     */
    @Override
    public boolean isLevelUnique(WorkLevelRule workLevelRule)
    {
        Long ruleId = StringUtils.isNull(workLevelRule.getRuleId()) ? -1L : workLevelRule.getRuleId();
        WorkLevelRule info = levelRuleMapper.selectLevelRuleByTypeAndLevel(workLevelRule);
        if (StringUtils.isNotNull(info) && info.getRuleId().longValue() != ruleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据职级获取可执行的作业阶段范围
     * 
     * @param level 职级
     * @return 阶段范围描述
     */
    @Override
    public String getStageRangeByLevel(Integer level)
    {
        WorkLevelRule rule = levelRuleMapper.selectLevelRuleByLevel(level);
        if (rule == null)
        {
            return "未知职级";
        }
        return rule.getMinStage() + " ~ " + rule.getMaxStage();
    }

    /**
     * 根据职能类型 ID 和职级获取可执行的作业阶段范围
     * 
     * @param positionTypeId 职能类型 ID
     * @param level 职级
     * @return 阶段范围描述
     */
    @Override
    public String getStageRangeByTypeAndLevel(Long positionTypeId, Integer level)
    {
        WorkLevelRule query = new WorkLevelRule();
        query.setPositionTypeId(positionTypeId);
        query.setLevel(level);
        WorkLevelRule rule = levelRuleMapper.selectLevelRuleByTypeAndLevel(query);
        if (rule == null)
        {
            return "未知职级";
        }
        return rule.getMinStage() + " ~ " + rule.getMaxStage();
    }
}