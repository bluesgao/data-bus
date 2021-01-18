package com.bluesgao.databus.plugin.processor;


import java.io.Serializable;

/**
 * 数据处理结果
 */
public class DataProcessorResult implements Serializable {
    private Boolean success;
    private String msg;

    private DataProcessorResult() {
    }

    public DataProcessorResult(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public static DataProcessorResult success() {
        return new DataProcessorResult(true, "");
    }

    public static DataProcessorResult fail(String msg) {
        return new DataProcessorResult(false, msg);
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }
}
