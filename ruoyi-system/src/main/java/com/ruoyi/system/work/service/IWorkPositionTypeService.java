package com.ruoyi.system.work.service;

import java.util.List;
import com.ruoyi.system.work.domain.WorkPositionType;

/**
 * 职能类型 服务层
 * 
 * @author ruoyi
 */
public interface IWorkPositionTypeService
{
    /**
     * 查询职能类型列表
     * 
     * @param workPositionType 职能类型信息
     * @return 职能类型信息集合
     */
    public List<WorkPositionType> selectPositionTypeList(WorkPositionType workPositionType);

    /**
     * 查询所有职能类型
     * 
     * @return 职能类型列表
     */
    public List<WorkPositionType> selectPositionTypeAll();

    /**
     * 通过职能类型 ID 查询职能类型信息
     * 
     * @param typeId 职能类型 ID
     * @return 职能类型对象信息
     */
    public WorkPositionType selectPositionTypeById(Long typeId);

    /**
     * 根据类型编码查询职能类型信息
     * 
     * @param typeCode 类型编码
     * @return 职能类型对象信息
     */
    public WorkPositionType checkTypeCodeUnique(String typeCode);

    /**
     * 批量删除职能类型信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    public int deletePositionTypeByIds(String ids);

    /**
     * 新增保存职能类型信息
     * 
     * @param workPositionType 职能类型信息
     * @return 结果
     */
    public int insertPositionType(WorkPositionType workPositionType);

    /**
     * 修改保存职能类型信息
     * 
     * @param workPositionType 职能类型信息
     * @return 结果
     */
    public int updatePositionType(WorkPositionType workPositionType);

    /**
     * 校验职能类型编码是否唯一
     * 
     * @param workPositionType 职能类型信息
     * @return 结果
     */
    public boolean checkTypeCodeUnique(WorkPositionType workPositionType);
}