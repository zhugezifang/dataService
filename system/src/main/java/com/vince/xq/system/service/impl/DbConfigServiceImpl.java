package com.vince.xq.system.service.impl;

import com.vince.xq.common.constant.Constants;
import com.vince.xq.common.constant.GenConstants;
import com.vince.xq.common.core.text.Convert;
import com.vince.xq.common.enums.DbTypeEnum;
import com.vince.xq.common.utils.ShiroUtils;
import com.vince.xq.common.utils.StringUtils;
import com.vince.xq.system.domain.DbConfig;
import com.vince.xq.system.mapper.DbConfigMapper;
import com.vince.xq.system.service.IDbConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class DbConfigServiceImpl implements IDbConfigService {

    @Autowired
    private DbConfigMapper dbConfigMapper;

    @Override
    public List<DbConfig> selectDbConfigList(DbConfig dbConfig) {
        return dbConfigMapper.selectDbConfigList(dbConfig);
    }

    @Override
    public List<DbConfig> selectDbConfigAll() {
        return dbConfigMapper.selectDbConfigAll();
    }

    /*@Override
    public List<DbConfig> selectDbConfigsByUserId(Long userId)
    {
        List<DbConfig> userDbConfigs = dbConfigMapper.selectDbConfigsByUserId(userId);
        List<DbConfig> dbConfigs = dbConfigMapper.selectDbConfigAll();
        for (DbConfig DbConfig : dbConfigs)
        {
            for (DbConfig userDbConfig : userDbConfigs)
            {
                if (DbConfig.getId().longValue() == userDbConfig.getId().longValue())
                {
                    DbConfig.setFlag(true);
                    break;
                }
            }
        }
        return dbConfigs;
    }*/


    @Override
    public DbConfig selectDbConfigById(Long id) {
        return dbConfigMapper.selectDbConfigById(id);
    }

    @Override
    public List<String> selectDbTypesAll() {
        List<String> list = new ArrayList<>();
        for (DbTypeEnum dbTypeEnum : DbTypeEnum.values()) {
            list.add(dbTypeEnum.getType());
        }
        return list;
    }

    @Override
    public int deleteDbConfigByIds(String ids) {
        Long[] idsArray = Convert.toLongArray(ids);
        /*for (Long id : idsArray)
        {
            DbConfig DbConfig = selectDbConfigById(id);
            if (countUserPostById(DbConfig) > 0)
            {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }*/
        return dbConfigMapper.deleteDbConfigByIds(idsArray);
    }


    @Override
    public int insertDbConfig(DbConfig dbConfig) {
        dbConfig.setCreateBy(ShiroUtils.getLoginName());
        return dbConfigMapper.insertDbConfig(dbConfig);
    }


    @Override
    public int updateDbConfig(DbConfig dbConfig) {
        dbConfig.setCreateBy(ShiroUtils.getLoginName());
        return dbConfigMapper.updateDbConfig(dbConfig);
    }

    @Override
    public int countUserPostById(Long postId) {
        return 0;
    }


    /*@Override
    public int countUserPostById(Long postId)
    {
        return userPostMapper.countUserPostById(postId);
    }*/


    @Override
    public String checkConnectNameUnique(DbConfig dbConfig) {
        DbConfig info = dbConfigMapper.checkConnectNameUnique(dbConfig.getConnectName());
        if (StringUtils.isNotNull(info)) {
            return GenConstants.DBCONFIG_NAME_NOT_UNIQUE;
        }
        return GenConstants.DBCONFIG_NAME_UNIQUE;
    }

    @Override
    public void testConnection(DbConfig dbConfig) throws Exception {
        DbTypeEnum dbTypeEnum = DbTypeEnum.findEnumByType(dbConfig.getType());
        if (dbTypeEnum == null) {
            throw new Exception("不识别的类型");
        } else {
            testConnectionDriver(dbConfig, dbTypeEnum.getConnectDriver());
        }
    }


    private void testConnectionDriver(DbConfig dbConfig, String connectDriver) throws Exception {
        try {
            Class.forName(connectDriver);
        } catch (ClassNotFoundException e) {
            throw new Exception("注册驱动失败");
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUserName(), dbConfig.getPwd());
            Statement stat = conn.createStatement();
            ResultSet re = stat.executeQuery(Constants.TEST_CONNECT_SQL);
            int i = 0;
            while (re.next()) {
                i++;
                //System.out.println(re.getString(1));
            }
            re.close();
            stat.close();
            conn.close();
            if (i == 0) {
                throw new Exception("该连接下没有库");
            }
        } catch (SQLException e) {
            throw new Exception("连接数据库失败");
        }
    }
}
