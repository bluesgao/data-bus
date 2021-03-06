package com.wyyt.databus.fetcher;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.wwyt.databus.plugin.common.constants.HttpCfgConstants;
import com.wwyt.databus.plugin.fetcher.DataFetcher;
import com.wwyt.databus.plugin.fetcher.DataFetcherResult;
import com.wyyt.databus.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
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
        String errMsg = "";
        try {
            String url = params.get(HttpCfgConstants.url).toString();
            Map<String, Object> postMap = getPostParamValues(data, (List<String>) params.get(HttpCfgConstants.biz_fields));
            String ret = HttpUtils.postJson(url, JSONUtils.toJSONString(postMap), null);
            if (ret != null && ret.length() > 0) {
                Map<String, Object> resultMap = JSON.parseObject(ret, Map.class);
                return DataFetcherResult.success(resultMap);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("HttpFetcher HttpUtils.postJson err:{}", e);
            errMsg = e.getMessage();
        }
        return DataFetcherResult.fail(errMsg);
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
        } else if (Objects.isNull(params.get(HttpCfgConstants.biz_fields))) {
            err.append("biz_fields为空;");
        }
        return err.toString();
    }

    private Map<String, Object> getPostParamValues(Map<String, Object> data, List<String> paramFields) {
        Map<String, Object> kvMap = new HashMap<>(8);
        for (String field : paramFields) {
            Object value = data.get(field);
            kvMap.put(field, value);
        }
        return kvMap;
    }
}
