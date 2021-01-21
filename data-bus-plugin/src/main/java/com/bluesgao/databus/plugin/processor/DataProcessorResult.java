package com.bluesgao.databus.plugin.processor;


import java.io.Serializable;

/**
 * 数据处理结果
 */
public class DataProcessorResult implements Serializable {
    private Boolean success;
    private String msg;
    private Object data;

    private DataProcessorResult() {
    }

    public DataProcessorResult(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public DataProcessorResult(Boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public static DataProcessorResult success() {
        return new DataProcessorResult(true, "");
    }

    public static DataProcessorResult success(Object data) {
        return new DataProcessorResult(true, "", data);
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

    @Override
    public String toString() {
        return "DataProcessorResult{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
