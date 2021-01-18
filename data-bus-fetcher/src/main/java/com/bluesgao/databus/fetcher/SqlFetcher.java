package com.bluesgao.databus.fetcher;

import com.alibaba.druid.util.JdbcUtils;
import com.bluesgao.databus.database.JdbcBuilder;
import com.bluesgao.databus.database.JdbcProps;
import com.bluesgao.databus.plugin.common.JdbcCfgConstants;
import com.bluesgao.databus.plugin.common.ModeType;
import com.bluesgao.databus.plugin.fetcher.DataFetcher;
import com.bluesgao.databus.plugin.fetcher.DataFetcherParam;
import com.bluesgao.databus.plugin.fetcher.DataFetcherResult;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：SqlFetcher
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class SqlFetcher implements DataFetcher {

    @Override
    public DataFetcherResult fetch(DataFetcherParam params, String event, Map<String, Object> data) {
        if (params.getMode().equalsIgnoreCase(ModeType.SQL.getType())) {
            /**
             *       "url": "jdbc:mysql://gyl.mysql.dev.wyyt:6612/renewscc_dev?tinyInt1isBit=false&transformedBitIsBoolean=false",
             *       "driverClassName": "com.mysql.jdbc.Driver",
             *       "username": "zyc",
             *       "password": "XNtyEFrgMwR5DYtBEjBG",
             *       "sql": "select * from t_user where user_id=?",
             *       "paramFields":"user_id"
             */

            JdbcProps jdbcProps = new JdbcProps();
            jdbcProps.setDriverClassName(params.getCfg().get(JdbcCfgConstants.driverClassName));
            jdbcProps.setUrl(params.getCfg().get(JdbcCfgConstants.url));
            jdbcProps.setUsername(params.getCfg().get(JdbcCfgConstants.username));
            jdbcProps.setPassword(params.getCfg().get(JdbcCfgConstants.password));


            DataSource dataSource = JdbcBuilder.build(jdbcProps);

            List<Map<String, Object>> dataList = null;
            try {
                List<Object> queryParams = getQueryParams(data, params.getCfg().get(JdbcCfgConstants.paramFields));
                dataList = JdbcUtils.executeQuery(dataSource, params.getCfg().get(JdbcCfgConstants.sql), queryParams);
                log.info("dataList:{}", dataList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dataList != null && dataList.size() > 0) {
                return DataFetcherResult.success(dataList.get(0));
            }

        } else {
            return DataFetcherResult.fail(String.format("数据获取方式[%s]不支持", params.getMode()));
        }
        return DataFetcherResult.fail("");
    }

    @Override
    public String getName() {
        return SqlFetcher.class.getCanonicalName();
    }


    private List<Object> getQueryParams(Map<String, Object> data, String paramFields) {
        List<Object> queryParams = new ArrayList<>();
        String[] qrys = paramFields.split(",");
        for (int i = 0; i < qrys.length; i++) {
            Object value = data.get(qrys[i]);
            queryParams.add(value);
        }
        return queryParams;
    }
}
