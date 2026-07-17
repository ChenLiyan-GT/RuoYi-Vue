package com.ruoyi.system.work.mapper;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.system.work.domain.WorkJob;

/**
 * 作业 数据层
 * 
 * @author ruoyi
 */
public interface WorkJobMapper
{
    /**
     * 查询作业列表
     * 
     * @param workJob 作业信息
     * @return 作业数据集合
     */
    public List<WorkJob> selectJobList(WorkJob workJob);

    /**
     * 查询所有作业
     * 
     * @return 作业列表
     */
    public List<WorkJob> selectJobAll();

    /**
     * 通过作业 ID 查询作业信息
     * 
     * @param jobId 作业 ID
     * @return 作业对象信息
     */
    public WorkJob selectJobById(Long jobId);

    /**
     * 根据作业编号查询作业信息
     * 
     * @param jobNo 作业编号
     * @return 作业对象信息
     */
    public WorkJob checkJobNoUnique(String jobNo);

    /**
     * 根据部门 ID 查询作业列表
     * 
     * @param deptId 部门 ID
     * @return 作业数据集合
     */
    public List<WorkJob> selectJobsByDeptId(Long deptId);

    /**
     * 根据状态查询作业列表
     * 
     * @param status 状态
     * @return 作业数据集合
     */
    public List<WorkJob> selectJobsByStatus(String status);

    /**
     * 批量删除作业信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deleteJobByIds(Long[] ids);

    /**
     * 修改作业信息
     * 
     * @param workJob 作业信息
     * @return 结果
     */
    public int updateJob(WorkJob workJob);

    /**
     * 新增作业信息
     * 
     * @param workJob 作业信息
     * @return 结果
     */
    public int insertJob(WorkJob workJob);

    /**
     * 更新作业进度
     * 
     * @param jobId 作业 ID
     * @param progress 进度
     * @return 结果
     */
    public int updateJobProgress(Long jobId, BigDecimal progress);

    /**
     * 更新作业状态
     * 
     * @param jobId 作业 ID
     * @param status 状态
     * @return 结果
     */
    public int updateJobStatus(Long jobId, String status);

    /**
     * 统计部门作业数量
     * 
     * @param deptId 部门 ID
     * @param status 状态
     * @return 数量
     */
    public int countJobsByDeptAndStatus(Long deptId, String status);
}