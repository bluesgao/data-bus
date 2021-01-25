package com.wyyt.databus.app.retry;

import com.alibaba.fastjson.JSON;
import com.wwyt.databus.core.binlog.BinlogItemWrapper;
import com.wwyt.databus.core.binlog.BinlogWrapper;
import com.wyyt.databus.app.binlog.BinlogDispatcher;
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
public class RetryMsgListener {

    @Resource
    private BinlogDispatcher dispatcher;

    @KafkaListener(topics = Constants.RETRY_TOPIC, id = Constants.RETRY_CONSUMER_GROUP_ID)
    public void onMessage(List<ConsumerRecord<?, ?>> records) {
        log.info("retryMsg批量拉取的数据数量:{}:", records.size());

        List<BinlogWrapper> binlogWrappers = new ArrayList<BinlogWrapper>(records.size());
        for (ConsumerRecord<?, ?> record : records) {
            if (Optional.ofNullable(record.value()).isPresent()) {
                //parse binlog
                BinlogWrapper binlogWrapper = JSON.parseObject(record.value().toString(), BinlogWrapper.class);
                log.info("binlogWrapper:{}", JSON.toJSONString(binlogWrapper));
                if (binlogWrapper != null) {
                    binlogWrappers.add(binlogWrapper);
                }
            }
        }

        log.info("binlogWrappers:{}", JSON.toJSONString(binlogWrappers));
        if (!CollectionUtils.isEmpty(binlogWrappers)) {
            //处理binlog
            dispatcher.dispatch(binlogWrappers);
        }

    }
}
