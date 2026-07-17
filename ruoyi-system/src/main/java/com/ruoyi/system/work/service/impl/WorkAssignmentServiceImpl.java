package com.ruoyi.system.work.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.work.domain.WorkAssignment;
import com.ruoyi.system.work.domain.WorkEmployee;
import com.ruoyi.system.work.domain.WorkJobStage;
import com.ruoyi.system.work.domain.WorkLevelRule;
import com.ruoyi.system.work.mapper.WorkAssignmentMapper;
import com.ruoyi.system.work.mapper.WorkJobStageMapper;
import com.ruoyi.system.work.service.IWorkAssignmentService;
import com.ruoyi.system.work.service.IWorkEmployeeService;
import com.ruoyi.system.work.service.IWorkLevelRuleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 作业分配 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkAssignmentServiceImpl implements IWorkAssignmentService
{
    @Autowired
    private WorkAssignmentMapper assignmentMapper;

    @Autowired
    private WorkJobStageMapper jobStageMapper;

    @Autowired
    private IWorkEmployeeService employeeService;

    @Autowired
    private IWorkLevelRuleService levelRuleService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 查询作业分配列表
     * 
     * @param workAssignment 作业分配信息
     * @return 作业分配信息集合
     */
    @Override
    public List<WorkAssignment> selectAssignmentList(WorkAssignment workAssignment)
    {
        return assignmentMapper.selectAssignmentList(workAssignment);
    }

    /**
     * 根据作业阶段 ID 查询作业分配列表
     * 
     * @param jobStageId 作业阶段 ID
     * @return 作业分配信息集合
     */
    @Override
    public List<WorkAssignment> selectAssignmentsByJobStageId(Long jobStageId)
    {
        return assignmentMapper.selectAssignmentsByJobStageId(jobStageId);
    }

    /**
     * 根据员工 ID 查询作业分配列表
     * 
     * @param employeeId 员工 ID
     * @return 作业分配信息集合
     */
    @Override
    public List<WorkAssignment> selectAssignmentsByEmployeeId(Long employeeId)
    {
        return assignmentMapper.selectAssignmentsByEmployeeId(employeeId);
    }

    /**
     * 通过作业分配 ID 查询作业分配信息
     * 
     * @param assignId 作业分配 ID
     * @return 作业分配对象信息
     */
    @Override
    public WorkAssignment selectAssignmentById(Long assignId)
    {
        return assignmentMapper.selectAssignmentById(assignId);
    }

    /**
     * 查询待开始的作业分配
     * 
     * @param employeeId 员工 ID
     * @return 作业分配信息集合
     */
    @Override
    public List<WorkAssignment> selectPendingAssignments(Long employeeId)
    {
        return assignmentMapper.selectPendingAssignments(employeeId);
    }

    /**
     * 批量删除作业分配信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    public int deleteAssignmentByIds(String ids)
    {
        Long[] assignIds = Convert.toLongArray(ids);
        return assignmentMapper.deleteAssignmentByIds(assignIds);
    }

    /**
     * 新增保存作业分配信息
     * 
     * @param workAssignment 作业分配信息
     * @return 结果
     */
    @Override
    public int insertAssignment(WorkAssignment workAssignment)
    {
        // 检查员工负载
        WorkEmployee employee = employeeService.selectEmployeeById(workAssignment.getEmployeeId());
        if (employee != null)
        {
            Double currentWorkload = assignmentMapper.sumEmployeeWorkload(employee.getEmployeeId());
            if (currentWorkload + workAssignment.getWorkload() > employee.getMaxWorkload())
            {
                throw new ServiceException("员工当前负载已满，无法分配更多工作");
            }
        }
        return assignmentMapper.insertAssignment(workAssignment);
    }

    /**
     * 修改保存作业分配信息
     * 
     * @param workAssignment 作业分配信息
     * @return 结果
     */
    @Override
    public int updateAssignment(WorkAssignment workAssignment)
    {
        return assignmentMapper.updateAssignment(workAssignment);
    }

    /**
     * 更新作业分配状态
     * 
     * @param assignId 作业分配 ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateAssignmentStatus(Long assignId, String status)
    {
        return assignmentMapper.updateAssignmentStatus(assignId, status);
    }

    /**
     * 统计员工当前分配的工时总和
     * 
     * @param employeeId 员工 ID
     * @return 工时总和
     */
    @Override
    public Double sumEmployeeWorkload(Long employeeId)
    {
        return assignmentMapper.sumEmployeeWorkload(employeeId);
    }

    /**
     * 统计员工已完成的工时总和
     * 
     * @param employeeId 员工 ID
     * @return 工时总和
     */
    @Override
    public Double sumEmployeeCompletedWorkload(Long employeeId)
    {
        return assignmentMapper.sumEmployeeCompletedWorkload(employeeId);
    }

    /**
     * 智能分配作业阶段给员工
     * 
     * @param jobStageId 作业阶段 ID
     * @param employeeIds 员工 ID 列表
     * @param workloadPerEmployee 每人分配工时
     * @return 分配的记录 ID 列表
     */
    @Override
    @Transactional
    public List<Long> smartAssign(Long jobStageId, List<Long> employeeIds, Double workloadPerEmployee)
    {
        List<Long> result = new ArrayList<>();
        
        // 获取作业阶段信息
        WorkJobStage stage = jobStageMapper.selectJobStageById(jobStageId);
        if (stage == null)
        {
            throw new ServiceException("作业阶段不存在");
        }
        
        // 获取阶段要求的最低级别
        int minLevel = stage.getMinLevel() != null ? stage.getMinLevel() : 1;
        
        // 获取阶段要求的技能
        List<String> requiredSkills = new ArrayList<>();
        if (stage.getRequiredSkills() != null)
        {
            try
            {
                JsonNode skillsNode = objectMapper.readTree(stage.getRequiredSkills());
                for (JsonNode skillNode : skillsNode)
                {
                    requiredSkills.add(skillNode.asText());
                }
            }
            catch (Exception e)
            {
                // 忽略解析错误
            }
        }
        
        // 获取候选员工列表并计算匹配分数
        List<EmployeeScore> scoredEmployees = new ArrayList<>();
        for (Long employeeId : employeeIds)
        {
            WorkEmployee employee = employeeService.selectEmployeeById(employeeId);
            if (employee == null || !"0".equals(employee.getStatus()))
            {
                continue; // 跳过不存在的员工或离职员工
            }
            
            // 计算分数
            double score = calculateEmployeeScore(employee, minLevel, requiredSkills, workloadPerEmployee);
            scoredEmployees.add(new EmployeeScore(employee, score));
        }
        
        // 按分数降序排序
        scoredEmployees.sort(Comparator.comparingDouble(EmployeeScore::getScore).reversed());
        
        // 分配给分数最高的员工
        for (EmployeeScore es : scoredEmployees)
        {
            WorkAssignment assignment = new WorkAssignment();
            assignment.setJobStageId(jobStageId);
            assignment.setEmployeeId(es.getEmployee().getEmployeeId());
            assignment.setWorkload(workloadPerEmployee);
            assignment.setStatus("0"); // 待开始
            assignment.setAssignTime(DateUtils.getNowDate());
            assignment.setPriority((int) (es.getScore() * 10));
            
            assignmentMapper.insertAssignment(assignment);
            result.add(assignment.getAssignId());
        }
        
        return result;
    }

    /**
     * 取消分配
     * 
     * @param assignId 作业分配 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int cancelAssignment(Long assignId)
    {
        WorkAssignment assignment = assignmentMapper.selectAssignmentById(assignId);
        if (assignment == null)
        {
            throw new ServiceException("分配记录不存在");
        }
        
        // 只能取消待开始的分配
        if (!"0".equals(assignment.getStatus()))
        {
            throw new ServiceException("只能取消待开始的分配");
        }
        
        return assignmentMapper.deleteAssignmentByIds(new Long[]{assignId});
    }

    /**
     * 计算员工匹配分数
     * 
     * @param employee 员工信息
     * @param minLevel 最低级别要求
     * @param requiredSkills 技能要求
     * @param workload 分配工时
     * @return 匹配分数
     */
    private double calculateEmployeeScore(WorkEmployee employee, int minLevel, List<String> requiredSkills, double workload)
    {
        double score = 0.0;
        
        // 1. 负载分 (0-40 分)：负载越低分数越高
        double loadRatio = employee.getCurrentWorkload() / employee.getMaxWorkload();
        double loadScore = (1 - loadRatio) * 40;
        score += loadScore;
        
        // 2. 级别匹配分 (0-30 分)：级别越高且满足最低要求分数越高
        if (employee.getPositionLevel() >= minLevel)
        {
            double levelScore = Math.min(30, (employee.getPositionLevel() - minLevel + 1) * 5);
            score += levelScore;
        }
        else
        {
            // 级别不满足要求，扣分
            score -= 20;
        }
        
        // 3. 技能匹配分 (0-30 分)
        if (!requiredSkills.isEmpty() && employee.getSkills() != null)
        {
            try
            {
                JsonNode skillsNode = objectMapper.readTree(employee.getSkills());
                List<String> employeeSkills = new ArrayList<>();
                for (JsonNode skillNode : skillsNode)
                {
                    employeeSkills.add(skillNode.asText());
                }
                
                long matchCount = requiredSkills.stream()
                    .filter(skill -> employeeSkills.contains(skill))
                    .count();
                
                double skillScore = (double) matchCount / requiredSkills.size() * 30;
                score += skillScore;
            }
            catch (Exception e)
            {
                // 忽略解析错误
            }
        }
        else if (requiredSkills.isEmpty())
        {
            // 没有技能要求，给满分
            score += 30;
        }
        
        return score;
    }

    /**
     * 员工分数内部类
     */
    private static class EmployeeScore
    {
        private final WorkEmployee employee;
        private final double score;

        public EmployeeScore(WorkEmployee employee, double score)
        {
            this.employee = employee;
            this.score = score;
        }

        public WorkEmployee getEmployee()
        {
            return employee;
        }

        public double getScore()
        {
            return score;
        }
    }
}