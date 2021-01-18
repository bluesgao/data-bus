package com.bluesgao.databus.plugin.processor;

import java.util.Map;

public interface DataProcessor {
    DataProcessorResult process(DataProcessorParam param, String event, Map<String, Object> data);

    String getName();
}
