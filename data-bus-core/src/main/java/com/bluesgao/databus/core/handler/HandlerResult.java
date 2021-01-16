package com.bluesgao.databus.core.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ToString
public class HandlerResult implements Serializable {
    private String handler;
    private Boolean success;
    private String msg;

    private HandlerResult() {
    }

    public static HandlerResult success(String handler) {
        return new HandlerResult(handler, true, "");
    }

    public static HandlerResult fail(String handler, String msg) {
        return new HandlerResult(handler, false, msg);
    }
}
