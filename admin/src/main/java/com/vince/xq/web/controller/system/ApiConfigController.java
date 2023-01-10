package com.vince.xq.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.vince.xq.common.annotation.Log;
import com.vince.xq.common.constant.GenConstants;
import com.vince.xq.common.core.controller.BaseController;
import com.vince.xq.common.core.domain.AjaxResult;
import com.vince.xq.common.core.page.TableDataInfo;
import com.vince.xq.common.enums.BusinessType;
import com.vince.xq.common.utils.poi.ExcelUtil;
import com.vince.xq.system.domain.ApiConfig;
import com.vince.xq.system.domain.ApiParam;
import com.vince.xq.system.domain.DbConfig;
import com.vince.xq.system.service.IApiConfigService;
import com.vince.xq.system.service.IDbConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/apiConfig")
public class ApiConfigController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ApiConfigController.class);

    private String prefix = "system/apiConfig";

    @Autowired
    private IApiConfigService apiConfigService;

    @Autowired
    private IDbConfigService dbConfigService;

    @RequiresPermissions("system:apiConfig:view")
    @GetMapping()
    public String operlog() {
        return prefix + "/apiConfig";
    }

    @RequiresPermissions("system:apiConfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ApiConfig apiConfig) {
        startPage();
        List<ApiConfig> list = apiConfigService.selectApiConfigList(apiConfig);
        return getDataTable(list);
    }

    @Log(title = "Api管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:apiConfig:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ApiConfig apiConfig) {
        List<ApiConfig> list = apiConfigService.selectApiConfigList(apiConfig);
        ExcelUtil<ApiConfig> util = new ExcelUtil<ApiConfig>(ApiConfig.class);
        return util.exportExcel(list, "api管理");
    }

    @RequiresPermissions("system:apiConfig:remove")
    @Log(title = "Api管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(apiConfigService.deleteApiConfigByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 新增api
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        List<DbConfig> dbConfigList = dbConfigService.selectDbConfigAll();
        mmap.put("dbList", dbConfigList);
        return prefix + "/add";
    }

    /**
     * 新增api
     */
    @RequiresPermissions("system:apiConfig:add")
    @Log(title = "add", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated ApiConfig apiConfig) {
        logger.info("apiConfig:" + JSONObject.toJSONString(apiConfig));
        if (GenConstants.APICONFIG_NAME_NOT_UNIQUE.equals(apiConfigService.checkNameUnique(apiConfig))) {
            return error("新增api名称'" + apiConfig.getApiName() + "'失败，名称已存在");
        }
        apiConfig.setCreateBy(getLoginName());
        return toAjax(apiConfigService.insertApiConfig(apiConfig));
    }

    //@RequiresPermissions("system:apiConfig:updateOnline")
    @Log(title = "updateOnline", businessType = BusinessType.UPDATE)
    @PostMapping("/updateOnline")
    @ResponseBody
    public AjaxResult onlineOrOffline(@Validated ApiConfig apiConfig) {
        logger.info("apiConfig:" + JSONObject.toJSONString(apiConfig));
        return toAjax(apiConfigService.updateApiConfigOnline(apiConfig));
    }


    /**
     * 修改api
     */
    @RequiresPermissions("system:apiConfig:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        ApiConfig apiConfig = apiConfigService.selectApiConfigById(id);
        mmap.put("apiConfig", apiConfig);
        return prefix + "/edit";
    }

    /**
     * 修改api
     */
    @RequiresPermissions("system:apiConfig:edit")
    @Log(title = "Api管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated ApiConfig apiConfig) {
        if (GenConstants.DBCONFIG_NAME_NOT_UNIQUE.equals(apiConfigService.checkNameUnique(apiConfig))) {
            return error("修改apiName 名称'" + apiConfig.getApiName() + "'失败，名称已存在");
        }
        return toAjax(apiConfigService.updateApiConfig(apiConfig));
    }

    //@RequiresPermissions("system:apiConfig:test")
    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") Long id, ModelMap mmap) {
        ApiConfig apiConfig = apiConfigService.selectApiConfigById(id);
        mmap.put("apiConfig", apiConfig);
        /*logger.info("params:{}", JSONObject.toJSONString(apiConfig.getParam()));
        mmap.put("params", apiConfig.getParam());*/
        return prefix + "/test";
    }

    @RequestMapping(value = "/testCase", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult testCase(String apiName, String params) {
        try {
            logger.info("apiName:{},params:{}", apiName, params);
            List<ApiParam> apiParamList = JSONObject.parseArray(params, ApiParam.class);
            AjaxResult.Response response = apiConfigService.runApi(apiName, apiParamList);
            return success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return error("错误信息:" + e.getMessage());
        }
    }

    @PostMapping("/checkNameUnique")
    @ResponseBody
    public String checkNameUnique(ApiConfig apiConfig) {
        return apiConfigService.checkNameUnique(apiConfig);
    }

}
