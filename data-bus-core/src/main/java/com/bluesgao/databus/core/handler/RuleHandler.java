package com.bluesgao.databus.core.handler;

import com.bluesgao.databus.core.binlog.BinlogWrapper;
import com.bluesgao.databus.core.rule.entity.RuleCfg;

public interface RuleHandler {
    HandlerResult handle(BinlogWrapper binlogWrapper, RuleCfg ruleCfg);
}
