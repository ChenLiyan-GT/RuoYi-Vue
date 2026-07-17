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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.work.domain.WorkAssignment;
import com.ruoyi.system.work.domain.WorkEmployee;
import com.ruoyi.system.work.domain.WorkJobStage;
import com.ruoyi.system.work.service.IWorkAssignmentService;
import com.ruoyi.system.work.service.IWorkEmployeeService;
import com.ruoyi.system.work.service.IWorkJobStageService;

/**
 * 作业分配操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/assignment")
public class WorkAssignmentController extends BaseController
{
    private String prefix = "system/work/assignment";

    @Autowired
    private IWorkAssignmentService workAssignmentService;

    @Autowired
    private IWorkJobStageService jobStageService;

    @Autowired
    private IWorkEmployeeService employeeService;

    @RequiresPermissions("system:work:assignment:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/assignment";
    }

    @RequiresPermissions("system:work:assignment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkAssignment workAssignment)
    {
        List<WorkAssignment> list = workAssignmentService.selectAssignmentList(workAssignment);
        return getDataTable(list);
    }

    @RequiresPermissions("system:work:assignment:list")
    @GetMapping("/list/byJobStage/{jobStageId}")
    @ResponseBody
    public AjaxResult listByJobStage(@PathVariable("jobStageId") Long jobStageId)
    {
        List<WorkAssignment> list = workAssignmentService.selectAssignmentsByJobStageId(jobStageId);
        return success(list);
    }

    @RequiresPermissions("system:work:assignment:list")
    @GetMapping("/list/byEmployee/{employeeId}")
    @ResponseBody
    public AjaxResult listByEmployee(@PathVariable("employeeId") Long employeeId)
    {
        List<WorkAssignment> list = workAssignmentService.selectAssignmentsByEmployeeId(employeeId);
        return success(list);
    }

    @RequiresPermissions("system:work:assignment:list")
    @GetMapping("/list/pending/{employeeId}")
    @ResponseBody
    public AjaxResult listPending(@PathVariable("employeeId") Long employeeId)
    {
        List<WorkAssignment> list = workAssignmentService.selectPendingAssignments(employeeId);
        return success(list);
    }

    @RequiresPermissions("system:work:assignment:remove")
    @Log(title = "作业分配", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workAssignmentService.deleteAssignmentByIds(ids));
    }

    /**
     * 新增作业分配
     */
    @RequiresPermissions("system:work:assignment:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("jobStages", jobStageService.selectJobStageList(new WorkJobStage()));
        mmap.put("employees", employeeService.selectEmployeeList(new WorkEmployee()));
        return prefix + "/add";
    }

    /**
     * 新增保存作业分配
     */
    @RequiresPermissions("system:work:assignment:add")
    @Log(title = "作业分配", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkAssignment workAssignment)
    {
        return toAjax(workAssignmentService.insertAssignment(workAssignment));
    }

    /**
     * 修改作业分配
     */
    @RequiresPermissions("system:work:assignment:edit")
    @GetMapping("/edit/{assignId}")
    public String edit(@PathVariable("assignId") Long assignId, ModelMap mmap)
    {
        mmap.put("assignment", workAssignmentService.selectAssignmentById(assignId));
        mmap.put("jobStages", jobStageService.selectJobStageList(new WorkJobStage()));
        mmap.put("employees", employeeService.selectEmployeeList(new WorkEmployee()));
        return prefix + "/edit";
    }

    /**
     * 修改保存作业分配
     */
    @RequiresPermissions("system:work:assignment:edit")
    @Log(title = "作业分配", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WorkAssignment workAssignment)
    {
        return toAjax(workAssignmentService.updateAssignment(workAssignment));
    }

    /**
     * 查看作业分配详情
     */
    @RequiresPermissions("system:work:assignment:query")
    @GetMapping("/detail/{assignId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable("assignId") Long assignId)
    {
        WorkAssignment assignment = workAssignmentService.selectAssignmentById(assignId);
        return success(assignment);
    }

    /**
     * 更新作业分配状态
     */
    @RequiresPermissions("system:work:assignment:edit")
    @Log(title = "作业分配", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    @ResponseBody
    public AjaxResult updateStatus(@RequestParam Long assignId, @RequestParam String status)
    {
        return toAjax(workAssignmentService.updateAssignmentStatus(assignId, status));
    }

    /**
     * 取消分配
     */
    @RequiresPermissions("system:work:assignment:edit")
    @Log(title = "作业分配", businessType = BusinessType.DELETE)
    @PostMapping("/cancel/{assignId}")
    @ResponseBody
    public AjaxResult cancel(@PathVariable("assignId") Long assignId)
    {
        return toAjax(workAssignmentService.cancelAssignment(assignId));
    }

    /**
     * 智能分配
     */
    @RequiresPermissions("system:work:assignment:add")
    @Log(title = "作业分配", businessType = BusinessType.INSERT)
    @PostMapping("/smartAssign")
    @ResponseBody
    public AjaxResult smartAssign(@RequestParam Long jobStageId, 
                                   @RequestParam List<Long> employeeIds,
                                   @RequestParam Double workloadPerEmployee)
    {
        List<Long> assignIds = workAssignmentService.smartAssign(jobStageId, employeeIds, workloadPerEmployee);
        return success(assignIds);
    }

    /**
     * 获取员工负载情况
     */
    @RequiresPermissions("system:work:assignment:query")
    @GetMapping("/employeeWorkload/{employeeId}")
    @ResponseBody
    public AjaxResult getEmployeeWorkload(@PathVariable("employeeId") Long employeeId)
    {
        WorkEmployee employee = employeeService.selectEmployeeById(employeeId);
        if (employee == null)
        {
            return error("员工不存在");
        }
        
        Double currentWorkload = workAssignmentService.sumEmployeeWorkload(employeeId);
        Double completedWorkload = workAssignmentService.sumEmployeeCompletedWorkload(employeeId);
        
        AjaxResult result = AjaxResult.success();
        result.put("employee", employee);
        result.put("currentWorkload", currentWorkload);
        result.put("completedWorkload", completedWorkload);
        result.put("maxWorkload", employee.getMaxWorkload());
        result.put("availableWorkload", employee.getMaxWorkload() - currentWorkload);
        result.put("loadRatio", currentWorkload / employee.getMaxWorkload() * 100);
        
        return result;
    }
}