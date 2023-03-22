package com.vince.xq.common.datasource.service;




import com.vince.xq.common.datasource.dto.DataSourceDto;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Copyright (c) 2023, SGX COMPANY.
 * @Author: sgx
 * @Description:
 **/
public interface IJdbcService {

    void test(Long id);

    /**
     * 删除数据库连接池
     *
     * @param id
     */
    void removeJdbcConnectionPool(Long id);


    /**
     * 获取连接
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    Connection getPooledConnection(DataSourceDto dataSource) throws SQLException;

    /**
     * 测试数据库连接  获取一个连接
     *
     * @param dataSource
     * @return
     * @throws ClassNotFoundException driverName不正确
     * @throws SQLException
     */
    Connection getUnPooledConnection(DataSourceDto dataSource) throws SQLException;
}
