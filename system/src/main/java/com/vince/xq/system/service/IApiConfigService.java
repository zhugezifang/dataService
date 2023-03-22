package com.vince.xq.system.service;

import com.vince.xq.common.core.domain.AjaxResult;
import com.vince.xq.system.domain.ApiConfig;
import com.vince.xq.system.domain.ApiParam;

import java.util.LinkedHashMap;
import java.util.List;

public interface IApiConfigService {

    public List<ApiConfig> selectApiConfigList(ApiConfig apiConfig);

    public List<ApiConfig> selectApiConfigAll();

    /*public List<ApiConfig> selectApiConfigsByUserId(Long userId);*/

    public ApiConfig selectApiConfigById(Long id);

    public int deleteApiConfigByIds(String ids);

    public int insertApiConfig(ApiConfig apiConfig);

    public int updateApiConfig(ApiConfig apiConfig);

    public int countUserPostById(Long postId);

    public String checkNameUnique(ApiConfig apiConfig);

    public int updateApiConfigOnline(ApiConfig apiConfig);

    public AjaxResult.Response runApi(String apiName, List<ApiParam> params) throws Exception;

    public AjaxResult.Response runApiByType(String apiName, List<ApiParam> params, String type,long startTime) throws Exception;

    public AjaxResult.Response runApiByTypePool(String apiName, List<ApiParam> params, String type,long startTime) throws Exception;

    public ApiConfig selectApiConfigByApiName(String apiName);

}
