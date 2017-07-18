package com.common.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.codegen.po.EadtSysDatabaseSource;


/**
 * Created by IntelliJ IDEA. User: ritchrs Date: 11-10-24 Time: 下午2:28 desc:
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> _targetDataSources;
    //数据源类型 1 dbcp  BasicDataSource类型 2 DruidDataSource
    private static String type;


    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }


    public void setTargetDataSources(Map targetDataSources) {
        this._targetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }


    public String getDbtype(String key) {
        String driver = null;
        if (_targetDataSources.get(key) instanceof BasicDataSource) {
            driver = ((BasicDataSource) _targetDataSources.get(key)).getDriverClassName();
        } else {
            driver = ((DruidDataSource) _targetDataSources.get(key)).getDriverClassName();
        }
        if ("oracle.jdbc.driver.OracleDriver".equals(driver)) {
            return "oracle";
        } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driver)) {
            return "SQLServer";
        } else if ("com.ibm.db2.jcc.DB2Driver".equals(driver)) {
            return "db2";
        }
        return null;
    }


    public void addTargetDataSource(String key, DataSource dataSource) {
        _targetDataSources.put(key, dataSource);
        this.setTargetDataSources(_targetDataSources);
    }


    public void addDataSource(String key, EadtSysDatabaseSource eadtSysDatabaseSource) {
        if (!this.isExist(key)) {
            DataSource dataSource =
                    this.createDataSource(eadtSysDatabaseSource.getDbdriver(),
                            eadtSysDatabaseSource.getConnecturl(), eadtSysDatabaseSource.getUsername(),
                            eadtSysDatabaseSource.getPassword());

            this.addTargetDataSource(key, dataSource);
        }
    }


    public static DataSource createDataSource(String driver, String url, String username, String password) {
        if (type.equals("1")) {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setInitialSize(10);
            dataSource.setMaxActive(50);
            dataSource.setMaxIdle(30);
            dataSource.setMinIdle(10);
            dataSource.setMaxWait(5000);
            return dataSource;
        } else {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(driver);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
            druidDataSource.setInitialSize(10);
            druidDataSource.setMaxActive(50);
            druidDataSource.setMaxIdle(30);
            druidDataSource.setMinIdle(10);
            druidDataSource.setMaxWait(5000);
//            DataSourceProxy proxyDataSource = new DataSourceProxy();
//            proxyDataSource.setRealDataSource(druidDataSource);
            return druidDataSource;
        }
    }


    /**
     * 添加数据源并切换
     *
     * @param
     */
    public void dnamicDataSource(EadtSysDatabaseSource eadtSysDatabaseSource) {
        this.addDataSource(eadtSysDatabaseSource.getDatabasesourcedbid(), eadtSysDatabaseSource);// 添加数据源
        DataSourceContextHolder.setDataSource(eadtSysDatabaseSource.getDatabasesourcedbid());
    }


    public boolean isExist(String key) {
        return _targetDataSources.containsKey(key);
    }


    /**
     * 获取数据源
     *
     * @param key
     * @return
     */
    public DataSource getDataSource(String key) {
        return (DataSource) this._targetDataSources.get(key);
    }
//
//    /**
//     * 获取数据源
//     *
//     * @param key
//     * @return
//     */
//    public static  DataSource getDataSource2(String key) {
//        return (DataSource) this._targetDataSources.get(key);
//    }


    /**
     * 指定key，获取连接
     *
     * @param key
     * @return
     */
    public Connection getConnection(String key) {
        Connection connection = null;
        DataSource dataSource = (DataSource) this._targetDataSources.get(key);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        DynamicDataSource.type = type;
    }
}
