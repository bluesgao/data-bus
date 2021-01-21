package com.wyyt.databus.processor;

import com.alibaba.druid.support.json.JSONUtils;
import com.wwyt.databus.plugin.common.constants.HttpCfgConstants;
import com.wwyt.databus.plugin.processor.DataProcessor;
import com.wwyt.databus.plugin.processor.DataProcessorResult;
import com.wyyt.databus.util.BeanMapUtils;
import com.wyyt.databus.util.HttpUtils;
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
        log.info("HttpProcessor ****开始处理****");
        if (params != null && params.size() > 0) {
            //进行参数验证
            String checkResult = checkParams(params);
            if (checkResult != null && checkResult.length() > 0) {
                return DataProcessorResult.fail(checkResult);
            }
        }

        try {
            String url = params.get(HttpCfgConstants.url).toString();
            Map<String, Object> postMap = BeanMapUtils.beanToMap(params.get(HttpCfgConstants.biz_fields));
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
        } else if (Objects.isNull(params.get(HttpCfgConstants.biz_fields))) {
            err.append("params为空;");
        }
        return err.toString();
    }
}
