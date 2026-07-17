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
import com.ruoyi.system.work.domain.WorkJobStage;
import com.ruoyi.system.work.service.IWorkJobStageService;
import com.ruoyi.system.work.service.IWorkJobService;

/**
 * 作业阶段操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/jobStage")
public class WorkJobStageController extends BaseController
{
    private String prefix = "system/work/jobStage";

    @Autowired
    private IWorkJobStageService jobStageService;

    @Autowired
    private IWorkJobService workJobService;

    @RequiresPermissions("system:work:jobStage:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/jobStage";
    }

    @RequiresPermissions("system:work:jobStage:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkJobStage workJobStage)
    {
        List<WorkJobStage> list = jobStageService.selectJobStageList(workJobStage);
        return getDataTable(list);
    }

    @RequiresPermissions("system:work:jobStage:list")
    @GetMapping("/list/byJob/{jobId}")
    @ResponseBody
    public AjaxResult listByJob(@PathVariable("jobId") Long jobId)
    {
        List<WorkJobStage> list = jobStageService.selectJobStagesByJobId(jobId);
        return success(list);
    }

    @RequiresPermissions("system:work:jobStage:list")
    @GetMapping("/list/toAssign")
    @ResponseBody
    public AjaxResult listToAssign(@RequestParam(defaultValue = "0") String status)
    {
        List<WorkJobStage> list = jobStageService.selectStagesToAssign(status);
        return success(list);
    }

    @RequiresPermissions("system:work:jobStage:remove")
    @Log(title = "作业阶段", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(jobStageService.deleteJobStageByIds(ids));
    }

    /**
     * 新增作业阶段
     */
    @RequiresPermissions("system:work:jobStage:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("jobs", workJobService.selectJobAll());
        return prefix + "/add";
    }

    /**
     * 新增保存作业阶段
     */
    @RequiresPermissions("system:work:jobStage:add")
    @Log(title = "作业阶段", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkJobStage workJobStage)
    {
        return toAjax(jobStageService.insertJobStage(workJobStage));
    }

    /**
     * 修改作业阶段
     */
    @RequiresPermissions("system:work:jobStage:edit")
    @GetMapping("/edit/{jobStageId}")
    public String edit(@PathVariable("jobStageId") Long jobStageId, ModelMap mmap)
    {
        mmap.put("jobStage", jobStageService.selectJobStageById(jobStageId));
        mmap.put("jobs", workJobService.selectJobAll());
        return prefix + "/edit";
    }

    /**
     * 修改保存作业阶段
     */
    @RequiresPermissions("system:work:jobStage:edit")
    @Log(title = "作业阶段", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WorkJobStage workJobStage)
    {
        return toAjax(jobStageService.updateJobStage(workJobStage));
    }

    /**
     * 查看作业阶段详情
     */
    @RequiresPermissions("system:work:jobStage:query")
    @GetMapping("/detail/{jobStageId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable("jobStageId") Long jobStageId)
    {
        WorkJobStage jobStage = jobStageService.selectJobStageById(jobStageId);
        return success(jobStage);
    }

    /**
     * 更新作业阶段状态
     */
    @RequiresPermissions("system:work:jobStage:edit")
    @Log(title = "作业阶段", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    @ResponseBody
    public AjaxResult updateStatus(@RequestParam Long jobStageId, @RequestParam String status)
    {
        return toAjax(jobStageService.updateJobStageStatus(jobStageId, status));
    }

    /**
     * 检查阶段是否可以开始
     */
    @RequiresPermissions("system:work:jobStage:query")
    @GetMapping("/canStart/{jobStageId}")
    @ResponseBody
    public AjaxResult canStart(@PathVariable("jobStageId") Long jobStageId)
    {
        boolean canStart = jobStageService.canStartStage(jobStageId);
        return success(canStart);
    }

    /**
     * 开始作业阶段
     */
    @RequiresPermissions("system:work:jobStage:edit")
    @Log(title = "作业阶段", businessType = BusinessType.UPDATE)
    @PostMapping("/start/{jobStageId}")
    @ResponseBody
    public AjaxResult startStage(@PathVariable("jobStageId") Long jobStageId)
    {
        if (!jobStageService.canStartStage(jobStageId))
        {
            return error("前置阶段未完成，无法开始此阶段");
        }
        WorkJobStage stage = jobStageService.selectJobStageById(jobStageId);
        stage.setStatus("1"); // 进行中
        return toAjax(jobStageService.updateJobStage(stage));
    }

    /**
     * 完成作业阶段
     */
    @RequiresPermissions("system:work:jobStage:edit")
    @Log(title = "作业阶段", businessType = BusinessType.UPDATE)
    @PostMapping("/complete/{jobStageId}")
    @ResponseBody
    public AjaxResult completeStage(@PathVariable("jobStageId") Long jobStageId)
    {
        WorkJobStage stage = jobStageService.selectJobStageById(jobStageId);
        stage.setStatus("2"); // 已完成
        return toAjax(jobStageService.updateJobStage(stage));
    }
}