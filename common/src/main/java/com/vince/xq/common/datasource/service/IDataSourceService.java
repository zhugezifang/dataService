package com.vince.xq.common.datasource.service;

import com.alibaba.fastjson2.JSONObject;
import com.vince.xq.common.datasource.dto.DataSourceDto;


import java.util.List;

public interface IDataSourceService {


    /**
     * 执行sql
     * @param dto
     * @return
     */
    List<JSONObject> execute(DataSourceDto dto);

    long mysqlTotal(DataSourceDto sourceDto);
    List<JSONObject> executeRelationalDb(DataSourceDto dto);
    /**
     * 关系型数据库 测试连接
     *
     * @param dto
     */
    Boolean testRelationalDb(DataSourceDto dto);
}
