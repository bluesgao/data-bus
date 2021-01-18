package com.bluesgao.databus.fetcher.datasource;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RedisProps {

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
