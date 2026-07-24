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
import com.ruoyi.system.work.domain.WorkStageTemplate;
import com.ruoyi.system.work.service.IWorkStageTemplateService;

/**
 * 作业阶段模板操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/stageTemplate")
public class WorkStageTemplateController extends BaseController
{
    private String prefix = "system/work/stageTemplate";

    @Autowired
    private IWorkStageTemplateService stageTemplateService;

    @RequiresPermissions("system:work:stageTemplate:view")
    @GetMapping()
    public String stageTemplate()
    {
        return prefix + "/stageTemplate";
    }

    @RequiresPermissions("system:work:stageTemplate:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkStageTemplate workStageTemplate)
    {
        List<WorkStageTemplate> list = stageTemplateService.selectStageTemplateList(workStageTemplate);
        return getDataTable(list);
    }

    @RequiresPermissions("system:work:stageTemplate:remove")
    @Log(title = "作业阶段模板", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(stageTemplateService.deleteStageTemplateByIds(ids));
    }

    /**
     * 新增作业阶段模板
     */
    @RequiresPermissions("system:work:stageTemplate:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存作业阶段模板
     */
    @RequiresPermissions("system:work:stageTemplate:add")
    @Log(title = "作业阶段模板", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkStageTemplate workStageTemplate)
    {
        if (!stageTemplateService.checkTemplateNameUnique(workStageTemplate))
        {
            return error("新增模板'" + workStageTemplate.getTemplateName() + "'失败，模板名称已存在");
        }
        return toAjax(stageTemplateService.insertStageTemplate(workStageTemplate));
    }

    /**
     * 修改作业阶段模板
     */
    @RequiresPermissions("system:work:stageTemplate:edit")
    @GetMapping("/edit/{templateId}")
    public String edit(@PathVariable("templateId") Long templateId, ModelMap mmap)
    {
        mmap.put("stageTemplate", stageTemplateService.selectStageTemplateById(templateId));
        return prefix + "/edit";
    }

    /**
     * 修改保存作业阶段模板
     */
    @RequiresPermissions("system:work:stageTemplate:edit")
    @Log(title = "作业阶段模板", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WorkStageTemplate workStageTemplate)
    {
        if (!stageTemplateService.checkTemplateNameUnique(workStageTemplate))
        {
            return error("修改模板'" + workStageTemplate.getTemplateName() + "'失败，模板名称已存在");
        }
        return toAjax(stageTemplateService.updateStageTemplate(workStageTemplate));
    }

    /**
     * 复制模板
     */
    @RequiresPermissions("system:work:stageTemplate:add")
    @Log(title = "作业阶段模板", businessType = BusinessType.INSERT)
    @PostMapping("/copy/{templateId}")
    @ResponseBody
    public AjaxResult copy(@PathVariable("templateId") Long templateId)
    {
        return toAjax(stageTemplateService.copyTemplate(templateId));
    }

    /**
     * 设为默认模板
     */
    @RequiresPermissions("system:work:stageTemplate:edit")
    @Log(title = "作业阶段模板", businessType = BusinessType.UPDATE)
    @PostMapping("/setDefault/{templateId}")
    @ResponseBody
    public AjaxResult setDefault(@PathVariable("templateId") Long templateId)
    {
        return toAjax(stageTemplateService.setDefaultTemplate(templateId));
    }

    /**
     * 查询所有模板列表
     */
    @RequiresPermissions("system:work:stageTemplate:list")
    @GetMapping("/list/all")
    @ResponseBody
    public AjaxResult listAll()
    {
        List<WorkStageTemplate> list = stageTemplateService.selectStageTemplateAll();
        return success(list);
    }

    /**
     * 根据部门 ID 查询模板列表
     */
    @RequiresPermissions("system:work:stageTemplate:list")
    @GetMapping("/list/byDept/{deptId}")
    @ResponseBody
    public AjaxResult listByDept(@PathVariable("deptId") Long deptId)
    {
        List<WorkStageTemplate> list = stageTemplateService.selectTemplatesByDeptId(deptId);
        return success(list);
    }

    /**
     * 查询默认模板
     */
    @RequiresPermissions("system:work:stageTemplate:list")
    @GetMapping("/default")
    @ResponseBody
    public AjaxResult getDefault()
    {
        WorkStageTemplate template = stageTemplateService.selectDefaultTemplate();
        return success(template);
    }

    /**
     * 校验模板名称
     */
    @PostMapping("/checkTemplateNameUnique")
    @ResponseBody
    public boolean checkTemplateNameUnique(WorkStageTemplate workStageTemplate)
    {
        return stageTemplateService.checkTemplateNameUnique(workStageTemplate);
    }
}
