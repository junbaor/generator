package com.common.base;

import org.apache.commons.dbcp.BasicDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 12-3-20
 * Time: 上午10:44
 * To change this template use File | Settings | File Templates.
 */
public class CommonDbpcDataSourcePools {
    private static Map<Object, Object> dbpcDataSourcePools = new HashMap<Object, Object>();

    public static BasicDataSource getDataSource(String key, String driver, String url, String username, String password) {
        if (dbpcDataSourcePools.containsKey(key)) {
            BasicDataSource basicDataSource = (BasicDataSource) dbpcDataSourcePools.get(key);
            boolean driverFlag = basicDataSource.getDriverClassName().equals(driver);
            boolean urlFlag = basicDataSource.getUrl().equals(url);
            boolean usernameFlag = basicDataSource.getUsername().equals(username);
            boolean passwordFlag = basicDataSource.getPassword().equals(password);
            if (driverFlag && urlFlag && usernameFlag && passwordFlag) {
                return basicDataSource;
            } else {
                return newDataSource(key, driver, url, username, password);
            }
        } else {
            return newDataSource(key, driver, url, username, password);
        }
    }

    private static BasicDataSource newDataSource(String key, String driver, String url, String username, String password) {
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
        dbpcDataSourcePools.put(key, dataSource);
        return dataSource;
    }
}
