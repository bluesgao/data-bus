package com.bluesgao.databus.fetcher;

import com.alibaba.druid.util.JdbcUtils;
import com.bluesgao.databus.database.JdbcBuilder;
import com.bluesgao.databus.database.JdbcProps;
import com.bluesgao.databus.plugin.common.JdbcCfgConstants;
import com.bluesgao.databus.plugin.common.ModeType;
import com.bluesgao.databus.plugin.fetcher.DataFetcher;
import com.bluesgao.databus.plugin.fetcher.DataFetcherParam;
import com.bluesgao.databus.plugin.fetcher.DataFetcherResult;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：DefaultFetcher
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class HttpFetcher implements DataFetcher {

    public static void main(String[] args) {
        HttpFetcher protocolFetcher = new HttpFetcher();
        Map<String, Object> data = new HashMap<>(8);
        data.put("protocol_id", "215165519539209204");
        protocolFetcher.fetch(null, "update", data);

        System.out.println(protocolFetcher.getName());
    }

    @Override
    public DataFetcherResult fetch(DataFetcherParam params, String event, Map<String, Object> data) {
        if (params.getMode().equalsIgnoreCase(ModeType.HTTP.getType())) {
            //todo
        } else {
            return DataFetcherResult.fail(String.format("数据获取方式[%s]不支持", params.getMode()));
        }
        return DataFetcherResult.fail("");
    }

    @Override
    public String getName() {
        return HttpFetcher.class.getCanonicalName();
    }


    private List<Object> getQueryParams(Map<String, Object> data, String paramFields) {
        List<Object> queryParams = new ArrayList<>();
        String[] qrys = paramFields.split(",");
        for (int i = 0; i < qrys.length; i++) {
            Object value = data.get(qrys[i]);
            queryParams.add(value);
        }
        return queryParams;
    }
}
