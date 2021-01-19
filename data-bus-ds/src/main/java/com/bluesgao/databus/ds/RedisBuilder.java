package com.bluesgao.databus.ds;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Slf4j
public class RedisBuilder {
    public static JedisPool build(RedisProps props) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxTotal(10);
        return new JedisPool(poolConfig, props.getHost(),
                Integer.parseInt(props.getHost()));
    }
}
