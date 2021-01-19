package com.bluesgao.databus.ds;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JdbcProps {
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
    /**
     * 数据库地址
     */
    private String url;
    /**
     * mysql驱动
     */
    private String driverClassName;

    private String table;

    private String sql;
}
