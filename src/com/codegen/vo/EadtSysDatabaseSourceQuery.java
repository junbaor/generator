package com.codegen.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class EadtSysDatabaseSourceQuery implements Serializable {

    /** 数据源配置记录ID */
    private String databasesourcedbid;
    /** 配置名称 */
    private String configname;
    /** 数据库版本 */
    private String dbversion;
    /** 连接地址 */
    private String connecturl;
    /** 驱动类 */
    private String dbdriver;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 保留字段1 */
    private String reserve;
    /** 保留字段2 */
    private String reserve2;
    /** 保留字段3 */
    private String reserve3;


    public String getDatabasesourcedbid() {
        return this.databasesourcedbid;
    }


    public void setDatabasesourcedbid(String value) {
        this.databasesourcedbid = value;
    }


    public String getConfigname() {
        return this.configname;
    }


    public void setConfigname(String value) {
        this.configname = value;
    }


    public String getDbversion() {
        return this.dbversion;
    }


    public void setDbversion(String value) {
        this.dbversion = value;
    }


    public String getConnecturl() {
        return this.connecturl;
    }


    public void setConnecturl(String value) {
        this.connecturl = value;
    }


    public String getDbdriver() {
        return this.dbdriver;
    }


    public void setDbdriver(String value) {
        this.dbdriver = value;
    }


    public String getUsername() {
        return this.username;
    }


    public void setUsername(String value) {
        this.username = value;
    }


    public String getPassword() {
        return this.password;
    }


    public void setPassword(String value) {
        this.password = value;
    }


    public String getReserve() {
        return this.reserve;
    }


    public void setReserve(String value) {
        this.reserve = value;
    }


    public String getReserve2() {
        return this.reserve2;
    }


    public void setReserve2(String value) {
        this.reserve2 = value;
    }


    public String getReserve3() {
        return this.reserve3;
    }


    public void setReserve3(String value) {
        this.reserve3 = value;
    }


    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
