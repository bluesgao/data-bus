package com.bluesgao.databus.plugin.processor;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName：DataProcessorParam
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 17:02
 **/
public class DataProcessorParam implements Serializable {
    private String mode;
    private Map<String, String> cfg;

    public DataProcessorParam(String mode, Map<String, String> cfg) {
        this.mode = mode;
        this.cfg = cfg;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<String, String> getCfg() {
        return cfg;
    }

    public void setCfg(Map<String, String> cfg) {
        this.cfg = cfg;
    }
}
