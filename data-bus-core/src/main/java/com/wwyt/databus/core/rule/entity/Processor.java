package com.wwyt.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
public class Processor implements Serializable {
    private String name;
    private String desc;
    private Map<String, Object> params;
}
