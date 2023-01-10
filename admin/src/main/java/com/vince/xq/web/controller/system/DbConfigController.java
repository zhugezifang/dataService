package com.vince.xq.web.controller.system;

import com.vince.xq.common.annotation.Log;
import com.vince.xq.common.constant.GenConstants;
import com.vince.xq.common.constant.UserConstants;
import com.vince.xq.common.core.domain.AjaxResult;
import com.vince.xq.common.core.page.TableDataInfo;
import com.vince.xq.common.enums.BusinessType;
import com.vince.xq.common.utils.poi.ExcelUtil;
import com.vince.xq.common.core.controller.BaseController;
import com.vince.xq.system.domain.DbConfig;
import com.vince.xq.system.service.IDbConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/system/dbConfig")
public class DbConfigController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DbConfigController.class);

    private String prefix = "system/dbConfig";

    @Autowired
    private IDbConfigService dbConfigService;

    @RequiresPermissions("system:dbConfig:view")
    @GetMapping()
    public String operlog() {
        return prefix + "/dbConfig";
    }

    @RequiresPermissions("system:dbConfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DbConfig dbConfig) {
        startPage();
        List<DbConfig> list = dbConfigService.selectDbConfigList(dbConfig);
        return getDataTable(list);
    }

    @Log(title = "数据源管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dbConfig:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DbConfig dbConfig) {
        List<DbConfig> list = dbConfigService.selectDbConfigList(dbConfig);
        ExcelUtil<DbConfig> util = new ExcelUtil<DbConfig>(DbConfig.class);
        return util.exportExcel(list, "岗位数据");
    }

    @RequiresPermissions("system:dbConfig:remove")
    @Log(title = "数据源管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(dbConfigService.deleteDbConfigByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 新增数据源
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("dbtypes", dbConfigService.selectDbTypesAll());
        return prefix + "/add";
    }

    /**
     * 新增数据源
     */
    @RequiresPermissions("system:dbConfig:add")
    @Log(title = "add", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated DbConfig dbConfig) {
        if (GenConstants.DBCONFIG_NAME_NOT_UNIQUE.equals(dbConfigService.checkConnectNameUnique(dbConfig))) {
            return error("新增数据库连接名称'" + dbConfig.getConnectName() + "'失败，数据库连接名称已存在");
        }
        dbConfig.setCreateBy(getLoginName());
        return toAjax(dbConfigService.insertDbConfig(dbConfig));
    }

    /**
     * 修改数据源
     */
    @RequiresPermissions("system:dbConfig:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("dbtypes", dbConfigService.selectDbTypesAll());
        DbConfig dbConfig = dbConfigService.selectDbConfigById(id);
        mmap.put("dbConfig", dbConfig);
        return prefix + "/edit";
    }

    /**
     * 修改数据源
     */
    @RequiresPermissions("system:dbConfig:edit")
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated DbConfig dbConfig) {
        if (GenConstants.DBCONFIG_NAME_NOT_UNIQUE.equals(dbConfigService.checkConnectNameUnique(dbConfig))) {
            return error("修改数据库连接名称'" + dbConfig.getConnectName() + "'失败，数据库连接名称已存在");
        }
        return toAjax(dbConfigService.updateDbConfig(dbConfig));
    }

    @PostMapping("/checkConnectNameUnique")
    @ResponseBody
    public String checkConnectNameUnique(DbConfig dbConfig) {
        return dbConfigService.checkConnectNameUnique(dbConfig);
    }


    @RequestMapping(value = "/testConnection", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult testConnection(DbConfig dbConfig) {
        try {
            dbConfigService.testConnection(dbConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return error("错误信息:" + e.getMessage());
        }
        return success("测试连接成功");
    }
}
