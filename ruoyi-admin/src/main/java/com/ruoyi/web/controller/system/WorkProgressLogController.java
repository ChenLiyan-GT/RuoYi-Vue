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
import com.ruoyi.system.work.domain.WorkProgressLog;
import com.ruoyi.system.work.service.IWorkProgressLogService;

/**
 * 进度更新记录操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/progressLog")
public class WorkProgressLogController extends BaseController
{
    private String prefix = "system/work/progressLog";

    @Autowired
    private IWorkProgressLogService workProgressLogService;

    @RequiresPermissions("system:work:progressLog:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/progressLog";
    }

    @RequiresPermissions("system:work:progressLog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkProgressLog workProgressLog)
    {
        List<WorkProgressLog> list = workProgressLogService.selectProgressLogList(workProgressLog);
        return getDataTable(list);
    }

    @RequiresPermissions("system:work:progressLog:list")
    @GetMapping("/list/byJobStage/{jobStageId}")
    @ResponseBody
    public AjaxResult listByJobStage(@PathVariable("jobStageId") Long jobStageId)
    {
        List<WorkProgressLog> list = workProgressLogService.selectProgressLogsByJobStageId(jobStageId);
        return success(list);
    }

    @RequiresPermissions("system:work:progressLog:list")
    @GetMapping("/list/byEmployee/{employeeId}")
    @ResponseBody
    public AjaxResult listByEmployee(@PathVariable("employeeId") Long employeeId)
    {
        List<WorkProgressLog> list = workProgressLogService.selectProgressLogsByEmployeeId(employeeId);
        return success(list);
    }

    @RequiresPermissions("system:work:progressLog:remove")
    @Log(title = "进度更新记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workProgressLogService.deleteProgressLogByIds(ids));
    }

    /**
     * 新增进度更新记录
     */
    @RequiresPermissions("system:work:progressLog:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        return prefix + "/add";
    }

    /**
     * 新增保存进度更新记录
     */
    @RequiresPermissions("system:work:progressLog:add")
    @Log(title = "进度更新记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkProgressLog workProgressLog)
    {
        return toAjax(workProgressLogService.insertProgressLog(workProgressLog));
    }

    /**
     * 查看进度更新记录详情
     */
    @RequiresPermissions("system:work:progressLog:query")
    @GetMapping("/detail/{logId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable("logId") Long logId)
    {
        WorkProgressLog progressLog = workProgressLogService.selectProgressLogById(logId);
        return success(progressLog);
    }

    /**
     * 记录进度更新
     */
    @RequiresPermissions("system:work:progressLog:add")
    @Log(title = "进度更新记录", businessType = BusinessType.INSERT)
    @PostMapping("/record")
    @ResponseBody
    public AjaxResult record(@RequestParam Long jobStageId,
                              @RequestParam Long employeeId,
                              @RequestParam Double progress,
                              @RequestParam(required = false) Double workload,
                              @RequestParam String content,
                              @RequestParam(required = false) String remark)
    {
        return toAjax(workProgressLogService.recordProgress(jobStageId, employeeId, progress, 
                                                             workload, content, remark, getLoginName()));
    }

    /**
     * 查询作业阶段的最新进度
     */
    @RequiresPermissions("system:work:progressLog:query")
    @GetMapping("/latest/{jobStageId}")
    @ResponseBody
    public AjaxResult latest(@PathVariable("jobStageId") Long jobStageId)
    {
        WorkProgressLog latestLog = workProgressLogService.selectLatestProgress(jobStageId);
        return success(latestLog);
    }
}