package com.ruoyi.web.controller.system;

import java.util.Date;
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
import com.ruoyi.system.work.domain.WorkTimesheet;
import com.ruoyi.system.work.service.IWorkTimesheetService;
import com.ruoyi.system.work.service.IWorkAssignmentService;

/**
 * 工时记录操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/timesheet")
public class WorkTimesheetController extends BaseController
{
    private String prefix = "system/work/timesheet";

    @Autowired
    private IWorkTimesheetService workTimesheetService;

    @Autowired
    private IWorkAssignmentService workAssignmentService;

    @RequiresPermissions("system:work:timesheet:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/timesheet";
    }

    @RequiresPermissions("system:work:timesheet:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkTimesheet workTimesheet)
    {
        List<WorkTimesheet> list = workTimesheetService.selectTimesheetList(workTimesheet);
        return getDataTable(list);
    }

    @RequiresPermissions("system:work:timesheet:list")
    @GetMapping("/list/byAssign/{assignId}")
    @ResponseBody
    public AjaxResult listByAssign(@PathVariable("assignId") Long assignId)
    {
        List<WorkTimesheet> list = workTimesheetService.selectTimesheetsByAssignId(assignId);
        return success(list);
    }

    @RequiresPermissions("system:work:timesheet:list")
    @GetMapping("/list/byEmployee/{employeeId}")
    @ResponseBody
    public AjaxResult listByEmployee(@PathVariable("employeeId") Long employeeId)
    {
        List<WorkTimesheet> list = workTimesheetService.selectTimesheetsByEmployeeId(employeeId);
        return success(list);
    }

    @RequiresPermissions("system:work:timesheet:list")
    @GetMapping("/list/byDateRange")
    @ResponseBody
    public AjaxResult listByDateRange(@RequestParam Long employeeId,
                                       @RequestParam Date startDate,
                                       @RequestParam Date endDate)
    {
        List<WorkTimesheet> list = workTimesheetService.selectTimesheetsByDateRange(employeeId, startDate, endDate);
        return success(list);
    }

    @RequiresPermissions("system:work:timesheet:remove")
    @Log(title = "工时记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workTimesheetService.deleteTimesheetByIds(ids));
    }

    /**
     * 新增工时记录
     */
    @RequiresPermissions("system:work:timesheet:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("assignments", workAssignmentService.selectAssignmentList(new com.ruoyi.system.work.domain.WorkAssignment()));
        return prefix + "/add";
    }

    /**
     * 新增保存工时记录
     */
    @RequiresPermissions("system:work:timesheet:add")
    @Log(title = "工时记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkTimesheet workTimesheet)
    {
        workTimesheet.setStatus("0"); // 草稿
        return toAjax(workTimesheetService.insertTimesheet(workTimesheet));
    }

    /**
     * 修改工时记录
     */
    @RequiresPermissions("system:work:timesheet:edit")
    @GetMapping("/edit/{timesheetId}")
    public String edit(@PathVariable("timesheetId") Long timesheetId, ModelMap mmap)
    {
        mmap.put("timesheet", workTimesheetService.selectTimesheetById(timesheetId));
        mmap.put("assignments", workAssignmentService.selectAssignmentList(new com.ruoyi.system.work.domain.WorkAssignment()));
        return prefix + "/edit";
    }

    /**
     * 修改保存工时记录
     */
    @RequiresPermissions("system:work:timesheet:edit")
    @Log(title = "工时记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WorkTimesheet workTimesheet)
    {
        return toAjax(workTimesheetService.updateTimesheet(workTimesheet));
    }

    /**
     * 查看工时记录详情
     */
    @RequiresPermissions("system:work:timesheet:query")
    @GetMapping("/detail/{timesheetId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable("timesheetId") Long timesheetId)
    {
        WorkTimesheet timesheet = workTimesheetService.selectTimesheetById(timesheetId);
        return success(timesheet);
    }

    /**
     * 提交工时记录
     */
    @RequiresPermissions("system:work:timesheet:edit")
    @Log(title = "工时记录", businessType = BusinessType.UPDATE)
    @PostMapping("/submit/{timesheetId}")
    @ResponseBody
    public AjaxResult submit(@PathVariable("timesheetId") Long timesheetId)
    {
        return toAjax(workTimesheetService.submitTimesheet(timesheetId, getUsername()));
    }

    /**
     * 审核工时记录（通过）
     */
    @RequiresPermissions("system:work:timesheet:edit")
    @Log(title = "工时记录", businessType = BusinessType.UPDATE)
    @PostMapping("/approve/{timesheetId}")
    @ResponseBody
    public AjaxResult approve(@PathVariable("timesheetId") Long timesheetId,
                               @RequestParam(required = false) String auditComment)
    {
        return toAjax(workTimesheetService.auditTimesheet(timesheetId, "2", auditComment, getUsername()));
    }

    /**
     * 审核工时记录（驳回）
     */
    @RequiresPermissions("system:work:timesheet:edit")
    @Log(title = "工时记录", businessType = BusinessType.UPDATE)
    @PostMapping("/reject/{timesheetId}")
    @ResponseBody
    public AjaxResult reject(@PathVariable("timesheetId") Long timesheetId,
                              @RequestParam(required = false) String auditComment)
    {
        return toAjax(workTimesheetService.auditTimesheet(timesheetId, "3", auditComment, getUsername()));
    }

    /**
     * 统计员工工时
     */
    @RequiresPermissions("system:work:timesheet:query")
    @GetMapping("/summary/{employeeId}")
    @ResponseBody
    public AjaxResult summary(@PathVariable("employeeId") Long employeeId,
                               @RequestParam Date startDate,
                               @RequestParam Date endDate)
    {
        Double totalWorkload = workTimesheetService.sumWorkloadByDateRange(employeeId, startDate, endDate);
        AjaxResult result = AjaxResult.success();
        result.put("employeeId", employeeId);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("totalWorkload", totalWorkload);
        return result;
    }
}