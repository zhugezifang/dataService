package com.vince.xq.web.controller.system;

import com.vince.xq.common.annotation.Log;
import com.vince.xq.common.constant.UserConstants;
import com.vince.xq.common.core.controller.BaseController;
import com.vince.xq.common.core.domain.AjaxResult;
import com.vince.xq.common.core.page.TableDataInfo;
import com.vince.xq.common.enums.BusinessType;
import com.vince.xq.common.utils.poi.ExcelUtil;
import com.vince.xq.system.domain.SysPost;
import com.vince.xq.system.service.ISysPostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/test")
public class SysTestController extends BaseController
{
    private String prefix = "system/test";

    @Autowired
    private ISysPostService postService;

    @RequiresPermissions("system:test:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/test";
    }

    @RequiresPermissions("system:test:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysPost test)
    {
        startPage();
        List<SysPost> list = postService.selectPostList(test);
        return getDataTable(list);
    }

    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:test:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysPost test)
    {
        List<SysPost> list = postService.selectPostList(test);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    @RequiresPermissions("system:test:remove")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(postService.deletePostByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 新增岗位
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存岗位
     */
    @RequiresPermissions("system:test:add")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysPost test)
    {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(test)))
        {
            return error("新增岗位'" + test.getPostName() + "'失败，岗位名称已存在");
        }
        else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(test)))
        {
            return error("新增岗位'" + test.getPostName() + "'失败，岗位编码已存在");
        }
        test.setCreateBy(getLoginName());
        return toAjax(postService.insertPost(test));
    }

    /**
     * 修改岗位
     */
    @RequiresPermissions("system:test:edit")
    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable("postId") Long postId, ModelMap mmap)
    {
        mmap.put("test", postService.selectPostById(postId));
        return prefix + "/edit";
    }

    /**
     * 修改保存岗位
     */
    @RequiresPermissions("system:test:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysPost test)
    {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(test)))
        {
            return error("修改岗位'" + test.getPostName() + "'失败，岗位名称已存在");
        }
        else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(test)))
        {
            return error("修改岗位'" + test.getPostName() + "'失败，岗位编码已存在");
        }
        test.setUpdateBy(getLoginName());
        return toAjax(postService.updatePost(test));
    }

    /**
     * 校验岗位名称
     */
    @PostMapping("/checkPostNameUnique")
    @ResponseBody
    public String checkPostNameUnique(SysPost test)
    {
        return postService.checkPostNameUnique(test);
    }

    /**
     * 校验岗位编码
     */
    @PostMapping("/checkPostCodeUnique")
    @ResponseBody
    public String checkPostCodeUnique(SysPost test)
    {
        return postService.checkPostCodeUnique(test);
    }
}
