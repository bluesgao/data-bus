package com.bluesgao.databus.util.sql;


import java.io.Serializable;
import java.util.Map;


public class SqlEntity implements Serializable {
    private String tableName;
    private Map<String, Object> fieldAndValues;
    private Map<String, Object> whereAndValues;

    public SqlEntity(String tableName, Map<String, Object> fieldAndValues, Map<String, Object> whereAndValues) {
        this.tableName = tableName;
        this.fieldAndValues = fieldAndValues;
        this.whereAndValues = whereAndValues;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, Object> getFieldAndValues() {
        return fieldAndValues;
    }

    public void setFieldAndValues(Map<String, Object> fieldAndValues) {
        this.fieldAndValues = fieldAndValues;
    }

    public Map<String, Object> getWhereAndValues() {
        return whereAndValues;
    }

    public void setWhereAndValues(Map<String, Object> whereAndValues) {
        this.whereAndValues = whereAndValues;
    }

    @Override
    public String toString() {
        return "SqlEntity{" +
                "tableName='" + tableName + '\'' +
                ", fieldAndValues=" + fieldAndValues +
                ", whereAndValues=" + whereAndValues +
                '}';
    }
}
