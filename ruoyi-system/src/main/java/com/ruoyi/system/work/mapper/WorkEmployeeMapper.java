package com.ruoyi.system.work.mapper;

import java.util.List;
import com.ruoyi.system.work.domain.WorkEmployee;

/**
 * 员工信息 数据层
 * 
 * @author ruoyi
 */
public interface WorkEmployeeMapper
{
    /**
     * 查询员工列表
     * 
     * @param workEmployee 员工信息
     * @return 员工数据集合
     */
    public List<WorkEmployee> selectEmployeeList(WorkEmployee workEmployee);

    /**
     * 查询所有员工
     * 
     * @return 员工列表
     */
    public List<WorkEmployee> selectEmployeeAll();

    /**
     * 通过员工 ID 查询员工信息
     * 
     * @param employeeId 员工 ID
     * @return 员工对象信息
     */
    public WorkEmployee selectEmployeeById(Long employeeId);

    /**
     * 根据工号查询员工信息
     * 
     * @param employeeNo 员工工号
     * @return 员工对象信息
     */
    public WorkEmployee checkEmployeeNoUnique(String employeeNo);

    /**
     * 根据部门 ID 查询员工列表
     * 
     * @param deptId 部门 ID
     * @return 员工数据集合
     */
    public List<WorkEmployee> selectEmployeesByDeptId(Long deptId);

    /**
     * 根据职能和级别查询员工列表
     * 
     * @param positionType 职能类型
     * @param minLevel 最低级别
     * @return 员工数据集合
     */
    public List<WorkEmployee> selectEmployeesByPosition(String positionType, Integer minLevel);

    /**
     * 查询空闲员工列表
     * 
     * @param positionType 职能类型
     * @param minLevel 最低级别
     * @return 空闲员工数据集合
     */
    public List<WorkEmployee> selectIdleEmployees(String positionType, Integer minLevel);

    /**
     * 批量删除员工信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteEmployeeByIds(Long[] ids);

    /**
     * 修改员工信息
     * 
     * @param workEmployee 员工信息
     * @return 结果
     */
    public int updateEmployee(WorkEmployee workEmployee);

    /**
     * 新增员工信息
     * 
     * @param workEmployee 员工信息
     * @return 结果
     */
    public int insertEmployee(WorkEmployee workEmployee);

    /**
     * 更新员工负载
     * 
     * @param workEmployee 员工信息
     * @return 结果
     */
    public int updateEmployeeWorkload(WorkEmployee workEmployee);
}