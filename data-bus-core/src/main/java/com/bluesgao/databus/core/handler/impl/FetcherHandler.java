package com.bluesgao.databus.core.handler.impl;

import com.bluesgao.databus.core.binlog.Binlog;
import com.bluesgao.databus.core.binlog.BinlogWrapper;
import com.bluesgao.databus.core.plugin.DataFetcherManager;
import com.bluesgao.databus.core.rule.entity.Fetcher;
import com.bluesgao.databus.core.rule.entity.RuleCfg;
import com.bluesgao.databus.core.handler.HandlerResult;
import com.bluesgao.databus.core.handler.RuleHandler;
import com.bluesgao.databus.plugin.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FetcherHandler implements RuleHandler {

    @Override
    public HandlerResult handle(BinlogWrapper binlogWrapper, RuleCfg ruleCfg) {
        //获取需要处理的数据
        Fetcher fetcher = ruleCfg.getFetcher();
        Map<String, String> fetchedData = new HashMap<>();
        if (fetcher != null && !StringUtils.isEmpty(fetcher.getName())) {
            DataFetcher dataFetcher = DataFetcherManager.get(fetcher.getName());
            if (dataFetcher == null) {
                //未找到指定的dataFetcher
                return HandlerResult.fail(FetcherHandler.class.getName(),String.format("未找到指定的dataFetcher[%s]", fetcher.getName()));
            }
            fetchedData = dataFetcher.fetch(binlogWrapper.getData(), binlogWrapper.getBinlog().getType());
            if (fetchedData == null || fetchedData.size() == 0) {
                //自定的dataFetcher获取数据为空
                return HandlerResult.fail(FetcherHandler.class.getName(),String.format("自定的dataFetcher[%s]获取数据为空", fetcher.getName()));
            }
        } else {
            //默认是取binlog中的data
            fetchedData = getDataFromBinlog(binlogWrapper.getBinlog());
        }

        binlogWrapper.setData(fetchedData);

        return HandlerResult.success(FetcherHandler.class.getName());
    }

    /**
     * 宠binlog中获取data
     *
     * @param binlog
     * @return
     */
    private Map<String, String> getDataFromBinlog(Binlog binlog) {
        Map<String, String> resultMap = new HashMap<>(16);
        for (Map<String, String> item : binlog.getData()) {
            resultMap.putAll(item);
        }
        return resultMap;
    }
}
