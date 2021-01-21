package com.wwyt.databus.core.rule;


import com.wwyt.databus.core.rule.entity.Monitor;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RuleCfgHolder {

    private static final ConcurrentHashMap<String, List<RuleCfg>> HOLDER = new ConcurrentHashMap<>(32);

    public static synchronized void putRuleCfg(RuleCfg ruleCfg) {
        for (Monitor monitor : ruleCfg.getMonitor()) {
            String key = genRuleCfgKey(monitor.getSchema(), monitor.getTable());
            if (HOLDER.containsKey(key)) {
                HOLDER.get(key).add(ruleCfg);
            } else {
                List<RuleCfg> list = new ArrayList<>();
                list.add(ruleCfg);
                HOLDER.put(key, list);
            }
        }
    }

    public static List<RuleCfg> getRuleCfg(String schema, String table) {
        return HOLDER.get(genRuleCfgKey(schema, table));
    }

    private static String genRuleCfgKey(String schema, String table) {
        return String.format("%s:%s", schema, table);
    }
}
