package com.wyyt.databus.util.sql;


import java.io.Serializable;
import java.util.Map;


public class SqlEntity implements Serializable {
    private String tableName;
    private Map<String, Object> fieldAndValues;
    private Map<String, Object> whereAndValues;
    private Boolean uuidAsPk;

    public SqlEntity(String tableName, Map<String, Object> fieldAndValues, Map<String, Object> whereAndValues) {
        this(tableName, fieldAndValues, whereAndValues, false);
    }

    public SqlEntity(String tableName, Map<String, Object> fieldAndValues, Map<String, Object> whereAndValues, Boolean uuidAsPk) {
        this.tableName = tableName;
        this.fieldAndValues = fieldAndValues;
        this.whereAndValues = whereAndValues;
        this.uuidAsPk = uuidAsPk;
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

    public Boolean getUuidAsPk() {
        return uuidAsPk;
    }

    public void setUuidAsPk(Boolean uuidAsPk) {
        this.uuidAsPk = uuidAsPk;
    }

    @Override
    public String toString() {
        return "SqlEntity{" +
                "tableName='" + tableName + '\'' +
                ", fieldAndValues=" + fieldAndValues +
                ", whereAndValues=" + whereAndValues +
                ", uuidAsPk=" + uuidAsPk +
                '}';
    }
}
