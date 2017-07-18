package com.common.base;

import javax.sql.DataSource;

import com.codegen.po.EadtSysDatabaseSource;


/**
 * Created by IntelliJ IDEA. User: ritchrs Date: 11-10-24 Time: 下午3:39 desc:
 */
public class DataSourceContextHolder {

    private static final ThreadLocal contextHolder = new ThreadLocal();


    public static void setDataSource(String customerType) {
        contextHolder.set(customerType);
    }


    public static String getDataSource() {
        return (String) contextHolder.get();
    }


    public static void clearDataSource() {
        contextHolder.remove();
    }


    /**
     * 创建数据源
     * 
     * @param dataSource1
     * @param eadtSysDatabaseSource
     * @param dynamicDataSource
     */
    public static void changeDataSource(String dataSource1, EadtSysDatabaseSource eadtSysDatabaseSource,
            DynamicDataSource dynamicDataSource) {
        if (!dynamicDataSource.isExist(dataSource1)) {
            DataSource dataSource =
                    DynamicDataSource.createDataSource(eadtSysDatabaseSource.getDbdriver(),
                            eadtSysDatabaseSource.getConnecturl(), eadtSysDatabaseSource.getUsername(),
                            eadtSysDatabaseSource.getPassword());
            dynamicDataSource.addTargetDataSource(dataSource1, dataSource);
        }
        DataSourceContextHolder.setDataSource(dataSource1);
    }

}
