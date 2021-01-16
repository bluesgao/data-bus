package com.bluesgao.databus.core.util.sql;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
public class SqlEntity implements Serializable {
    private String tableName;
    private Map<String, Object> fieldAndValues;
    private Map<String, Object> whereAndValues;
}
