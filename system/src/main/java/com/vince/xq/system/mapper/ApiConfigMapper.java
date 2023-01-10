package com.vince.xq.system.mapper;

import com.vince.xq.system.domain.ApiConfig;

import java.util.List;


public interface ApiConfigMapper
{

    public List<ApiConfig> selectApiConfigList(ApiConfig apiConfig);

    public List<ApiConfig> selectApiConfigAll();

    public ApiConfig selectApiConfigById(Long id);

    public ApiConfig selectApiConfigByApiName(String apiName);

    public int deleteApiConfigByIds(Long[] ids);

    public int updateApiConfig(ApiConfig apiConfig);

    public int insertApiConfig(ApiConfig apiConfig);

    public ApiConfig checkApiNameUnique(String apiName);

    public List<ApiConfig> selectApiConfigsByUser(String createBy);

    public int updateApiConfigOnline(ApiConfig apiConfig);
}
