package com.wwyt.databus.core.rule.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;


@ToString
@Getter
@Setter
public class Fetcher implements Serializable {
    private String name;
    private String desc;
    private Map<String, Object> params;
}
