package com.ruoyi.system.work.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.work.domain.WorkEmployee;
import com.ruoyi.system.work.mapper.WorkEmployeeMapper;
import com.ruoyi.system.work.service.IWorkEmployeeService;

/**
 * 员工信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkEmployeeServiceImpl implements IWorkEmployeeService
{
    @Autowired
    private WorkEmployeeMapper employeeMapper;

    /**
     * 查询员工列表
     * 
     * @param workEmployee 员工信息
     * @return 员工信息集合
     */
    @Override
    public List<WorkEmployee> selectEmployeeList(WorkEmployee workEmployee)
    {
        return employeeMapper.selectEmployeeList(workEmployee);
    }

    /**
     * 查询所有员工
     * 
     * @return 员工列表
     */
    @Override
    public List<WorkEmployee> selectEmployeeAll()
    {
        return employeeMapper.selectEmployeeAll();
    }

    /**
     * 通过员工 ID 查询员工信息
     * 
     * @param employeeId 员工 ID
     * @return 员工对象信息
     */
    @Override
    public WorkEmployee selectEmployeeById(Long employeeId)
    {
        return employeeMapper.selectEmployeeById(employeeId);
    }

    /**
     * 根据工号查询员工信息
     * 
     * @param employeeNo 员工工号
     * @return 员工对象信息
     */
    @Override
    public WorkEmployee checkEmployeeNoUnique(String employeeNo)
    {
        return employeeMapper.checkEmployeeNoUnique(employeeNo);
    }

    /**
     * 根据部门 ID 查询员工列表
     * 
     * @param deptId 部门 ID
     * @return 员工数据集合
     */
    @Override
    public List<WorkEmployee> selectEmployeesByDeptId(Long deptId)
    {
        return employeeMapper.selectEmployeesByDeptId(deptId);
    }

    /**
     * 根据职能和级别查询员工列表
     * 
     * @param positionType 职能类型
     * @param minLevel 最低级别
     * @return 员工数据集合
     */
    @Override
    public List<WorkEmployee> selectEmployeesByPosition(String positionType, Integer minLevel)
    {
        return employeeMapper.selectEmployeesByPosition(positionType, minLevel);
    }

    /**
     * 查询空闲员工列表
     * 
     * @param positionType 职能类型
     * @param minLevel 最低级别
     * @return 空闲员工数据集合
     */
    @Override
    public List<WorkEmployee> selectIdleEmployees(String positionType, Integer minLevel)
    {
        return employeeMapper.selectIdleEmployees(positionType, minLevel);
    }

    /**
     * 批量删除员工信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeByIds(String ids)
    {
        Long[] employeeIds = Convert.toLongArray(ids);
        return employeeMapper.deleteEmployeeByIds(employeeIds);
    }

    /**
     * 新增保存员工信息
     * 
     * @param workEmployee 员工信息
     * @return 结果
     */
    @Override
    public int insertEmployee(WorkEmployee workEmployee)
    {
        // 校验工号唯一性
        if (!checkEmployeeNoUnique(workEmployee))
        {
            throw new ServiceException("新增员工'" + workEmployee.getEmployeeName() + "'失败，工号已存在");
        }
        // 初始化负载状态
        workEmployee.setStatus(calculateLoadStatus(workEmployee));
        return employeeMapper.insertEmployee(workEmployee);
    }

    /**
     * 修改保存员工信息
     * 
     * @param workEmployee 员工信息
     * @return 结果
     */
    @Override
    public int updateEmployee(WorkEmployee workEmployee)
    {
        // 校验工号唯一性
        if (!checkEmployeeNoUnique(workEmployee))
        {
            throw new ServiceException("修改员工'" + workEmployee.getEmployeeName() + "'失败，工号已存在");
        }
        // 更新负载状态
        workEmployee.setStatus(calculateLoadStatus(workEmployee));
        return employeeMapper.updateEmployee(workEmployee);
    }

    /**
     * 更新员工负载
     * 
     * @param workEmployee 员工信息
     * @return 结果
     */
    @Override
    public int updateEmployeeWorkload(WorkEmployee workEmployee)
    {
        // 计算并更新负载状态
        workEmployee.setStatus(calculateLoadStatus(workEmployee));
        return employeeMapper.updateEmployeeWorkload(workEmployee);
    }

    /**
     * 计算员工负载状态
     * 
     * @param workEmployee 员工信息
     * @return 负载状态 (0 空闲 1 工作中 2 过载)
     */
    @Override
    public String calculateLoadStatus(WorkEmployee workEmployee)
    {
        BigDecimal currentWorkload = workEmployee.getCurrentWorkload();
        BigDecimal maxWorkload = workEmployee.getMaxWorkload();
        
        if (currentWorkload == null || maxWorkload == null || maxWorkload.compareTo(BigDecimal.ZERO) == 0)
        {
            return "0"; // 默认空闲
        }
        
        BigDecimal ratio = currentWorkload.divide(maxWorkload, 2, BigDecimal.ROUND_HALF_UP);
        
        // 负载率 < 50% 为空闲，50%-90% 为工作中，> 90% 为过载
        if (ratio.compareTo(new BigDecimal("0.5")) < 0)
        {
            return "0"; // 空闲
        }
        else if (ratio.compareTo(new BigDecimal("0.9")) < 0)
        {
            return "1"; // 工作中
        }
        else
        {
            return "2"; // 过载
        }
    }

    /**
     * 校验员工工号是否唯一
     * 
     * @param workEmployee 员工信息
     * @return 结果
     */
    @Override
    public boolean checkEmployeeNoUnique(WorkEmployee workEmployee)
    {
        Long employeeId = StringUtils.isNull(workEmployee.getEmployeeId()) ? -1L : workEmployee.getEmployeeId();
        WorkEmployee info = employeeMapper.checkEmployeeNoUnique(workEmployee.getEmployeeNo());
        if (StringUtils.isNotNull(info) && info.getEmployeeId().longValue() != employeeId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}