package com.bluesgao.databus.core.common;

public enum ModeType {

    BINLOG("BINLOG", "binlog"),
    SQL("SQL", "sql");

    private String type;
    private String name;

    ModeType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static ModeType get(String type) {
        for (ModeType item : ModeType.values()) {
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
