package com.bluesgao.databus.plugin.processor;

import java.util.Map;

public interface DataProcessor {
    DataProcessorResult process(Map<String, String> params, String event, Map<String, Object> data);

    String getName();
}
