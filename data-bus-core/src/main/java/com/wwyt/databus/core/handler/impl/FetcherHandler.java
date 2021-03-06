package com.wwyt.databus.core.handler.impl;

import com.wwyt.databus.core.binlog.BinlogItem;
import com.wwyt.databus.core.binlog.BinlogItemWrapper;
import com.wwyt.databus.core.handler.HandlerResult;
import com.wwyt.databus.core.handler.RuleHandler;
import com.wwyt.databus.core.plugin.DataFetcherManager;
import com.wwyt.databus.core.rule.entity.Fetcher;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import com.wwyt.databus.plugin.fetcher.DataFetcher;
import com.wwyt.databus.plugin.fetcher.DataFetcherResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FetcherHandler implements RuleHandler {

    @Override
    public HandlerResult handle(BinlogItemWrapper binlogItemWrapper, RuleCfg ruleCfg) {
        //获取需要处理的数据
        log.info("数据获取-处理器：binlogItemWrapper:{},ruleCfg:{}", binlogItemWrapper, ruleCfg);

        Fetcher fetcher = ruleCfg.getFetcher();
        Map<String, Object> fetchedData = new HashMap<>();
        if (fetcher != null && !StringUtils.isEmpty(fetcher.getName())) {
            DataFetcher dataFetcher = DataFetcherManager.get(fetcher.getName());
            if (dataFetcher == null) {
                //未找到指定的dataFetcher
                return HandlerResult.fail(FetcherHandler.class.getName(), String.format("未找到指定的dataFetcher[%s]", fetcher.getName()));
            }
            DataFetcherResult ret = dataFetcher.fetch(ruleCfg.getFetcher().getParams(), binlogItemWrapper.getBinlogItem().getType(), getDataFromBinlog(binlogItemWrapper.getBinlogItem()));
            if (ret == null || !ret.getSuccess() ||
                    ret.getData() == null || ret.getData().size() == 0) {
                //自定的dataFetcher获取数据为空
                return HandlerResult.fail(FetcherHandler.class.getName(),
                        String.format("自定的dataFetcher[%s]获取数据为空,原因[%s]", fetcher.getName(), ret.getMsg()));
            }
            fetchedData = ret.getData();
        } else {
            //默认是取binlog中的data
            fetchedData = getDataFromBinlog(binlogItemWrapper.getBinlogItem());
        }

        binlogItemWrapper.setData(fetchedData);

        return HandlerResult.success(FetcherHandler.class.getName());
    }

    /**
     * 从binlog中获取data
     *
     * @param binlogItem
     * @return
     */
    private Map<String, Object> getDataFromBinlog(BinlogItem binlogItem) {
        Map<String, Object> resultMap = new HashMap<>(16);
        for (String key : binlogItem.getData().keySet()) {
            resultMap.put(key, binlogItem.getData().get(key));
        }
        return resultMap;
    }
}
