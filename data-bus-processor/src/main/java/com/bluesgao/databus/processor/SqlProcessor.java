package com.bluesgao.databus.processor;

import com.alibaba.druid.util.JdbcUtils;
import com.bluesgao.databus.database.JdbcBuilder;
import com.bluesgao.databus.database.JdbcProps;
import com.bluesgao.databus.plugin.common.EventType;
import com.bluesgao.databus.plugin.common.JdbcCfgConstants;
import com.bluesgao.databus.plugin.common.ModeType;
import com.bluesgao.databus.plugin.processor.DataProcessor;
import com.bluesgao.databus.plugin.processor.DataProcessorResult;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

/**
 * @ClassName：SqlProcessor
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class SqlProcessor implements DataProcessor {

    @Override
    public DataProcessorResult process(DataProcessorParam params, String event, Map<String, Object> data) {
        if (params.getMode().equalsIgnoreCase(ModeType.SQL.getType())) {
            JdbcProps jdbcProps = new JdbcProps();
            jdbcProps.setDriverClassName(params.getCfg().get(JdbcCfgConstants.driverClassName));
            jdbcProps.setUrl(params.getCfg().get(JdbcCfgConstants.url));
            jdbcProps.setUsername(params.getCfg().get(JdbcCfgConstants.username));
            jdbcProps.setPassword(params.getCfg().get(JdbcCfgConstants.password));
            DataSource dataSource = JdbcBuilder.build(jdbcProps);
            if (event.equalsIgnoreCase(EventType.INSERT.getEvent()) || event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
                try {
                    String sql = upsertSql(params.getCfg().get(JdbcCfgConstants.table), data);
                    if (sql == null) {
                        return DataProcessorResult.fail("upsertSql生成失败");
                    }
                    JdbcUtils.execute(dataSource, sql, Collections.emptyList());
                    return DataProcessorResult.success();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return DataProcessorResult.fail(String.format("数据处理方式[%s]不支持", params.getMode()));
        }
        return DataProcessorResult.fail("");
    }

    @Override
    public String getName() {
        return SqlProcessor.class.getCanonicalName();
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
                //sql.append(value);

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
