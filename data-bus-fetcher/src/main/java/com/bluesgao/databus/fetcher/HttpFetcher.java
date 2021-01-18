package com.bluesgao.databus.fetcher;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.bluesgao.databus.plugin.common.HttpCfgConstants;
import com.bluesgao.databus.plugin.fetcher.DataFetcher;
import com.bluesgao.databus.plugin.fetcher.DataFetcherResult;
import com.bluesgao.databus.util.BeanMapUtils;
import com.bluesgao.databus.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName：HttpFetcher
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class HttpFetcher implements DataFetcher {

    @Override
    public DataFetcherResult fetch(Map<String, Object> params, String event, Map<String, Object> data) {

        if (params != null && params.size() > 0) {
            //进行参数验证
            String checkResult = checkParams(params);
            if (checkResult != null && checkResult.length() > 0) {
                return DataFetcherResult.fail(checkResult);
            }
        }

        //todo
        try {
            String url = params.get(HttpCfgConstants.url).toString();
            Map<String, Object> postMap = BeanMapUtils.beanToMap(params.get(HttpCfgConstants.params));
            String ret = HttpUtils.sendPostJson(url, JSONUtils.toJSONString(postMap), null);
            if (ret != null && ret.length() > 0) {
                Map<String, Object> resultMap = JSON.parseObject(ret, Map.class);
                return DataFetcherResult.success(resultMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataFetcherResult.fail("");
    }

    @Override
    public String getName() {
        return HttpFetcher.class.getCanonicalName();
    }

    private String checkParams(Map<String, Object> params) {
        StringBuilder err = new StringBuilder();
        if (Objects.isNull(params.get(HttpCfgConstants.protocol))) {
            err.append("protocol为空;");
        } else if (Objects.isNull(params.get(HttpCfgConstants.url))) {
            err.append("url为空;");
        } else if (Objects.isNull(params.get(HttpCfgConstants.params))) {
            err.append("params为空;");
        }
        return err.toString();
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
