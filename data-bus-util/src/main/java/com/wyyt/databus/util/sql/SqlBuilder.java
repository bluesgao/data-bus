package com.wyyt.databus.util.sql;

import java.util.Map;

public class SqlBuilder {
    public static String insert(SqlEntity entity) {
        if (entity == null || entity.getTableName() == null || entity.getTableName().length() == 0
                || entity.getFieldAndValues() == null || entity.getFieldAndValues().size() == 0) {
            return null;
        }
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
                if (value != null) {
                    valuesPart.append("'" + value + "'");
                } else {
                    valuesPart.append(value);
                }
            }
        }
        if (!entity.getUuidAsPk()) {
            sql.append("INSERT INTO ")//
                    .append(entity.getTableName()).append(" (").append(fieldsPart).append(") VALUES (")//
                    .append(valuesPart.toString()).append(")");
        } else {
            sql.append("INSERT INTO ")//
                    .append(entity.getTableName()).append(" ( id, ").append(fieldsPart).append(") VALUES ( UUID_SHORT(), ")//
                    .append(valuesPart.toString()).append(")");
        }

        return sql.toString();
    }

    public static String upsert(SqlEntity entity) {

        String insertSql = SqlBuilder.insert(entity);
        if (insertSql == null || insertSql.length() == 0) {
            return null;
        }

        //update
        StringBuilder updateSql = new StringBuilder(" ON DUPLICATE KEY UPDATE ");
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : entity.getFieldAndValues().entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            if (field != null && field.length() > 0) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    // 非第一个参数，追加逗号
                    updateSql.append(", ");
                }
                updateSql.append(field);
                updateSql.append(" = ");
                if (value != null) {
                    updateSql.append("'" + value + "'");
                } else {
                    updateSql.append(value);
                }
            }
        }
        return insertSql + updateSql.toString();
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
                if (value != null) {
                    sql.append(field).append(" = ").append("'" + value + "'");
                } else {
                    sql.append(field).append(" = ").append(value);
                }
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

    public static String delete(SqlEntity entity) {
        if (entity == null || entity.getTableName() == null || entity.getTableName().length() == 0
                || entity.getWhereAndValues() == null || entity.getWhereAndValues().size() == 0) {
            return null;
        }

        //DELETE FROM runoob_tbl WHERE runoob_id=3 and name='test';
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(entity.getTableName());
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
