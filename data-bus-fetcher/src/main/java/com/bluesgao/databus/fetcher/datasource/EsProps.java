package com.bluesgao.databus.fetcher.datasource;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EsProps {

    /**
     * 连接地址
     */
    private String clusterHostPort;
    /**
     * 数据源连接用户名
     */
    protected String username;
    /**
     * 数据源连接密码
     */
    protected String password;
}
