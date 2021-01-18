package com.bluesgao.databus.fetcher.datasource;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JdbcProps {
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
    protected String username;
    /**
     * 数据源连接密码
     */
    protected String password;
    /**
     * 数据源实例
     */
    protected String database;
}
