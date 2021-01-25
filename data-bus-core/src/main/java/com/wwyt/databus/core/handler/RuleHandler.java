package com.wwyt.databus.core.handler;

import com.wwyt.databus.core.binlog.BinlogItemWrapper;
import com.wwyt.databus.core.rule.entity.RuleCfg;

public interface RuleHandler {
    HandlerResult handle(BinlogItemWrapper binlogItemWrapper, RuleCfg ruleCfg);
}
