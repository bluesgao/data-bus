package com.bluesgao.databus.plugin;

import java.util.Map;

public interface DataProcessor {
    boolean process(Map<String, String> data, String event);
    String getName();
}
