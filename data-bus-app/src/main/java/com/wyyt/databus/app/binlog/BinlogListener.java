package com.wyyt.databus.app.binlog;

import com.alibaba.fastjson.JSON;
import com.wwyt.databus.core.binlog.Binlog;
import com.wwyt.databus.core.binlog.BinlogWrapper;
import com.wwyt.databus.plugin.common.enums.EventType;
import com.wyyt.databus.app.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BinlogListener {

    @Resource
    private BinlogDispatcher dispatcher;

    @KafkaListener(topics = "#{'${topicName}'.split(',')}", id = Constants.BINLOG_CONSUMER_GROUP_ID)
//    @KafkaListener(topics = Constants.BINLOG_TOPIC_PATTERN, id = Constants.BINLOG_CONSUMER_GROUP_ID)
    public void onMessage(List<ConsumerRecord<?, ?>> records) {
        log.info("binlog批量拉取的数据数量:{}:", records.size());

        List<BinlogWrapper> binlogWrappers = new ArrayList<BinlogWrapper>(records.size());
        for (ConsumerRecord<?, ?> record : records) {
            if (Optional.ofNullable(record.value()).isPresent()) {
                //parse binlog
                Binlog binlog = JSON.parseObject(record.value().toString(), Binlog.class);
                log.info("binlog:{}", JSON.toJSONString(binlog));
                //包装binlog
                if (supportEvent(binlog.getType())) {
                    binlogWrappers.add(BinlogWrapper.warp(binlog));
                }
            }
        }

        log.info("binlogWrappers:{}", JSON.toJSONString(binlogWrappers));
        if (!CollectionUtils.isEmpty(binlogWrappers)) {
            //处理binlog
            dispatcher.dispatch(binlogWrappers);
        }

    }

    private boolean supportEvent(String event) {
        if (event.equalsIgnoreCase(EventType.INSERT.getEvent())
                || event.equalsIgnoreCase(EventType.UPDATE.getEvent())
                || event.equalsIgnoreCase(EventType.DELETE.getEvent())) {
            return true;
        }
        return false;
    }
}
