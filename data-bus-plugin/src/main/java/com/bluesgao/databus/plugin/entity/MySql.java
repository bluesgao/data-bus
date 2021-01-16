package com.bluesgao.databus.plugin.entity;

import java.io.Serializable;

public class MySql implements Serializable {

    /**
     * 数据库地址
     */
    private String url;
    /**
     * mysql驱动
     */
    private String driverClassName;

    /**
     * 数据源连接用户名
     */
    protected String userName;
    /**
     * 数据源连接密码
     */
    protected String password;
    /**
     * 数据源实例
     */
    protected String database;

    public MySql(String url, String driverClassName, String userName, String password, String database) {
        this.url = url;
        this.driverClassName = driverClassName;
        this.userName = userName;
        this.password = password;
        this.database = database;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
