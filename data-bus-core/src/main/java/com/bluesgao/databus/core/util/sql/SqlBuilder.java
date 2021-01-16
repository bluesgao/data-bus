package com.bluesgao.databus.core.util.sql;

import java.util.Map;

public class SqlBuilder {
    public static String insert(SqlEntity entity) {

        final StringBuilder fieldsPart = new StringBuilder();
        final StringBuilder valuesPart = new StringBuilder();
        final StringBuilder sql = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : entity.getFieldAndValues().entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            if (field != null && field.length() > 0) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    // 非第一个参数，追加逗号
                    fieldsPart.append(", ");
                    valuesPart.append(", ");
                }
                fieldsPart.append(field);
                valuesPart.append(value);
            }
        }
        sql.append("INSERT INTO ")//
                .append(entity.getTableName()).append(" (").append(fieldsPart).append(") VALUES (")//
                .append(valuesPart.toString()).append(")");

        return sql.toString();
    }

    public static String update(SqlEntity entity) {

        final StringBuilder sql = new StringBuilder();

        sql.append("UPDATE ").append(entity.getTableName()).append(" SET ");
        boolean isFirst = true;
        //set
        for (Map.Entry<String, Object> entry : entity.getFieldAndValues().entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            if (field != null && field.length() > 0) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    // 非第一个参数，追加逗号
                    sql.append(", ");
                }
                sql.append(field).append(" = ").append(value);
            }
        }

        //where
        boolean isFirstWhere = true;
        for (Map.Entry<String, Object> entry : entity.getWhereAndValues().entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            if (field != null && field.length() > 0) {
                if (isFirstWhere) {
                    // 第一个条件，追加where
                    sql.append(" WHERE ");
                    isFirstWhere = false;
                } else {
                    // 非第一个条件，追加and
                    sql.append(" AND ");
                }
                sql.append(field).append(" = ").append(value);
            }
        }
        return sql.toString();
    }
}
