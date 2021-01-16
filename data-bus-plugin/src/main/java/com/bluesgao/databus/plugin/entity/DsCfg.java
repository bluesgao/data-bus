package com.bluesgao.databus.plugin.entity;

import java.io.Serializable;

public class DsCfg implements Serializable {
    private String type;
    private MySql mySql;
    private Redis redis;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MySql getMySql() {
        return mySql;
    }

    public void setMySql(MySql mySql) {
        this.mySql = mySql;
    }

    public Redis getRedis() {
        return redis;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }
}
