package com.bluesgao.databus.processor;

import com.bluesgao.databus.plugin.common.ModeType;
import com.bluesgao.databus.plugin.processor.DataProcessor;
import com.bluesgao.databus.plugin.processor.DataProcessorResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @ClassName：HttpProcessor
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class HttpProcessor implements DataProcessor {

    @Override
    public DataProcessorResult process(DataProcessorParam params, String event, Map<String, Object> data) {
        if (params.getMode().equalsIgnoreCase(ModeType.HTTP.getType())) {
            //todo
        } else {
            return DataProcessorResult.fail(String.format("数据处理方式[%s]不支持", params.getMode()));
        }
        return DataProcessorResult.fail("");
    }

    @Override
    public String getName() {
        return HttpProcessor.class.getCanonicalName();
    }
}
