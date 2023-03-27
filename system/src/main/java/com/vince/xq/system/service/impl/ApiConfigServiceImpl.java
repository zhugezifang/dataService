package com.vince.xq.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.vince.xq.common.constant.GenConstants;
import com.vince.xq.common.core.domain.AjaxResult;
import com.vince.xq.common.core.text.Convert;
import com.vince.xq.common.datasource.dto.DataSourceDto;
import com.vince.xq.common.datasource.service.IDataSourceService;
import com.vince.xq.common.enums.DbTypeEnum;
import com.vince.xq.common.utils.LRUMap;
import com.vince.xq.common.utils.RunApi;
import com.vince.xq.common.utils.StringUtils;
import com.vince.xq.system.domain.ApiConfig;
import com.vince.xq.system.domain.ApiParam;
import com.vince.xq.system.domain.DbConfig;
import com.vince.xq.system.mapper.ApiConfigMapper;
import com.vince.xq.system.service.IApiConfigService;
import com.vince.xq.system.service.IDbConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ApiConfigServiceImpl implements IApiConfigService {

    private static final Logger log = LoggerFactory.getLogger(ApiConfigServiceImpl.class);

    private LRUMap<String, ApiConfig> apiConfigCache = null;
    @Autowired
    private IDataSourceService dataSourceService;
    @Autowired
    private ApiConfigMapper apiConfigMapper;

    @Autowired
    private IDbConfigService dbConfigService;

    @PostConstruct
    public void init() {
        apiConfigCache = new LRUMap<>(500);
        List<ApiConfig> apiConfigList = selectApiConfigAll();
        for (ApiConfig apiConfig : apiConfigList) {
            apiConfigCache.put(apiConfig.getApiName(), apiConfig);
        }
        log.info("apiConfigCache size:" + apiConfigCache.size());
    }

    @Override
    public List<ApiConfig> selectApiConfigList(ApiConfig apiConfig) {
        return apiConfigMapper.selectApiConfigList(apiConfig);
    }

    @Override
    public List<ApiConfig> selectApiConfigAll() {
        List<ApiConfig> apiConfigList = apiConfigMapper.selectApiConfigAll();
        for (ApiConfig apiConfig : apiConfigList) {
            apiConfig.setParam(JSONObject.parseArray(apiConfig.getParams(), ApiParam.class));
        }
        return apiConfigList;
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

        return apiConfigMapper.updateApiConfig(apiConfig);
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
        ApiConfig apiConfig = apiConfigCache.get(apiName);
        if (apiConfig == null) {
            apiConfig = apiConfigMapper.selectApiConfigByApiName(apiName);
            apiConfig.setParam(JSONObject.parseArray(apiConfig.getParams(), ApiParam.class));
        }
        return apiConfig;
    }

    @Override
    public AjaxResult.Response runApiByType(String apiName, List<ApiParam> apiParamList, String method, long startTime) throws Exception {
        log.info("=========runApiByType apiName:{},apiParamList:{},method:{}=============", apiName, JSONObject.toJSONString(apiParamList), method);
        Map<String, String> paramsMap = new HashMap<>();
        for (ApiParam apiParam : apiParamList) {
            paramsMap.put(apiParam.getName(), apiParam.getValue());
        }
        ApiConfig apiConfig = selectApiConfigByApiName(apiName);

        method = method.toLowerCase(Locale.ROOT);
        AjaxResult.Response response;
        if (method.equals(apiConfig.getRequestMode())) {
            DbConfig dbConfig = dbConfigService.selectDbConfigById(apiConfig.getDbConfigId());

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
            List<LinkedHashMap<String, Object>> list = RunApi.runSql(dbTypeEnum, url, userName, pwd, sql);
            long costTime = System.currentTimeMillis() - startTime;
            if (costTime > apiConfig.getTimeOut()) {
                log.info("=====runApiByType 接口响应超时 costTime:{},timeOut:{}===========", costTime, apiConfig.getTimeOut());
                response = new AjaxResult.Response(AjaxResult.Type.ERROR, "error", "接口请求超时");
            } else {
                response = new AjaxResult.Response(AjaxResult.Type.SUCCESS, "success", list);
            }
        } else {
            response = new AjaxResult.Response(AjaxResult.Type.ERROR, "error", "请求类型不匹配");
        }
        return response;
    }


    @Override
    public AjaxResult.Response runApiByTypePool(String apiName, List<ApiParam> apiParamList, String method, long startTime) throws Exception {
        log.info("=========runApiByType apiName:{},apiParamList:{},method:{}=============", apiName, JSONObject.toJSONString(apiParamList), method);
        Map<String, String> paramsMap = new HashMap<>();
        for (ApiParam apiParam : apiParamList) {
            paramsMap.put(apiParam.getName(), apiParam.getValue());
        }
        ApiConfig apiConfig = selectApiConfigByApiName(apiName);

        method = method.toLowerCase(Locale.ROOT);
        AjaxResult.Response response;
        if (method.equals(apiConfig.getRequestMode())) {
            DbConfig dbConfig = dbConfigService.selectDbConfigById(apiConfig.getDbConfigId());

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
            DataSourceDto dto = new DataSourceDto();
            dto.setUsername(userName);
            dto.setPassword(pwd);
            dto.setJdbcUrl(url);
            dto.setDriverName(dbTypeEnum.getConnectDriver());
            dto.setId(apiConfig.getDbConfigId());
            dto.setDynSentence(sql);
            dto.setSourceType(dbConfig.getType());
            List<com.alibaba.fastjson2.JSONObject> list = dataSourceService.execute(dto);
            long costTime = System.currentTimeMillis() - startTime;
            if (costTime > apiConfig.getTimeOut()) {
                log.info("=====runApiByType 接口响应超时 costTime:{},timeOut:{}===========", costTime, apiConfig.getTimeOut());
                response = new AjaxResult.Response(AjaxResult.Type.ERROR, "error", "接口请求超时");
            } else {
                response = new AjaxResult.Response(AjaxResult.Type.SUCCESS, "success", list);
            }
        } else {
            response = new AjaxResult.Response(AjaxResult.Type.ERROR, "error", "请求类型不匹配");
        }
        return response;
    }


    @Override
    public AjaxResult.Response runApi(String apiName, List<ApiParam> apiParamList) throws Exception {
        log.info("=========runApi apiName:{},apiParamList:{}=============", apiName, JSONObject.toJSONString(apiParamList));
        Map<String, String> paramsMap = new HashMap<>();
        for (ApiParam apiParam : apiParamList) {
            paramsMap.put(apiParam.getName(), apiParam.getValue());
        }
        ApiConfig apiConfig = selectApiConfigByApiName(apiName);
        DbConfig dbConfig = dbConfigService.selectDbConfigById(apiConfig.getDbConfigId());
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
        List<LinkedHashMap<String, Object>> list = RunApi.runSql(dbTypeEnum, url, userName, pwd, sql);
        AjaxResult.Response response = new AjaxResult.Response(AjaxResult.Type.SUCCESS, "success", list);

        return response;
    }
}
