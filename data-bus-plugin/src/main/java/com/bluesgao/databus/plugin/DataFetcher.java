package com.bluesgao.databus.plugin;

import java.util.Map;

public interface DataFetcher {
    Map<String, String> fetch(Map<String, String> data, String event);
    String getName();
}
