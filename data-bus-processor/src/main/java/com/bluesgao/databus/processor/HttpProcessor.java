package com.bluesgao.databus.processor;

import com.alibaba.druid.support.json.JSONUtils;
import com.bluesgao.databus.plugin.common.HttpCfgConstants;
import com.bluesgao.databus.plugin.processor.DataProcessor;
import com.bluesgao.databus.plugin.processor.DataProcessorResult;
import com.bluesgao.databus.util.BeanMapUtils;
import com.bluesgao.databus.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @ClassName：HttpProcessor
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class HttpProcessor implements DataProcessor {

    @Override
    public DataProcessorResult process(Map<String, Object> params, String event, Map<String, Object> data) {
        if (params != null && params.size() > 0) {
            //进行参数验证
            String checkResult = checkParams(params);
            if (checkResult != null && checkResult.length() > 0) {
                return DataProcessorResult.fail(checkResult);
            }
        }

        try {
            String url = params.get(HttpCfgConstants.url).toString();
            Map<String, Object> postMap = BeanMapUtils.beanToMap(params.get(HttpCfgConstants.params));
            String ret = HttpUtils.postJson(url, JSONUtils.toJSONString(postMap), null);
            if (ret != null && ret.length() > 0) {
                return DataProcessorResult.success(ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataProcessorResult.fail("");
    }

    @Override
    public String getName() {
        return HttpProcessor.class.getCanonicalName();
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
}
