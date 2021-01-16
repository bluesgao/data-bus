package com.bluesgao.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class Monitor implements Serializable {
    private String schema;
    private String table;
    private List<String> event;
    private List<String> field;
}
