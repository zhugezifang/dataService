package com.vince.xq.system.service;

import com.vince.xq.system.domain.DbConfig;

import java.util.List;

/**
 * 岗位信息 服务层
 * 
 * @author ruoyi
 */
public interface IDbConfigService
{
    public List<DbConfig> selectDbConfigList(DbConfig dbConfig);

    public List<DbConfig> selectDbConfigAll();

    /*public List<DbConfig> selectDbConfigsByUserId(Long userId);*/

    public DbConfig selectDbConfigById(Long id);

    public List<String> selectDbTypesAll();

    public int deleteDbConfigByIds(String ids);

    public int insertDbConfig(DbConfig dbConfig);

    public int updateDbConfig(DbConfig dbConfig);

    public int countUserPostById(Long postId);

    public String checkConnectNameUnique(DbConfig dbConfig);

    public void testConnection(DbConfig dbConfig) throws Exception;

}
