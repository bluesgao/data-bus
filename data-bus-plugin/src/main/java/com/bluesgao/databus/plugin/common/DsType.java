package com.bluesgao.databus.plugin.common;

public enum DsType {

    MYSQL("MYSQL", "mysql"),
    REDIS("REDIS", "redis"),
    CALLBACK("CALLBACK", "callback"),
    ES("ES", "elasticsearch");

    private String type;
    private String name;

    DsType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static DsType get(String type) {
        for (DsType item : DsType.values()) {
            if (item.getType().equalsIgnoreCase(type)) {
                return item;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }


}
