package com.bluesgao.databus.processor;

import com.alibaba.druid.util.JdbcUtils;
import com.bluesgao.databus.ds.JdbcBuilder;
import com.bluesgao.databus.ds.JdbcProps;
import com.bluesgao.databus.plugin.common.constants.JdbcCfgConstants;
import com.bluesgao.databus.plugin.common.enums.EventType;
import com.bluesgao.databus.plugin.processor.DataProcessor;
import com.bluesgao.databus.plugin.processor.DataProcessorResult;
import com.bluesgao.databus.util.sql.SqlBuilder;
import com.bluesgao.databus.util.sql.SqlEntity;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName：JdbcProcessor
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class JdbcProcessor implements DataProcessor {

    @Override
    public DataProcessorResult process(Map<String, Object> params, String event, Map<String, Object> data) {
        log.info("JdbcProcessor ****开始处理****");

        if (params != null && params.size() > 0) {
            //进行参数验证
            String checkResult = checkParams(params);
            if (checkResult != null && checkResult.length() > 0) {
                return DataProcessorResult.fail(checkResult);
            }
        }

        JdbcProps jdbcProps = new JdbcProps();
        jdbcProps.setDriverClassName(params.get(JdbcCfgConstants.driverClassName).toString());
        jdbcProps.setUrl(params.get(JdbcCfgConstants.url).toString());
        jdbcProps.setUsername(params.get(JdbcCfgConstants.username).toString());
        jdbcProps.setPassword(params.get(JdbcCfgConstants.password).toString());

        DataSource dataSource = JdbcBuilder.build(jdbcProps);

        String table = params.get(JdbcCfgConstants.biz_table).toString();
        if (event.equalsIgnoreCase(EventType.INSERT.getEvent()) || event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            log.info("INSERT OR UPDATE 处理中");
            try {
                //String sql = upsertSql(table, data);
                SqlEntity entity = new SqlEntity(table, data, null);
                String sql = SqlBuilder.upsert(entity);
                if (sql == null) {
                    return DataProcessorResult.fail("upsertSql生成失败");
                }
                JdbcUtils.execute(dataSource, sql);
                return DataProcessorResult.success();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (event.equalsIgnoreCase(EventType.DELETE.getEvent())) {
            log.info("DELETE 处理中");
            try {
                //String sql = deleteSql(table, getParamValue(data, (List<String>) params.get(JdbcCfgConstants.biz_fields)));

                Map<String, Object> whereFieldAndVaules = new HashMap<>(8);
                for (String field : (List<String>) params.get(JdbcCfgConstants.biz_fields)) {
                    whereFieldAndVaules.put(field, data.get(field));
                }

                SqlEntity entity = new SqlEntity(table, null, whereFieldAndVaules);
                String sql = SqlBuilder.delete(entity);
                if (sql == null) {
                    return DataProcessorResult.fail("deleteSql生成失败");
                }
                JdbcUtils.execute(dataSource, sql);
                return DataProcessorResult.success();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return DataProcessorResult.fail("");
    }

    @Override
    public String getName() {
        return JdbcProcessor.class.getCanonicalName();
    }

    /**
     * 参数检查
     *
     * @param params
     * @return
     */
    private String checkParams(Map<String, Object> params) {
        StringBuilder err = new StringBuilder();
        if (Objects.isNull(params.get(JdbcCfgConstants.driverClassName))) {
            err.append("driverClassName为空;");
        } else if (Objects.isNull(params.get(JdbcCfgConstants.url))) {
            err.append("url为空;");
        } else if (Objects.isNull(params.get(JdbcCfgConstants.username))) {
            err.append("username为空;");
        } else if (Objects.isNull(params.get(JdbcCfgConstants.password))) {
            err.append("password为空;");
        } else if (Objects.isNull(params.get(JdbcCfgConstants.biz_table))) {
            err.append("biz_table为空;");
        } /*else if (Objects.isNull(params.get(JdbcCfgConstants.biz_fields)) ||
                !(params.get(JdbcCfgConstants.biz_fields) instanceof List)) {
            err.append("biz_fields为空;");
        }*/
        return err.toString();
    }

    private Map<String, Object> getParamValue(Map<String, Object> data, List<String> paramFields) {
        Map<String, Object> fieldValues = new HashMap<>(8);
        for (String field : paramFields) {
            Object value = data.get(field);
            fieldValues.put(field, value);
        }
        return fieldValues;
    }

    private String deleteSql(String table, Map<String, Object> fieldValues) {
        if (table == null || table.length() == 0 || fieldValues == null || fieldValues.size() == 0) {
            return null;
        }

        //DELETE FROM runoob_tbl WHERE runoob_id=3 and name='test';
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(table);
        sql.append(" WHERE ");
        int i = 0;
        for (String key : fieldValues.keySet()) {
            sql.append(key);
            sql.append(" = ");
            sql.append(fieldValues.get(key));
            if (i <= fieldValues.keySet().size() - 1) {
                sql.append(" AND ");
            }
            i++;
        }
        sql.append(" ; ");

        return sql.toString();
    }

    private String upsertSql(String table, Map<String, Object> data) {
        if (table == null || table.length() == 0 || data == null || data.size() == 0) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        //组装sql insert into t_user(id,name) values(12,test);
        sql.append("insert into ").append(table).append(" (");

        StringBuilder values = new StringBuilder(" values(");
        int i = 0;
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (value != null) {
                sql.append(key);
                values.append("'" + value + "'");
                //values.append(value);

                if (i < data.keySet().size() - 1) {
                    sql.append(", ");
                    values.append(", ");
                }
            }
            i++;
        }


        sql.append(") ");
        values.append(")");
        sql.append(values);

        //update
        int j = 0;
        sql.append(" ON DUPLICATE KEY UPDATE ");
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (value != null) {
                sql.append(key);
                sql.append(" = ");
                sql.append("'" + value + "'");
                //biz_sql.append(value);

                if (j < data.keySet().size() - 1) {
                    sql.append(" , ");
                }
            }
            j++;
        }
        log.info("组装sql:{}", sql.toString());
        return sql.toString();
    }
}
