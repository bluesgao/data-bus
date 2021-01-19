package com.bluesgao.databus.plugin.common.constants;

public interface RedisCfgConstants {
    /*连接信息*/
    String host = "host";
    String port = "port";
    String username = "username";
    String password = "password";
    String database = "ds";

    /*key配置*/
    String key_prefix = "key.prefix";//key前缀
    String key_separator = "key.separator";//key的分隔符
    String key_type = "key.type";//key的数据类型（string,map）
    String key_items = "key.items";//key的组成字段

    /*业务字段*/
    String biz_fields = "biz.fields";//需要处理的字段
}
