package com.vince.xq.system.mapper;

import com.vince.xq.system.domain.DbConfig;

import java.util.List;


public interface DbConfigMapper
{

    public List<DbConfig> selectDbConfigList(DbConfig DbConfig);

    public List<DbConfig> selectDbConfigAll();

    public DbConfig selectDbConfigById(Long id);

    public int deleteDbConfigByIds(Long[] ids);

    public int updateDbConfig(DbConfig DbConfig);

    public int insertDbConfig(DbConfig DbConfig);

    public DbConfig checkConnectNameUnique(String connectName);

    public List<DbConfig> selectDbConfigsByUser(String createBy);
}
