package com.bluesgao.databus.plugin.fetcher;

import java.util.Map;

public interface DataFetcher {
    DataFetcherResult fetch(Map<String, String> params, String event, Map<String, Object> data);

    String getName();
}
