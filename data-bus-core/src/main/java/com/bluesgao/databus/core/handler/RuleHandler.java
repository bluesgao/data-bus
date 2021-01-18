package com.bluesgao.databus.core.handler;

import com.bluesgao.databus.core.binlog.Binlog;
import com.bluesgao.databus.core.binlog.BinlogWrapper;
import com.bluesgao.databus.core.rule.entity.RuleCfg;

import java.util.HashMap;
import java.util.Map;

public interface RuleHandler {
    /**
     * 从binlog中获取data
     *
     * @param binlog
     * @return
     */
    public static Map<String, Object> getDataFromBinlog(Binlog binlog) {
        Map<String, Object> resultMap = new HashMap<>(16);
        for (Map<String, String> item : binlog.getData()) {
            resultMap.putAll(item);
        }
        return resultMap;
    }

    HandlerResult handle(BinlogWrapper binlogWrapper, RuleCfg ruleCfg);
}
