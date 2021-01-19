package com.bluesgao.databus.ds;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class JdbcBuilder {

    public static DataSource build(JdbcProps props) {
        DataSource dataSource = null;
        Map<String, Object> cfgMap = new HashMap<>(16);
        //基本属性 url、user、password
        cfgMap.put("url", props.getUrl());
        cfgMap.put("driverClassName", props.getDriverClassName());
        cfgMap.put("username", props.getUsername());
        cfgMap.put("password", props.getPassword());
        //配置初始化大小、最小、最大
        cfgMap.put("initialSize", "5");
        cfgMap.put("minIdle", "5");
        cfgMap.put("maxActive", "10");
        //配置获取连接等待超时的时间
        cfgMap.put("maxWait", "3000");
        //配置一个连接在池中最小生存的时间，单位是毫秒
        cfgMap.put("minEvictableIdleTimeMillis", "300000");


        try {
            dataSource = DruidDataSourceFactory.createDataSource(cfgMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dataSource != null) {
            return dataSource;
        }
        return null;
    }
}
