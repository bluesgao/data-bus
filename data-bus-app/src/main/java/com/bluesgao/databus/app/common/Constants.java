package com.bluesgao.databus.app.common;

/**
 * 常量
 */
public interface Constants {

    String BINLOG_CONSUMER_GROUP_ID = "${spring.kafka.consumer.group-id}";
    String SETTING_SUFFIX = ".json";
    String RETRY_TOPIC = "${spring.kafka.producer.retry.topic}";
    String FAIL_TOPIC = "${spring.kafka.producer.fail.topic}";
    String RETRY_CONSUMER_GROUP_ID = "${spring.kafka.consumer.retry.group-id}";
    Integer RETRY_TIME = 3;
}
