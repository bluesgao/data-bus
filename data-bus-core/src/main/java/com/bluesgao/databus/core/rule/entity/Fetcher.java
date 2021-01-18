package com.bluesgao.databus.core.rule.entity;

import lombok.ToString;

import java.io.Serializable;
import java.util.Map;


@ToString
@lombok.Data
public class Fetcher implements Serializable {
    private String name;
    private String desc;
    private Map<String, String> params;
}
