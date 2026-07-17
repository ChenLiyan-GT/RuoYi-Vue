package com.ruoyi.system.work.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.work.domain.WorkPositionType;
import com.ruoyi.system.work.mapper.WorkPositionTypeMapper;
import com.ruoyi.system.work.service.IWorkPositionTypeService;

/**
 * 职能类型 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class WorkPositionTypeServiceImpl implements IWorkPositionTypeService
{
    @Autowired
    private WorkPositionTypeMapper positionTypeMapper;

    /**
     * 查询职能类型列表
     * 
     * @param workPositionType 职能类型信息
     * @return 职能类型信息集合
     */
    @Override
    public List<WorkPositionType> selectPositionTypeList(WorkPositionType workPositionType)
    {
        return positionTypeMapper.selectPositionTypeList(workPositionType);
    }

    /**
     * 查询所有职能类型
     * 
     * @return 职能类型列表
     */
    @Override
    public List<WorkPositionType> selectPositionTypeAll()
    {
        return positionTypeMapper.selectPositionTypeAll();
    }

    /**
     * 通过职能类型 ID 查询职能类型信息
     * 
     * @param typeId 职能类型 ID
     * @return 职能类型对象信息
     */
    @Override
    public WorkPositionType selectPositionTypeById(Long typeId)
    {
        return positionTypeMapper.selectPositionTypeById(typeId);
    }

    /**
     * 根据类型编码查询职能类型信息
     * 
     * @param typeCode 类型编码
     * @return 职能类型对象信息
     */
    @Override
    public WorkPositionType checkTypeCodeUnique(String typeCode)
    {
        return positionTypeMapper.checkTypeCodeUnique(typeCode);
    }

    /**
     * 批量删除职能类型信息
     * 
     * @param ids 需要删除的数据 ID
     * @return 结果
     */
    @Override
    public int deletePositionTypeByIds(String ids)
    {
        Long[] typeIds = Convert.toLongArray(ids);
        return positionTypeMapper.deletePositionTypeByIds(typeIds);
    }

    /**
     * 新增保存职能类型信息
     * 
     * @param workPositionType 职能类型信息
     * @return 结果
     */
    @Override
    public int insertPositionType(WorkPositionType workPositionType)
    {
        // 校验类型编码唯一性
        if (!checkTypeCodeUnique(workPositionType))
        {
            throw new ServiceException("新增职能类型'" + workPositionType.getTypeName() + "'失败，类型编码已存在");
        }
        return positionTypeMapper.insertPositionType(workPositionType);
    }

    /**
     * 修改保存职能类型信息
     * 
     * @param workPositionType 职能类型信息
     * @return 结果
     */
    @Override
    public int updatePositionType(WorkPositionType workPositionType)
    {
        // 校验类型编码唯一性
        if (!checkTypeCodeUnique(workPositionType))
        {
            throw new ServiceException("修改职能类型'" + workPositionType.getTypeName() + "'失败，类型编码已存在");
        }
        return positionTypeMapper.updatePositionType(workPositionType);
    }

    /**
     * 校验职能类型编码是否唯一
     * 
     * @param workPositionType 职能类型信息
     * @return 结果
     */
    @Override
    public boolean checkTypeCodeUnique(WorkPositionType workPositionType)
    {
        Long typeId = StringUtils.isNull(workPositionType.getTypeId()) ? -1L : workPositionType.getTypeId();
        WorkPositionType info = positionTypeMapper.checkTypeCodeUnique(workPositionType.getTypeCode());
        if (StringUtils.isNotNull(info) && info.getTypeId().longValue() != typeId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}