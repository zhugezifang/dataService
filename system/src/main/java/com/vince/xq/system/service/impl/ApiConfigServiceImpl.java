package com.vince.xq.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.vince.xq.common.constant.GenConstants;
import com.vince.xq.common.core.domain.AjaxResult;
import com.vince.xq.common.core.text.Convert;
import com.vince.xq.common.enums.DbTypeEnum;
import com.vince.xq.common.utils.RunApi;
import com.vince.xq.common.utils.StringUtils;
import com.vince.xq.system.domain.ApiConfig;
import com.vince.xq.system.domain.ApiParam;
import com.vince.xq.system.domain.DbConfig;
import com.vince.xq.system.mapper.ApiConfigMapper;
import com.vince.xq.system.mapper.DbConfigMapper;
import com.vince.xq.system.service.IApiConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiConfigServiceImpl implements IApiConfigService {

    private static final Logger log = LoggerFactory.getLogger(ApiConfigServiceImpl.class);

    @Autowired
    private ApiConfigMapper apiConfigMapper;

    @Autowired
    private DbConfigMapper dbConfigMapper;

    @Override
    public List<ApiConfig> selectApiConfigList(ApiConfig apiConfig) {
        return apiConfigMapper.selectApiConfigList(apiConfig);
    }

    @Override
    public List<ApiConfig> selectApiConfigAll() {
        return apiConfigMapper.selectApiConfigAll();
    }

    @Override
    public ApiConfig selectApiConfigById(Long id) {
        ApiConfig apiConfig = apiConfigMapper.selectApiConfigById(id);
        apiConfig.setParam(JSONObject.parseArray(apiConfig.getParams(), ApiParam.class));
        return apiConfig;
    }

    @Override
    public int deleteApiConfigByIds(String ids) {
        Long[] idArr = Convert.toLongArray(ids);
        return apiConfigMapper.deleteApiConfigByIds(idArr);
    }

    @Override
    public int insertApiConfig(ApiConfig apiConfig) {
        return apiConfigMapper.insertApiConfig(apiConfig);
    }

    @Override
    public int updateApiConfig(ApiConfig apiConfig) {
        return 0;
    }

    @Override
    public int countUserPostById(Long postId) {
        return 0;
    }

    @Override
    public String checkNameUnique(ApiConfig apiConfig) {
        ApiConfig info = apiConfigMapper.checkApiNameUnique(apiConfig.getApiName());
        if (StringUtils.isNotNull(info)) {
            return GenConstants.DBCONFIG_NAME_NOT_UNIQUE;
        }
        return GenConstants.DBCONFIG_NAME_UNIQUE;
    }

    @Override
    public int updateApiConfigOnline(ApiConfig apiConfig) {
        return apiConfigMapper.updateApiConfigOnline(apiConfig);
    }


    @Override
    public ApiConfig selectApiConfigByApiName(String apiName) {
        ApiConfig apiConfig = apiConfigMapper.selectApiConfigByApiName(apiName);
        apiConfig.setParam(JSONObject.parseArray(apiConfig.getParams(), ApiParam.class));
        return apiConfig;
    }

    @Override
    public AjaxResult.Response runApi(String apiName, List<ApiParam> apiParamList) throws Exception {
        log.info("=========runApi apiName:{},apiParamList:{}=============", apiName, JSONObject.toJSONString(apiParamList));
        Map<String, String> paramsMap = new HashMap<>();
        for (ApiParam apiParam : apiParamList) {
            paramsMap.put(apiParam.getName(), apiParam.getValue());
        }
        ApiConfig apiConfig = selectApiConfigByApiName(apiName);
        DbConfig dbConfig = dbConfigMapper.selectDbConfigById(apiConfig.getDbConfigId());
        DbTypeEnum dbTypeEnum = DbTypeEnum.findEnumByType(dbConfig.getType());
        String url = dbConfig.getUrl();
        String userName = dbConfig.getUserName();
        String pwd = dbConfig.getPwd();
        String sql = apiConfig.getApiSql();

        for (ApiParam apiParam : apiConfig.getParam()) {
            String type = apiParam.getType();
            if (type.equals("String")) {
                sql = sql.replace("{" + apiParam.getName() + "}", "'" + paramsMap.get(apiParam.getName()) + "'");
            } else if (type.equals("Bigint")) {
                sql = sql.replace("{" + apiParam.getName() + "}", paramsMap.get(apiParam.getName()));
            }
        }
        sql = sql.replaceAll("\\$", "");
        log.info("=====sql:{}===========", sql);
        List<LinkedHashMap<String, String>> list = RunApi.runSql(dbTypeEnum, url, userName, pwd, sql);
        AjaxResult.Response response = new AjaxResult.Response(AjaxResult.Type.SUCCESS, "success", list);

        return response;
    }
}
