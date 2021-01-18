package com.bluesgao.databus.plugin.fetcher;


import java.io.Serializable;
import java.util.Map;

/**
 * 数据获取处理结果
 */
public class DataFetcherResult implements Serializable {
    private Boolean success;
    private String msg;
    private Map<String, Object> data;

    private DataFetcherResult() {
    }

    public DataFetcherResult(Boolean success, String msg, Map<String, Object> data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public static DataFetcherResult success(Map<String, Object> data) {
        return new DataFetcherResult(true, "", data);
    }

    public static DataFetcherResult fail(String msg) {
        return new DataFetcherResult(false, msg, null);
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
