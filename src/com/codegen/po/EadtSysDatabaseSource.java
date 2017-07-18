package com.codegen.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.common.base.BaseEntity;


@Entity
@Table(name = "EADT_SYS_DATABASE_SOURCE")
public class EadtSysDatabaseSource extends BaseEntity implements java.io.Serializable {

    // alias
    public static final String TABLE_ALIAS = "记录数据库配置信息";
    public static final String ALIAS_DATABASESOURCEDBID = "数据源配置记录ID";
    public static final String ALIAS_CONFIGNAME = "配置名称";
    public static final String ALIAS_DBVERSION = "数据库版本";
    public static final String ALIAS_CONNECTURL = "连接地址";
    public static final String ALIAS_DBDRIVER = "驱动类";
    public static final String ALIAS_USERNAME = "用户名";
    public static final String ALIAS_PASSWORD = "密码";
    public static final String ALIAS_RESERVE = "保留字段1";
    public static final String ALIAS_RESERVE2 = "保留字段2";
    public static final String ALIAS_RESERVE3 = "保留字段3";

    // date formats

    // 可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
    // columns START
    /**
     * 数据源配置记录ID db_column: DATABASESOURCEDBID
     */
    @Length(max = 32)
    private String databasesourcedbid;
    /**
     * 配置名称 db_column: CONFIGNAME
     */
    @NotBlank
    @Length(max = 200)
    private String configname;
    /**
     * 数据库版本 db_column: DBVERSION
     */
    @NotBlank
    @Length(max = 50)
    private String dbversion;
    /**
     * 连接地址 db_column: CONNECTURL
     */
    @NotBlank
    @Length(max = 500)
    private String connecturl;
    /**
     * 驱动类 db_column: DBDRIVER
     */
    @NotBlank
    @Length(max = 100)
    private String dbdriver;
    /**
     * 用户名 db_column: USERNAME
     */
    @Length(max = 200)
    private String username;
    /**
     * 密码 db_column: PASSWORD
     */
    @Length(max = 50)
    private String password;
    /**
     * 保留字段1 db_column: RESERVE
     */
    @Length(max = 100)
    private String reserve;
    /**
     * 保留字段2 db_column: RESERVE2
     */
    @Length(max = 100)
    private String reserve2;
    /**
     * 保留字段3 db_column: RESERVE3
     */
    @Length(max = 100)
    private String reserve3;


    // columns END

    public EadtSysDatabaseSource() {
    }


    public EadtSysDatabaseSource(String databasesourcedbid) {
        this.databasesourcedbid = databasesourcedbid;
    }


    public void setDatabasesourcedbid(String value) {
        this.databasesourcedbid = value;
    }


    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "uuid")
    @Column(name = "DATABASESOURCEDBID", unique = false, nullable = false, insertable = true, updatable = true, length = 32)
    public String getDatabasesourcedbid() {
        return this.databasesourcedbid;
    }


    @Column(name = "CONFIGNAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
    public String getConfigname() {
        return this.configname;
    }


    public void setConfigname(String value) {
        this.configname = value;
    }


    @Column(name = "DBVERSION", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
    public String getDbversion() {
        return this.dbversion;
    }


    public void setDbversion(String value) {
        this.dbversion = value;
    }


    @Column(name = "CONNECTURL", unique = false, nullable = false, insertable = true, updatable = true, length = 500)
    public String getConnecturl() {
        return this.connecturl;
    }


    public void setConnecturl(String value) {
        this.connecturl = value;
    }


    @Column(name = "DBDRIVER", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
    public String getDbdriver() {
        return this.dbdriver;
    }


    public void setDbdriver(String value) {
        this.dbdriver = value;
    }


    @Column(name = "USERNAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getUsername() {
        return this.username;
    }


    public void setUsername(String value) {
        this.username = value;
    }


    @Column(name = "PASSWORD", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getPassword() {
        return this.password;
    }


    public void setPassword(String value) {
        this.password = value;
    }


    @Column(name = "RESERVE", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
    public String getReserve() {
        return this.reserve;
    }


    public void setReserve(String value) {
        this.reserve = value;
    }


    @Column(name = "RESERVE2", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
    public String getReserve2() {
        return this.reserve2;
    }


    public void setReserve2(String value) {
        this.reserve2 = value;
    }


    @Column(name = "RESERVE3", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
    public String getReserve3() {
        return this.reserve3;
    }


    public void setReserve3(String value) {
        this.reserve3 = value;
    }


    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("Databasesourcedbid", getDatabasesourcedbid()).append("Configname", getConfigname())
            .append("Dbversion", getDbversion()).append("Connecturl", getConnecturl())
            .append("Dbdriver", getDbdriver()).append("Username", getUsername())
            .append("Password", getPassword()).append("Reserve", getReserve())
            .append("Reserve2", getReserve2()).append("Reserve3", getReserve3()).toString();
    }


    public int hashCode() {
        return new HashCodeBuilder().append(getDatabasesourcedbid()).toHashCode();
    }


    public boolean equals(Object obj) {
        if (obj instanceof EadtSysDatabaseSource == false)
            return false;
        if (this == obj)
            return true;
        EadtSysDatabaseSource other = (EadtSysDatabaseSource) obj;
        return new EqualsBuilder().append(getDatabasesourcedbid(), other.getDatabasesourcedbid()).isEquals();
    }
}
