package com.bluesgao.databus.plugin.common;

public enum ProtocolType {

    HTTP("HTTP", "http"),
    DUBBO("DUBBO", "dubbo"),
    PB("PB", "Protocol Buffer");

    private String type;
    private String name;

    ProtocolType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static ProtocolType get(String type) {
        for (ProtocolType item : ProtocolType.values()) {
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
