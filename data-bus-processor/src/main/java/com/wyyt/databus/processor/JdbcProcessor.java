package com.wyyt.databus.processor;

import com.alibaba.druid.util.JdbcUtils;
import com.wwyt.databus.plugin.common.constants.JdbcCfgConstants;
import com.wwyt.databus.plugin.common.enums.EventType;
import com.wwyt.databus.plugin.processor.DataProcessor;
import com.wwyt.databus.plugin.processor.DataProcessorResult;
import com.wyyt.databus.ds.JdbcBuilder;
import com.wyyt.databus.ds.JdbcProps;
import com.wyyt.databus.util.sql.SqlBuilder;
import com.wyyt.databus.util.sql.SqlEntity;
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

        DataSource dataSource = getDataSource(params);
        if (dataSource == null) {
            return DataProcessorResult.fail("获取数据连接conn错误");
        }

        String table = params.get(JdbcCfgConstants.biz_table).toString();
        if (event.equalsIgnoreCase(EventType.INSERT.getEvent()) || event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            log.info("INSERT OR UPDATE 处理中");
            try {
                //String sql = upsertSql(table, data);
                SqlEntity entity = new SqlEntity(table, data, null);
                String sql = SqlBuilder.upsert(entity);
                log.info("INSERT OR UPDATE upsertSql:{}", sql);

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
     * 获取数据库连接
     *
     * @param params
     * @return
     */
    private DataSource getDataSource(Map<String, Object> params) {
        JdbcProps jdbcProps = new JdbcProps();
        jdbcProps.setDriverClassName(params.get(JdbcCfgConstants.driverClassName).toString());
        jdbcProps.setUrl(params.get(JdbcCfgConstants.url).toString());
        jdbcProps.setUsername(params.get(JdbcCfgConstants.username).toString());
        jdbcProps.setPassword(params.get(JdbcCfgConstants.password).toString());
        DataSource dataSource = JdbcBuilder.build(jdbcProps);
        return dataSource;
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
}
