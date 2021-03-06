package com.wyyt.databus.app.binlog;


import cn.hutool.core.collection.CollectionUtil;
import com.wwyt.databus.core.binlog.Binlog;
import com.wwyt.databus.core.binlog.BinlogWrapper;
import com.wwyt.databus.core.handler.HandlerChain;
import com.wwyt.databus.core.rule.RuleCfgHolder;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Data
@Component
public class BinlogDispatcher {
    @Resource
    private Executor handlerExecutor;

    public void dispatch(List<BinlogWrapper> binlogWrappers) {
        for (BinlogWrapper binlogWrapper : binlogWrappers) {
            Binlog binlog = binlogWrapper.getBinlog();
            List<RuleCfg> ruleCfgList = RuleCfgHolder.getRuleCfg(binlog.getDatabase(), binlog.getTable());
            if (CollectionUtil.isNotEmpty(ruleCfgList)) {
                handlerExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (RuleCfg ruleCfg : ruleCfgList) {
                            HandlerChain.process(binlogWrapper, ruleCfg);
                        }
                    }
                });
            }
        }
    }

}
