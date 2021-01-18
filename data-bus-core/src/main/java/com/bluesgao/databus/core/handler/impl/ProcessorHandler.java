package com.bluesgao.databus.core.handler.impl;

import com.bluesgao.databus.core.binlog.BinlogWrapper;
import com.bluesgao.databus.core.handler.HandlerResult;
import com.bluesgao.databus.core.handler.RuleHandler;
import com.bluesgao.databus.core.plugin.DataProcessorManager;
import com.bluesgao.databus.core.rule.entity.Processor;
import com.bluesgao.databus.core.rule.entity.RuleCfg;
import com.bluesgao.databus.plugin.processor.DataProcessor;
import com.bluesgao.databus.plugin.processor.DataProcessorResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessorHandler implements RuleHandler {

    @Override
    public HandlerResult handle(BinlogWrapper binlogWrapper, RuleCfg ruleCfg) {
        for (Processor processor : ruleCfg.getProcessor()) {
            DataProcessor dataProcessor = DataProcessorManager.get(processor.getName());
            if (dataProcessor == null) {
                //未找到指定的dataProcessor
                return HandlerResult.fail(ProcessorHandler.class.getName(), String.format("未找到指定的dataProcessor[%s]", dataProcessor.getName()));
            }
            DataProcessorResult ret = dataProcessor.process(processor.getParams(), binlogWrapper.getBinlog().getType(), RuleHandler.getDataFromBinlog(binlogWrapper.getBinlog()));
            if (ret == null || !ret.getSuccess()) {
                //指定的dataProcessor处理失败
                return HandlerResult.fail(ProcessorHandler.class.getName(), String.format("指定的dataProcessor[%s]处理失败,原因[%s]", dataProcessor.getName(), ret.getMsg()));
            }
        }

        return HandlerResult.success(ProcessorHandler.class.getName());
    }
}
