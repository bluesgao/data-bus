package com.bluesgao.databus.processor;

import com.alibaba.fastjson.JSON;
import com.bluesgao.databus.ds.RedisBuilder;
import com.bluesgao.databus.ds.RedisProps;
import com.bluesgao.databus.plugin.common.enums.EventType;
import com.bluesgao.databus.plugin.common.constants.RedisCfgConstants;
import com.bluesgao.databus.plugin.processor.DataProcessor;
import com.bluesgao.databus.plugin.processor.DataProcessorResult;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName：RedisProcessor
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class RedisProcessor implements DataProcessor {

    @Override
    public DataProcessorResult process(Map<String, Object> params, String event, Map<String, Object> data) {
        log.info("RedisProcessor ****开始处理****");
        if (params != null && params.size() > 0) {
            //进行参数验证
            String checkResult = checkParams(params);
            if (checkResult != null && checkResult.length() > 0) {
                return DataProcessorResult.fail(checkResult);
            }
        }

        Jedis jedis = getJedis(params);

        String keyType = params.get(RedisCfgConstants.key_type).toString();
        String key = genKey(params);
        List<String> bizFields = (List<String>) params.get(RedisCfgConstants.biz_fields);
        Map<String, String> kvMap = getKeyValueMap(bizFields, data);

        if (event.equalsIgnoreCase(EventType.INSERT.getEvent())) {
            log.info("INSERT 处理中");
            if (keyType.equalsIgnoreCase("string")) {
                jedis.set(key, JSON.toJSONString(kvMap));
            } else if (keyType.equalsIgnoreCase("map")) {
                jedis.hmset(key, kvMap);
            } else {
                log.warn("redis processor 暂时只支持string和map数据结构,redisKey:", key);
            }
        } else if (event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            log.info("UPDATE 处理中");

        } else if (event.equalsIgnoreCase(EventType.DELETE.getEvent())) {
            log.info("DELETE 处理中");
        }
        return DataProcessorResult.fail("");
    }

    @Override
    public String getName() {
        return RedisProcessor.class.getCanonicalName();
    }

    /**
     * redis 客户端
     *
     * @param params
     * @return
     */
    private Jedis getJedis(Map<String, Object> params) {
        //jedis
        RedisProps props = new RedisProps();
        props.setHost(params.get(RedisCfgConstants.host).toString());
        props.setPort(params.get(RedisCfgConstants.port).toString());
        props.setUsername(params.get(RedisCfgConstants.username).toString());
        props.setPassword(params.get(RedisCfgConstants.password).toString());
        props.setDatabase(params.getOrDefault(RedisCfgConstants.database, "0").toString());
        JedisPool jedisPool = RedisBuilder.build(props);
        return jedisPool.getResource();
    }

    /**
     * 参数检查
     *
     * @param params
     * @return
     */
    private String checkParams(Map<String, Object> params) {
        StringBuilder err = new StringBuilder();
        if (Objects.isNull(params.get(RedisCfgConstants.host))) {
            err.append("host为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.port))) {
            err.append("port为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.username))) {
            err.append("username为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.password))) {
            err.append("password为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.key_prefix))) {
            err.append("key_prefix为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.key_separator))) {
            err.append("key_separator为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.key_type))) {
            err.append("key_type为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.key_items)) ||
                !(params.get(RedisCfgConstants.key_items) instanceof List)) {
            err.append("key_items为空;");
        }
        return err.toString();
    }

    /**
     * 拼接redis key
     *
     * @param params
     * @return
     */
    private static String genKey(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();

        String keyPrefix = params.get(RedisCfgConstants.key_prefix).toString();
        String keySeparator = params.get(RedisCfgConstants.key_separator).toString();
        List<String> keyItems = (List<String>) params.get(RedisCfgConstants.key_items);

        if (StringUtils.isNotEmpty(keyPrefix) && StringUtils.isNotEmpty(keySeparator) && !CollectionUtils.isEmpty(keyItems)) {
            //拼接前缀
            builder.append(keyPrefix);
            //拼接分隔符
            builder.append(keySeparator);
            //拼接元素
            for (int i = 0; i < keyItems.size(); i++) {
                String item = keyItems.get(i);
                builder.append(item);
                //拼接分隔符(最后一个元素不需要拼接分隔符)
                if (i < (keyItems.size() - 1)) {
                    builder.append(keySeparator);

                }
            }
        }
        return builder.toString();
    }


    /**
     * 获取redis kv数据
     *
     * @param bizFields
     * @param data
     * @return
     */
    private Map<String, String> getKeyValueMap(List<String> bizFields, Map<String, Object> data) {
        Map<String, String> cvMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(bizFields)) {
            for (int i = 0; i < bizFields.size(); i++) {
                String col = bizFields.get(i);
                for (String key : data.keySet()) {
                    if (col.equals(key)) {
                        if (key.contains("_")) {
                            //将 数据库的列名转换为小驼峰，方便业务侧反序列化
                            cvMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key), JSON.toJSONString(data.get(key)));
                        } else {
                            cvMap.put(key, JSON.toJSONString(data.get(key)));
                        }
                    }
                }
            }
        } else {
            for (String key : data.keySet()) {
                if (key.contains("_")) {
                    //将 数据库的列名转换为小驼峰，方便业务侧反序列化
                    cvMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key), JSON.toJSONString(data.get(key)));
                } else {
                    cvMap.put(key, JSON.toJSONString(data.get(key)));
                }
            }
        }
        return cvMap;
    }
}
