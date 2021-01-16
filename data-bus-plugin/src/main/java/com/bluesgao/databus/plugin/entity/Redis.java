package com.bluesgao.databus.plugin.entity;

import java.io.Serializable;

public class Redis  implements Serializable {

    /**
     * redis连接地址
     */
    private String host;
    /**
     * redis连接端口号
     */
    private String port;

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

    public Redis(String host, String port, String userName, String password, String database) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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
