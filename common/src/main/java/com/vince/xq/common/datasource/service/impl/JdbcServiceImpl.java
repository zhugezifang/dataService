package com.vince.xq.common.datasource.service.impl;

import com.alibaba.druid.pool.DruidDataSource;

import com.vince.xq.common.datasource.config.DruidPropertie;
import com.vince.xq.common.datasource.dto.DataSourceDto;
import com.vince.xq.common.datasource.service.IJdbcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Copyright (c) 2023
 * @Author: sgx
 * @Description:
 **/
@Service
@Slf4j
public class JdbcServiceImpl implements IJdbcService {

    @Autowired
    private DruidPropertie druidProperties;

    /**
     * 所有数据源的连接池存在map里
     */
    private static Map<Long, DruidDataSource> map = new ConcurrentHashMap<>();
    private Object lock = new Object();

    /**
     * 随便初始化连接池，防止空指针异常
     */
    static {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        map.put(0L,dataSource);
    }


    public DruidDataSource getJdbcConnectionPool(DataSourceDto dataSource) {
        if (map.containsKey(dataSource.getId())) {
            return map.get(dataSource.getId());
        } else {
            try {
                synchronized (lock) {
                    if (!map.containsKey(dataSource.getId())) {
                        DruidDataSource pool = druidProperties.dataSource(dataSource.getJdbcUrl(),
                                dataSource.getUsername(), dataSource.getPassword(), dataSource.getDriverName());
                        map.put(dataSource.getId(), pool);
                        log.info("创建连接池成功：{}", dataSource.getJdbcUrl());
                    }
                }
                return map.get(dataSource.getId());
            } finally {

            }
        }
    }


    @Override
    public void test(Long id) {
        log.info("id:"+id);
    }

    /**
     * 删除数据库连接池
     *
     * @param id
     */
    @Override
    public void removeJdbcConnectionPool(Long id) {
        try {
            DruidDataSource pool = map.get(id);
            if (pool != null) {
                log.info("remove pool success, datasourceId:{}", id);
                map.remove(id);
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
        }
    }



    /**
     * 获取连接
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getPooledConnection(DataSourceDto dataSource) throws SQLException{
        DruidDataSource pool = getJdbcConnectionPool(dataSource);
        return pool.getConnection();
    }

    /**
     * 测试数据库连接  获取一个连接
     *
     * @param dataSource
     * @return
     * @throws ClassNotFoundException driverName不正确
     * @throws SQLException
     */
    @Override
    public Connection getUnPooledConnection(DataSourceDto dataSource) throws SQLException {
        DruidDataSource druidDataSource = druidProperties.dataSource(dataSource.getJdbcUrl(),
                dataSource.getUsername(), dataSource.getPassword(), dataSource.getDriverName());
        map.put(dataSource.getId(),druidDataSource);
        return druidDataSource.getConnection();
    }

}
