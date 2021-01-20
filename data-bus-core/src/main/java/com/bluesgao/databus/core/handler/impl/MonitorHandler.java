package com.bluesgao.databus.core.handler.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bluesgao.databus.core.binlog.Binlog;
import com.bluesgao.databus.core.binlog.BinlogWrapper;
import com.bluesgao.databus.core.handler.HandlerResult;
import com.bluesgao.databus.core.handler.RuleHandler;
import com.bluesgao.databus.core.rule.entity.Monitor;
import com.bluesgao.databus.core.rule.entity.RuleCfg;
import com.bluesgao.databus.plugin.common.enums.EventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class MonitorHandler implements RuleHandler {

    @Override
    public HandlerResult handle(BinlogWrapper binlogWrapper, RuleCfg ruleCfg) {
        //监控的表，字段，事件
        log.info("监控过滤-处理器：binlogWrapper:{},ruleCfg:{}", binlogWrapper, ruleCfg);
        //判断表是否是需要监控的,只要有一个符合条件就表示这个schema和table需要处理
        for (Monitor monitor : ruleCfg.getMonitor()) {
            if (monitor.getSchema().equals(binlogWrapper.getBinlog().getDatabase()) && monitor.getTable().equals(binlogWrapper.getBinlog().getTable())) {
                if (monitor.getEvent().contains(binlogWrapper.getBinlog().getType().toLowerCase())) {
                    boolean flag = checkField(binlogWrapper.getBinlog(), monitor);
                    if (!flag) {
                        return HandlerResult.fail(MonitorHandler.class.getName(), "监控的表，事件匹配，但是字段不匹配");
                    } else {
                        return HandlerResult.success(MonitorHandler.class.getName());
                    }
                }
            }
        }
        return HandlerResult.fail(MonitorHandler.class.getName(), "没有匹配到需要监控的表、事件、字段");
    }

    /**
     * 新增、删除不关心字段
     * 更新关心字段
     *
     * @param binlog
     * @param monitor
     * @return
     */
    private boolean checkField(Binlog binlog, Monitor monitor) {
        boolean flag = false;
        if (binlog.getType().equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            if (CollectionUtils.isEmpty(monitor.getField())) {
                flag = true;
            }
            //求子集，从修改集合中获取
            if (CollectionUtil.containsAll(getAllFieldName(binlog.getOld()), monitor.getField())) {
                flag = true;
            }

        } else if (binlog.getType().equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            flag = true;
        } else if (binlog.getType().equalsIgnoreCase(EventType.DELETE.getEvent())) {
            flag = true;
        }
        return flag;
    }

    private List<String> getAllFieldName(List<Map<String, String>> data) {
        List<String> fields = new ArrayList<>();
        for (Map<String, String> field : data) {
            for (String key : field.keySet()) {
                fields.add(key);
            }
        }
        return fields;
    }
}
