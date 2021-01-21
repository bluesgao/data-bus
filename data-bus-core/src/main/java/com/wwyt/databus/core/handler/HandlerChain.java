package com.wwyt.databus.core.handler;

import com.wwyt.databus.core.binlog.BinlogWrapper;
import com.wwyt.databus.core.handler.impl.FetcherHandler;
import com.wwyt.databus.core.handler.impl.MonitorHandler;
import com.wwyt.databus.core.handler.impl.ProcessorHandler;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HandlerChain {

    private static List<RuleHandler> handlerList = new ArrayList<>(8);

    static {
        //bulidHandlerChain
        handlerList.add(new MonitorHandler());
        handlerList.add(new FetcherHandler());
        handlerList.add(new ProcessorHandler());
    }

    public static void process(BinlogWrapper binlogWrapper, RuleCfg rule) {
        log.info("处理器链开始处理：binlogWrapper:{},ruleCfg:{}", binlogWrapper, rule);
        for (RuleHandler handler : handlerList) {
            HandlerResult ret = handler.handle(binlogWrapper, rule);
            log.info("处理器链处理结果：HandlerResult:{}", ret);
            if (!ret.getSuccess()) {
                //处理器结果为false，则终止处理
                break;
            }

        }
    }
}
