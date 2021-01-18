package com.bluesgao.databus.plugin.common;

public enum EventType {

    /**
     * 新增
     */
    INSERT("INSERT", "新增"),

    /**
     * 修改
     */
    UPDATE("UPDATE", "修改"),

    /**
     * 删除
     */
    DELETE("DELETE", "删除");

    private String event;
    private String desc;

    EventType(String event, String desc) {
        this.event = event;
        this.desc = desc;
    }

    public static EventType get(String event) {
        for (EventType item : EventType.values()) {
            if (item.getEvent().equalsIgnoreCase(event)) {
                return item;
            }
        }
        return null;
    }

    public String getEvent() {
        return event;
    }


}
