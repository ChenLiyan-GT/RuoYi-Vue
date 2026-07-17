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
import com.ruoyi.system.work.domain.WorkLevelRule;
import com.ruoyi.system.work.service.IWorkLevelRuleService;

/**
 * 级别规则操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work/levelRule")
public class WorkLevelRuleController extends BaseController
{
    private String prefix = "system/work/levelRule";

    @Autowired
    private IWorkLevelRuleService levelRuleService;

    @RequiresPermissions("system:work:levelRule:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/levelRule";
    }

    @RequiresPermissions("system:work:levelRule:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkLevelRule workLevelRule)
    {
        List<WorkLevelRule> list = levelRuleService.selectLevelRuleList(workLevelRule);
        return getDataTable(list);
    }

    @RequiresPermissions("system:work:levelRule:remove")
    @Log(title = "级别规则", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(levelRuleService.deleteLevelRuleByIds(ids));
    }

    /**
     * 新增级别规则
     */
    @RequiresPermissions("system:work:levelRule:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存级别规则
     */
    @RequiresPermissions("system:work:levelRule:add")
    @Log(title = "级别规则", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WorkLevelRule workLevelRule)
    {
        if (!levelRuleService.checkLevelUnique(workLevelRule))
        {
            return error("新增级别规则失败，职级 P" + workLevelRule.getLevel() + "已存在");
        }
        return toAjax(levelRuleService.insertLevelRule(workLevelRule));
    }

    /**
     * 修改级别规则
     */
    @RequiresPermissions("system:work:levelRule:edit")
    @GetMapping("/edit/{ruleId}")
    public String edit(@PathVariable("ruleId") Long ruleId, ModelMap mmap)
    {
        mmap.put("levelRule", levelRuleService.selectLevelRuleById(ruleId));
        return prefix + "/edit";
    }

    /**
     * 修改保存级别规则
     */
    @RequiresPermissions("system:work:levelRule:edit")
    @Log(title = "级别规则", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WorkLevelRule workLevelRule)
    {
        if (!levelRuleService.checkLevelUnique(workLevelRule))
        {
            return error("修改级别规则失败，职级 P" + workLevelRule.getLevel() + "已存在");
        }
        return toAjax(levelRuleService.updateLevelRule(workLevelRule));
    }

    /**
     * 查询所有级别规则列表
     */
    @RequiresPermissions("system:work:levelRule:list")
    @GetMapping("/list/all")
    @ResponseBody
    public AjaxResult listAll()
    {
        List<WorkLevelRule> list = levelRuleService.selectLevelRuleAll();
        return success(list);
    }

    /**
     * 根据职级获取可执行阶段范围
     */
    @RequiresPermissions("system:work:levelRule:list")
    @GetMapping("/stageRange/{level}")
    @ResponseBody
    public AjaxResult getStageRange(@PathVariable("level") Integer level)
    {
        String stageRange = levelRuleService.getStageRangeByLevel(level);
        return success(stageRange);
    }

    /**
     * 校验职级
     */
    @PostMapping("/checkLevelUnique")
    @ResponseBody
    public boolean checkLevelUnique(WorkLevelRule workLevelRule)
    {
        return levelRuleService.checkLevelUnique(workLevelRule);
    }
}