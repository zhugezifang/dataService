package com.vince.xq.common.datasource.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import com.vince.xq.common.datasource.dto.DataSourceDto;
import com.vince.xq.common.datasource.service.IDataSourceService;
import com.vince.xq.common.datasource.service.IJdbcService;
import com.vince.xq.common.datasource.util.JdbcConstants;
import com.vince.xq.common.exception.base.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Copyright (c) 2023, SGX COMPANY.
 * @Author: sgx
 * @Description: DataSource 实现类
 **/
@Slf4j
@Service
public class DataSourceServiceImpl implements IDataSourceService {
    @Autowired
    private IJdbcService jdbcService;


    @Override
    public List<JSONObject> execute(DataSourceDto dto) {
        String sourceType = dto.getSourceType();
        switch (sourceType) {
            case JdbcConstants.ELASTIC_SEARCH_SQL:
//                return executeElasticsearchSql(dto);
            case JdbcConstants.MYSQL:
            case JdbcConstants.KUDU_IMAPLA:
            case JdbcConstants.ORACLE:
            case JdbcConstants.SQL_SERVER:
            case JdbcConstants.JDBC:
            case JdbcConstants.POSTGRESQL:
                return executeRelationalDb(dto);
            case JdbcConstants.HTTP:
//                return executeHttp(dto);
            default:
//                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY);
                return executeRelationalDb(dto);
        }
    }


    /**
     * 获取mysql count 和添加limit分页信息
     *
     * @param sourceDto 动态sql
     * @return
     */
    @Override
    public long mysqlTotal(DataSourceDto sourceDto) {
        // dynSentence = select name,age from X where X=1
        String dynSentence = sourceDto.getDynSentence();
        String sql = "select count(1) as total from (" + dynSentence + ") as gaeaExecute";
        sourceDto.setDynSentence(sql);
        List<JSONObject> result = executeRelationalDb(sourceDto);
        //sql 拼接 limit 分页信息
        int pageNumber = sourceDto.getPnum();
        int pageSize = sourceDto.getPsize();
        String sqlLimit = " limit " + (pageNumber - 1) * pageSize + "," + pageSize;
        //添加分页SQL select name,age from X where X=1 limit 5,4
        sourceDto.setDynSentence(dynSentence.concat(sqlLimit));
        log.info("当前total：{}, 添加分页参数,sql语句：{}", JSONObject.toJSONString(result), sourceDto.getDynSentence());
        return result.get(0).getLongValue("total");
    }


    /**
     * 将关系型数据库的查询结果转化为json
     *
     * @param dto
     * @return
     */
    @Override
    public List<JSONObject> executeRelationalDb(DataSourceDto dto) {
        Connection pooledConnection = null;
        try {
            pooledConnection = jdbcService.getUnPooledConnection(dto);
            //dto.getDynSentence()  获取对应的SQL
            PreparedStatement statement = pooledConnection.prepareStatement(dto.getDynSentence());
            ResultSet rs = statement.executeQuery();
            //获取列数
            int columnCount = rs.getMetaData().getColumnCount();
            //获取对应的列名
            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnLabel(i);
                columns.add(columnName);
            }
            List<JSONObject> list = new ArrayList<>();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                columns.forEach(t -> {
                    try {
                        Object value = rs.getObject(t);
                        //数据类型转换
                        Object result = dealResult(value);
                        jo.put(t, result);
                    } catch (SQLException throwable) {
                        log.error("error", throwable);
                    }
                });
                list.add(jo);
            }
            return list;
        } catch (Exception throwable) {
            log.error("error", throwable);
        } finally {
            try {
                if (pooledConnection != null) {
                    pooledConnection.close();
                }
            } catch (SQLException throwable) {
                log.error("error", throwable);
            }
        }
        return null;
    }

    /**
     * 解决sql返回值 类型问题
     * (through reference chain: java.util.HashMap["pageData"]->java.util.ArrayList[0]->java.util.HashMap["UPDATE_TIME"]->oracle.sql.TIMESTAMP["stream"])
     *
     * @param result
     * @return
     * @throws SQLException
     */
    private Object dealResult(Object result) throws SQLException {
        if (null == result) {
            return result;
        }
        String type = result.getClass().getName();
        if ("oracle.sql.TIMESTAMP".equals(type)) {
            //oracle.sql.TIMESTAMP处理逻辑
            return new Date((Long) JSON.toJSON(result));
        }
        return result;
    }

    /**
     * 关系型数据库 测试连接
     *
     * @param dto
     */
    @Override
    public Boolean testRelationalDb(DataSourceDto dto)  {
        try {
            Connection unPooledConnection = jdbcService.getUnPooledConnection(dto);
            String catalog = unPooledConnection.getCatalog();
            log.info("数据库测试连接成功：{}", catalog);
            unPooledConnection.close();
            return true;
        } catch (SQLException e) {
            log.error("error", e);
            throw new BaseException("连接信息出错");
        }
    }
}
