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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.work.domain.WorkEmployee;
import com.ruoyi.system.work.service.IWorkEmployeeService;

/**
 * 员工信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/employee")
public class WorkEmployeeController extends BaseController
{
    private String prefix = "system/work/employee";

    @Autowired
    private IWorkEmployeeService employeeService;

    @RequiresPermissions("system:work:employee:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/employee";
    }

    @RequiresPermissions("system:work:employee:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkEmployee workEmployee)
    {
        startPage();
        List<WorkEmployee> list = employeeService.selectEmployeeList(workEmployee);
        return getDataTable(list);
    }

    @Log(title = "员工管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:work:employee:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkEmployee workEmployee)
    {
        List<WorkEmployee> list = employeeService.selectEmployeeList(workEmployee);
        ExcelUtil<WorkEmployee> util = new ExcelUtil<WorkEmployee>(WorkEmployee.class);
        return util.exportExcel(list, "员工数据");
    }

    @RequiresPermissions("system:work:employee:remove")
    @Log(title = "员工管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(employeeService.deleteEmployeeByIds(ids));
    }

    /**
     * 新增员工
     */
    @RequiresPermissions("system:work:employee:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存员工
     */
    @RequiresPermissions("system:work:employee:add")
    @Log(title = "员工管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkEmployee workEmployee)
    {
        if (!employeeService.checkEmployeeNoUnique(workEmployee))
        {
            return error("新增员工'" + workEmployee.getEmployeeName() + "'失败，工号已存在");
        }
        workEmployee.setCreateBy(getLoginName());
        return toAjax(employeeService.insertEmployee(workEmployee));
    }

    /**
     * 修改员工
     */
    @RequiresPermissions("system:work:employee:edit")
    @GetMapping("/edit/{employeeId}")
    public String edit(@PathVariable("employeeId") Long employeeId, ModelMap mmap)
    {
        mmap.put("employee", employeeService.selectEmployeeById(employeeId));
        return prefix + "/edit";
    }

    /**
     * 修改保存员工
     */
    @RequiresPermissions("system:work:employee:edit")
    @Log(title = "员工管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WorkEmployee workEmployee)
    {
        if (!employeeService.checkEmployeeNoUnique(workEmployee))
        {
            return error("修改员工'" + workEmployee.getEmployeeName() + "'失败，工号已存在");
        }
        workEmployee.setUpdateBy(getLoginName());
        return toAjax(employeeService.updateEmployee(workEmployee));
    }

    /**
     * 查询空闲员工列表
     */
    @RequiresPermissions("system:work:employee:list")
    @PostMapping("/idle/list")
    @ResponseBody
    public TableDataInfo listIdleEmployees(String positionType, Integer minLevel)
    {
        List<WorkEmployee> list = employeeService.selectIdleEmployees(positionType, minLevel);
        return getDataTable(list);
    }

    /**
     * 根据职能和级别查询员工列表
     */
    @RequiresPermissions("system:work:employee:list")
    @PostMapping("/position/list")
    @ResponseBody
    public TableDataInfo listByPosition(String positionType, Integer minLevel)
    {
        List<WorkEmployee> list = employeeService.selectEmployeesByPosition(positionType, minLevel);
        return getDataTable(list);
    }

    /**
     * 校验员工工号
     */
    @PostMapping("/checkEmployeeNoUnique")
    @ResponseBody
    public boolean checkEmployeeNoUnique(WorkEmployee workEmployee)
    {
        return employeeService.checkEmployeeNoUnique(workEmployee);
    }
}