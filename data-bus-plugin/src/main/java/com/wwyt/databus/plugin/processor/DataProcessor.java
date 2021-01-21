package com.wwyt.databus.plugin.processor;

import java.util.Map;

public interface DataProcessor {
    DataProcessorResult process(Map<String, Object> params, String event, Map<String, Object> data);

    String getName();
}
