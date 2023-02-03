package com.vince.xq.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.vince.xq.common.enums.DbTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RunApi {
    private static final Logger log = LoggerFactory.getLogger(RunApi.class);

    public static List<LinkedHashMap<String, Object>> runSql(DbTypeEnum dbTypeEnum, String url, String userName, String pwd, String sql) throws Exception {
        try {
            Class.forName(dbTypeEnum.getConnectDriver());
        } catch (ClassNotFoundException e) {
            throw new Exception("注册驱动失败");
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, pwd);
            Statement stat = conn.createStatement();
            ResultSet re = stat.executeQuery(sql);
            ResultSetMetaData metaData = re.getMetaData();  //获取列集
            int columnCount = metaData.getColumnCount(); //获取列的数量
            List<LinkedHashMap<String, Object>> list = new ArrayList<>();
            while (re.next()) {
                LinkedHashMap<String, Object> hashMap = new LinkedHashMap();
                for (int i = 0; i < columnCount; i++) { //循环列
                    String columnName = metaData.getColumnName(i + 1); //通过序号获取列名,起始值为1
                    int type = metaData.getColumnType(i + 1);
                    if (Types.INTEGER == type) {
                        int columnValue = re.getInt(columnName);
                        hashMap.put(columnName, columnValue);
                    } else {
                        String columnValue = re.getString(columnName);
                        hashMap.put(columnName, columnValue);
                    }
                }
                list.add(hashMap);
            }
            re.close();
            stat.close();
            conn.close();
            log.info("======list:{}=====", JSONObject.toJSONString(list));
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("连接数据库失败");
        }
    }
}
