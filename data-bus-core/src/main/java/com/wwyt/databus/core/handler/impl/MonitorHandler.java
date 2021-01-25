package com.wwyt.databus.core.handler.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.wwyt.databus.core.binlog.BinlogItem;
import com.wwyt.databus.core.binlog.BinlogItemWrapper;
import com.wwyt.databus.core.handler.HandlerResult;
import com.wwyt.databus.core.handler.RuleHandler;
import com.wwyt.databus.core.rule.entity.Monitor;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import com.wwyt.databus.plugin.common.enums.EventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class MonitorHandler implements RuleHandler {

    @Override
    public HandlerResult handle(BinlogItemWrapper binlogItemWrapper, RuleCfg ruleCfg) {
        //监控的表，字段，事件
        log.info("监控过滤-处理器：binlogItemWrapper:{},ruleCfg:{}", binlogItemWrapper, ruleCfg);
        //判断表是否是需要监控的,只要有一个符合条件就表示这个schema和table需要处理
        for (Monitor monitor : ruleCfg.getMonitor()) {
            if (monitor.getSchema().equals(binlogItemWrapper.getBinlogItem().getDatabase()) && monitor.getTable().equals(binlogItemWrapper.getBinlogItem().getTable())) {
                if (monitor.getEvent().contains(binlogItemWrapper.getBinlogItem().getType().toLowerCase())) {
                    boolean flag = checkField(binlogItemWrapper.getBinlogItem(), monitor);
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
    private boolean checkField(BinlogItem binlog, Monitor monitor) {
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

    private List<String> getAllFieldName(Map<String, String> data) {
        List<String> fields = new ArrayList<>();
        for (String key : data.keySet()) {
            fields.add(key);
        }
        return fields;
    }
}
