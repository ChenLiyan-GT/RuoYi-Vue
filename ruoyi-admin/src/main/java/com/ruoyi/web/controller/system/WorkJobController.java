package com.ruoyi.web.controller.system;

import java.math.BigDecimal;
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
import com.ruoyi.system.work.domain.WorkJob;
import com.ruoyi.system.work.service.IWorkJobService;
import com.ruoyi.system.work.service.IWorkStageTemplateService;

/**
 * 作业管理操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/job")
public class WorkJobController extends BaseController
{
    private String prefix = "system/work/job";

    @Autowired
    private IWorkJobService workJobService;

    @Autowired
    private IWorkStageTemplateService stageTemplateService;

    @RequiresPermissions("system:work:job:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/job";
    }

    @RequiresPermissions("system:work:job:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkJob workJob)
    {
        List<WorkJob> list = workJobService.selectJobList(workJob);
        return getDataTable(list);
    }

    @RequiresPermissions("system:work:job:list")
    @GetMapping("/list/all")
    @ResponseBody
    public AjaxResult listAll()
    {
        List<WorkJob> list = workJobService.selectJobAll();
        return success(list);
    }

    @RequiresPermissions("system:work:job:list")
    @GetMapping("/list/byDept/{deptId}")
    @ResponseBody
    public AjaxResult listByDept(@PathVariable("deptId") Long deptId)
    {
        List<WorkJob> list = workJobService.selectJobsByDeptId(deptId);
        return success(list);
    }

    @RequiresPermissions("system:work:job:remove")
    @Log(title = "作业管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workJobService.deleteJobByIds(ids));
    }

    /**
     * 新增作业
     */
    @RequiresPermissions("system:work:job:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("templates", stageTemplateService.selectStageTemplateAll());
        return prefix + "/add";
    }

    /**
     * 新增保存作业
     */
    @RequiresPermissions("system:work:job:add")
    @Log(title = "作业管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkJob workJob)
    {
        if (!workJobService.checkJobNoUnique(workJob))
        {
            return error("新增作业'" + workJob.getJobName() + "'失败，作业编号已存在");
        }
        return toAjax(workJobService.insertJob(workJob));
    }

    /**
     * 修改作业
     */
    @RequiresPermissions("system:work:job:edit")
    @GetMapping("/edit/{jobId}")
    public String edit(@PathVariable("jobId") Long jobId, ModelMap mmap)
    {
        mmap.put("workJob", workJobService.selectJobById(jobId));
        mmap.put("templates", stageTemplateService.selectStageTemplateAll());
        return prefix + "/edit";
    }

    /**
     * 修改保存作业
     */
    @RequiresPermissions("system:work:job:edit")
    @Log(title = "作业管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WorkJob workJob)
    {
        if (!workJobService.checkJobNoUnique(workJob))
        {
            return error("修改作业'" + workJob.getJobName() + "'失败，作业编号已存在");
        }
        return toAjax(workJobService.updateJob(workJob));
    }

    /**
     * 查看作业详情
     */
    @RequiresPermissions("system:work:job:query")
    @GetMapping("/detail/{jobId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable("jobId") Long jobId)
    {
        WorkJob workJob = workJobService.selectJobById(jobId);
        return success(workJob);
    }

    /**
     * 更新作业进度
     */
    @RequiresPermissions("system:work:job:edit")
    @Log(title = "作业管理", businessType = BusinessType.UPDATE)
    @PostMapping("/updateProgress")
    @ResponseBody
    public AjaxResult updateProgress(@RequestParam Long jobId, @RequestParam BigDecimal progress)
    {
        if (progress.compareTo(BigDecimal.ZERO) < 0 || progress.compareTo(new BigDecimal("100")) > 0)
        {
            return error("进度必须在 0-100 之间");
        }
        return toAjax(workJobService.updateJobProgress(jobId, progress));
    }

    /**
     * 更新作业状态
     */
    @RequiresPermissions("system:work:job:edit")
    @Log(title = "作业管理", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    @ResponseBody
    public AjaxResult updateStatus(@RequestParam Long jobId, @RequestParam String status)
    {
        return toAjax(workJobService.updateJobStatus(jobId, status));
    }

    /**
     * 校验作业编号
     */
    @PostMapping("/checkJobNoUnique")
    @ResponseBody
    public boolean checkJobNoUnique(WorkJob workJob)
    {
        return workJobService.checkJobNoUnique(workJob);
    }

    /**
     * 生成作业编号
     */
    @RequiresPermissions("system:work:job:add")
    @GetMapping("/generateJobNo/{deptId}")
    @ResponseBody
    public AjaxResult generateJobNo(@PathVariable("deptId") Long deptId)
    {
        String jobNo = workJobService.generateJobNo(deptId);
        return success(jobNo);
    }
}