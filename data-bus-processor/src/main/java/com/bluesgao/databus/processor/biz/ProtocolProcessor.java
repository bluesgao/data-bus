package com.bluesgao.databus.processor.biz;

import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.util.StringUtils;
import com.bluesgao.databus.plugin.common.EventType;
import com.bluesgao.databus.plugin.processor.DataProcessor;
import com.bluesgao.databus.plugin.processor.DataProcessorResult;
import com.bluesgao.databus.processor.datasource.JdbcBuilder;
import com.bluesgao.databus.processor.datasource.JdbcProps;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：ProtocolProcessor
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class ProtocolProcessor implements DataProcessor {

    public static void main(String[] args) {
        ProtocolProcessor processor = new ProtocolProcessor();
        Map<String, Object> data = new HashMap<>(8);
        data.put("protocol_id", "215165519539209204");
        processor.process(null, "update", data);

        System.out.println(processor.getName());
    }

    @Override
    public DataProcessorResult process(Map<String, String> params, String event, Map<String, Object> data) {
        if (event.equalsIgnoreCase(EventType.INSERT.getEvent()) || event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {

            /**
             * "url": "jdbc:mysql://gyl.mysql.dev.wyyt:6612/wyw_dev?tinyInt1isBit=false&transformedBitIsBoolean=false",
             * "driverClassName": "com.mysql.jdbc.Driver",
             * "username": "zyc",
             * "password": "XNtyEFrgMwR5DYtBEjBG",
             * "database": "wyw_dev"
             */
            JdbcProps jdbcProps = new JdbcProps();
            jdbcProps.setDriverClassName("com.mysql.jdbc.Driver");
            jdbcProps.setUrl("jdbc:mysql://gyl.mysql.dev.wyyt:6612/wyw_dev?tinyInt1isBit=false&transformedBitIsBoolean=false");
            jdbcProps.setUsername("zyc");
            jdbcProps.setPassword("XNtyEFrgMwR5DYtBEjBG");
            DataSource dataSource = JdbcBuilder.build(jdbcProps);

            List<Map<String, Object>> dataList = null;
            try {
                List<Object> updateParams = new ArrayList<>();
                updateParams.add(data.get("protocol_id"));
                String sql = upsertSql("mls_plan", data);
                if (sql == null) {
                    DataProcessorResult.fail("upsertSql生成失败");
                }
                JdbcUtils.execute(dataSource, sql, updateParams);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dataList != null && dataList.size() > 0) {
                return DataProcessorResult.success();
            }
        }
        return DataProcessorResult.fail("");
    }

    @Override
    public String getName() {
        return ProtocolProcessor.class.getCanonicalName();
    }

    public String upsertSql(String table, Map<String, Object> data) {
        if (StringUtils.isEmpty(table) || data == null || data.size() == 0) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        //组装sql insert into t_user(id,name) values(12,test);
        sql.append("insert into ").append(table).append(" (");

        StringBuilder values = new StringBuilder(" values(");
        int i = 0;
        for (String key : data.keySet()) {
            Object value = data.get(key);
            sql.append(key);
            values.append("'" + value + "'");
            if (i < data.keySet().size() - 1) {
                sql.append(",");
                values.append(",");
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
            sql.append(key);
            sql.append(" = ");
            sql.append("'" + value + "'");
            if (j < data.keySet().size() - 1) {
                sql.append(" , ");
            }
            j++;
        }
        log.info("组装sql:{}", sql.toString());
        return sql.toString();
    }
}
