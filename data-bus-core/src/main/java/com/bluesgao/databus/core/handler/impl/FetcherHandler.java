package com.bluesgao.databus.core.handler.impl;

import com.bluesgao.databus.core.binlog.BinlogWrapper;
import com.bluesgao.databus.core.handler.HandlerResult;
import com.bluesgao.databus.core.handler.RuleHandler;
import com.bluesgao.databus.core.plugin.DataFetcherManager;
import com.bluesgao.databus.core.rule.entity.Fetcher;
import com.bluesgao.databus.core.rule.entity.RuleCfg;
import com.bluesgao.databus.plugin.fetcher.DataFetcher;
import com.bluesgao.databus.plugin.fetcher.DataFetcherResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FetcherHandler implements RuleHandler {

    @Override
    public HandlerResult handle(BinlogWrapper binlogWrapper, RuleCfg ruleCfg) {
        //获取需要处理的数据
        log.info("数据获取-处理器：binlogWrapper:{},ruleCfg:{}", binlogWrapper, ruleCfg);

        Fetcher fetcher = ruleCfg.getFetcher();
        Map<String, Object> fetchedData = new HashMap<>();
        if (fetcher != null && !StringUtils.isEmpty(fetcher.getName())) {
            DataFetcher dataFetcher = DataFetcherManager.get(fetcher.getName());
            if (dataFetcher == null) {
                //未找到指定的dataFetcher
                return HandlerResult.fail(FetcherHandler.class.getName(), String.format("未找到指定的dataFetcher[%s]", fetcher.getName()));
            }
            DataFetcherResult ret = dataFetcher.fetch(ruleCfg.getFetcher().getParams(), binlogWrapper.getBinlog().getType(), RuleHandler.getDataFromBinlog(binlogWrapper.getBinlog()));
            if (ret == null || !ret.getSuccess() ||
                    ret.getData() == null || ret.getData().size() == 0) {
                //自定的dataFetcher获取数据为空
                return HandlerResult.fail(FetcherHandler.class.getName(),
                        String.format("自定的dataFetcher[%s]获取数据为空,原因[%s]", fetcher.getName(), ret.getMsg()));
            }
            fetchedData = ret.getData();
        } else {
            //默认是取binlog中的data
            fetchedData = RuleHandler.getDataFromBinlog(binlogWrapper.getBinlog());
        }

        binlogWrapper.setData(fetchedData);

        return HandlerResult.success(FetcherHandler.class.getName());
    }
}
