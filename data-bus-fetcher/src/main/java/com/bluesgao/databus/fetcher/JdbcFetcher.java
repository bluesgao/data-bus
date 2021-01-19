package com.bluesgao.databus.fetcher;

import com.alibaba.druid.util.JdbcUtils;
import com.bluesgao.databus.database.JdbcBuilder;
import com.bluesgao.databus.database.JdbcProps;
import com.bluesgao.databus.plugin.common.JdbcCfgConstants;
import com.bluesgao.databus.plugin.fetcher.DataFetcher;
import com.bluesgao.databus.plugin.fetcher.DataFetcherResult;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName：JdbcFetcher
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class JdbcFetcher implements DataFetcher {

    @Override
    public DataFetcherResult fetch(Map<String, Object> params, String event, Map<String, Object> data) {
        if (params != null && params.size() > 0) {
            //进行参数验证
            String checkResult = checkParams(params);
            if (checkResult != null && checkResult.length() > 0) {
                return DataFetcherResult.fail(checkResult);
            }
        }
        /**
         *       "url": "jdbc:mysql://gyl.mysql.dev.wyyt:6612/renewscc_dev?tinyInt1isBit=false&transformedBitIsBoolean=false",
         *       "driverClassName": "com.mysql.jdbc.Driver",
         *       "username": "zyc",
         *       "password": "XNtyEFrgMwR5DYtBEjBG",
         *       "biz.sql": "select * from t_user where user_id=?",
         *       "biz.fields":"user_id"
         */

        JdbcProps jdbcProps = new JdbcProps();
        jdbcProps.setDriverClassName(params.get(JdbcCfgConstants.driverClassName).toString());
        jdbcProps.setUrl(params.get(JdbcCfgConstants.url).toString());
        jdbcProps.setUsername(params.get(JdbcCfgConstants.username).toString());
        jdbcProps.setPassword(params.get(JdbcCfgConstants.password).toString());


        DataSource dataSource = JdbcBuilder.build(jdbcProps);

        List<Map<String, Object>> dataList = null;
        try {
            List<Object> queryParams = getParamValue(data, (List<String>) params.get(JdbcCfgConstants.biz_fields));
            String script = params.get(JdbcCfgConstants.biz_sql).toString();
            dataList = JdbcUtils.executeQuery(dataSource, script, queryParams);
            log.info("dataList:{}", dataList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dataList != null && dataList.size() > 0) {
            return DataFetcherResult.success(dataList.get(0));
        }

        return DataFetcherResult.fail("");
    }

    @Override
    public String getName() {
        return JdbcFetcher.class.getCanonicalName();
    }

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
        } else if (Objects.isNull(params.get(JdbcCfgConstants.biz_sql))) {
            err.append("biz_sql为空;");
        } else if (Objects.isNull(params.get(JdbcCfgConstants.biz_fields)) ||
                !(params.get(JdbcCfgConstants.biz_fields) instanceof List)) {
            err.append("biz_fields为空;");
        }
        return err.toString();
    }

    private List<Object> getParamValue(Map<String, Object> data, List<String> paramFields) {
        List<Object> values = new ArrayList<>();

        for (int i = 0; i < paramFields.size(); i++) {
            Object value = data.get(paramFields.get(i));
            values.add(value);
        }
        return values;
    }
}
