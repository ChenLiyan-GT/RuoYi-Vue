package com.ruoyi.web.controller.system;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.work.domain.WorkPositionType;
import com.ruoyi.system.work.service.IWorkPositionTypeService;

/**
 * 职能类型操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/positionType")
public class WorkPositionTypeController extends BaseController
{
    private String prefix = "system/work/positionType";

    @Autowired
    private IWorkPositionTypeService positionTypeService;

    @RequiresPermissions("system:work:positionType:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/positionType";
    }

    @RequiresPermissions("system:work:positionType:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkPositionType workPositionType)
    {
        List<WorkPositionType> list = positionTypeService.selectPositionTypeList(workPositionType);
        return getDataTable(list);
    }

    @RequiresPermissions("system:work:positionType:remove")
    @Log(title = "职能类型", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(positionTypeService.deletePositionTypeByIds(ids));
    }

    /**
     * 新增职能类型
     */
    @RequiresPermissions("system:work:positionType:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存职能类型
     */
    @RequiresPermissions("system:work:positionType:add")
    @Log(title = "职能类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkPositionType workPositionType)
    {
        if (!positionTypeService.checkTypeCodeUnique(workPositionType))
        {
            return error("新增职能类型'" + workPositionType.getTypeName() + "'失败，类型编码已存在");
        }
        return toAjax(positionTypeService.insertPositionType(workPositionType));
    }

    /**
     * 修改职能类型
     */
    @RequiresPermissions("system:work:positionType:edit")
    @GetMapping("/edit/{typeId}")
    public String edit(@PathVariable("typeId") Long typeId, ModelMap mmap)
    {
        mmap.put("positionType", positionTypeService.selectPositionTypeById(typeId));
        return prefix + "/edit";
    }

    /**
     * 修改保存职能类型
     */
    @RequiresPermissions("system:work:positionType:edit")
    @Log(title = "职能类型", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WorkPositionType workPositionType)
    {
        if (!positionTypeService.checkTypeCodeUnique(workPositionType))
        {
            return error("修改职能类型'" + workPositionType.getTypeName() + "'失败，类型编码已存在");
        }
        return toAjax(positionTypeService.updatePositionType(workPositionType));
    }

    /**
     * 查询所有职能类型列表
     */
    @RequiresPermissions("system:work:positionType:list")
    @GetMapping("/list/all")
    @ResponseBody
    public AjaxResult listAll()
    {
        List<WorkPositionType> list = positionTypeService.selectPositionTypeAll();
        return success(list);
    }

    /**
     * 校验职能类型编码
     */
    @PostMapping("/checkTypeCodeUnique")
    @ResponseBody
    public boolean checkTypeCodeUnique(WorkPositionType workPositionType)
    {
        return positionTypeService.checkTypeCodeUnique(workPositionType);
    }
}