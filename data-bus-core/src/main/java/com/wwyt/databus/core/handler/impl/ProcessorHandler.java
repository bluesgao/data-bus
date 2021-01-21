package com.wwyt.databus.core.handler.impl;

import com.wwyt.databus.core.binlog.BinlogWrapper;
import com.wwyt.databus.core.handler.HandlerResult;
import com.wwyt.databus.core.handler.RuleHandler;
import com.wwyt.databus.core.plugin.DataProcessorManager;
import com.wwyt.databus.core.rule.entity.Processor;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import com.wwyt.databus.plugin.processor.DataProcessor;
import com.wwyt.databus.plugin.processor.DataProcessorResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessorHandler implements RuleHandler {

    @Override
    public HandlerResult handle(BinlogWrapper binlogWrapper, RuleCfg ruleCfg) {
        log.info("数据处理-处理器：binlogWrapper:{},ruleCfg:{}", binlogWrapper, ruleCfg);

        for (Processor processor : ruleCfg.getProcessor()) {
            DataProcessor dataProcessor = DataProcessorManager.get(processor.getName());
            if (dataProcessor == null) {
                //未找到指定的dataProcessor
                return HandlerResult.fail(ProcessorHandler.class.getName(), String.format("未找到指定的dataProcessor[%s]", processor.getName()));
            }
            DataProcessorResult ret = dataProcessor.process(processor.getParams(), binlogWrapper.getBinlog().getType(), binlogWrapper.getData());
            log.info("数据处理-处理器[{}]处理结果[{}]", processor.getName(), ret);
            if (ret == null || !ret.getSuccess()) {
                //指定的dataProcessor处理失败
                return HandlerResult.fail(processor.getName(), String.format("指定的dataProcessor[%s]处理失败,原因[%s]", processor.getName(), ret.getMsg()));
            }
        }

        return HandlerResult.success(ProcessorHandler.class.getName());
    }
}
