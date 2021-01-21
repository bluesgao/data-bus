package com.wwyt.databus.plugin.fetcher;

import java.util.Map;

public interface DataFetcher {
    DataFetcherResult fetch(Map<String, Object> params, String event, Map<String, Object> data);

    String getName();
}
