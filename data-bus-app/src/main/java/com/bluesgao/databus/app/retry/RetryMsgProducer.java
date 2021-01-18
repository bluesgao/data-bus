package com.bluesgao.databus.app.retry;

import com.alibaba.fastjson.JSON;
import com.bluesgao.databus.app.common.Constants;
import com.bluesgao.databus.core.binlog.BinlogWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

@Slf4j
@Component
public class RetryMsgProducer {
    private static String targetTopic = null;
    @Resource
    private KafkaTemplate kafkaTemplate;
    @Value("${spring.kafka.producer.retry.topic}")
    private String RETRY_TOPIC;
    @Value("${spring.kafka.producer.fail.topic}")
    private String FAIL_TOPIC;

    public void send(BinlogWrapper msg) {
        if (msg != null) {
            if (msg.getRetry().getRetryCount() <= Constants.RETRY_TIME) {
                targetTopic = RETRY_TOPIC;
            } else {
                targetTopic = FAIL_TOPIC;
            }
            log.info("send send targetTopic:{},binlog :{}", targetTopic, JSON.toJSONString(msg));
            kafkaTemplate.send(targetTopic, JSON.toJSONString(msg)).addCallback(new ListenableFutureCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("发送异常消息失败:topic:{},binlog:{},err:{}", targetTopic, JSON.toJSONString(msg), throwable.getMessage());
                }

                @Override
                public void onSuccess(Object o) {
                    log.info("发送异常消息成功:topic:{},binlog:{},rest:{}", targetTopic, JSON.toJSONString(msg), JSON.toJSONString(o));
                }
            });

        }
    }
}
