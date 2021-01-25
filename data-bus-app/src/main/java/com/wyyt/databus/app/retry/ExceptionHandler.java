package com.wyyt.databus.app.retry;

import com.alibaba.fastjson.JSON;
import com.wwyt.databus.core.binlog.BinlogItemWrapper;
import com.wwyt.databus.core.binlog.BinlogWrapper;
import com.wwyt.databus.core.common.ExceptionPolicyType;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ExceptionHandler {
    @Resource
    private RetryMsgProducer retryMsgProducer;

    public void handleException(ExceptionPolicyType exceptionPolicyType, BinlogWrapper binlogWrapper, RuleCfg rule, Exception e) {
        log.info("处理异常:binlog[{}],rules[{}],e[{}]", binlogWrapper, rule, e);
        if (exceptionPolicyType.equals(ExceptionPolicyType.LOG)) {
            log.error("处理异常策略[LOG]:binlog[{}],rules[{}],e[{}]", binlogWrapper, rule, e);
        } else if (exceptionPolicyType.equals(ExceptionPolicyType.DISCARD)) {
            log.warn("处理异常策略[DISCARD]:binlog[{}],rules[{}],e[{}]", binlogWrapper, rule, e);
        } else if (exceptionPolicyType.equals(ExceptionPolicyType.RETRY)) {
            log.info("处理异常策略[RETRY]:binlog[{}],rules[{}],e[{}]", binlogWrapper, rule, e);
            //异常处理
            BinlogWrapper wrapper = BinlogWrapper.warp(binlogWrapper.getBinlog(),
                    binlogWrapper.getRetry().getRetryCount() + 1,
                    "handler处理异常",
                    System.currentTimeMillis());
            try {
                retryMsgProducer.send(wrapper);
            } catch (Exception ex) {
                //记录日志，防止发送失败
                log.error("retryProducer.send 失败,消息:{}", JSON.toJSONString(binlogWrapper), ex);
            }
        } else {
            log.error("处理异常策略配置错误:binlog[{}],rules[{}],e[{}]", binlogWrapper, rule, e);
        }

    }
}
