package com.bluesgao.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Processor implements Serializable {
    private String name;
    private String desc;
}
