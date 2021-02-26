package com.wyyt.databus.fetcher.custom;

import com.alibaba.druid.util.JdbcUtils;
import com.wwyt.databus.plugin.common.enums.EventType;
import com.wwyt.databus.plugin.fetcher.DataFetcher;
import com.wwyt.databus.plugin.fetcher.DataFetcherResult;
import com.wyyt.databus.ds.JdbcBuilder;
import com.wyyt.databus.ds.JdbcProps;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：ProtocolFetcher
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class ProtocolFetcher implements DataFetcher {

    @Override
    public DataFetcherResult fetch(Map<String, Object> params, String event, Map<String, Object> data) {
//初始化数据源
        /**
         *       "url": "jdbc:mysql://gyl.mysql.dev.wyyt:6612/renewscc_dev?tinyInt1isBit=false&transformedBitIsBoolean=false",
         *       "driverClassName": "com.mysql.jdbc.Driver",
         *       "username": "zyc",
         *       "password": "XNtyEFrgMwR5DYtBEjBG",
         *       "ds": "renewscc_dev"
         */
        JdbcProps jdbcProps = new JdbcProps();
        jdbcProps.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcProps.setUrl("jdbc:mysql://192.168.32.90:6612/cscc_test?tinyInt1isBit=false&transformedBitIsBoolean=false");
        jdbcProps.setUsername("coalscc");
        jdbcProps.setPassword("af12121036b1c4e37ae8ab930a76679b");

        DataSource dataSource = JdbcBuilder.build(jdbcProps);
        if (event.equalsIgnoreCase(EventType.INSERT.getEvent()) || event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            List<Map<String, Object>> dataList = null;
            try {
                List<Object> queryParams = new ArrayList<>();
                queryParams.add(data.get("protocol_id"));
                dataList = JdbcUtils.executeQuery(dataSource, "SELECT t1.protocol_id as id,t1.protocol_sn as plan_sn,t1.status ,t1.old_status as is_closed ,t1.complete_time as finish_time ,t1.`number` as plan_qty ,t1.order_user_id as operator_id ,t1.order_user_name as operator_name ,t1.version ,t1.create_time as row_create_time,t1.update_time as row_update_time,t1.is_delete as is_deleted , t2.begin_time as start_time, t2.end_time FROM trade_protocol t1 LEFT JOIN trade_protocol_logistics t2 ON t1.protocol_id = t2.protocol_id WHERE t1.protocol_id =?;", queryParams);
                log.info("dataList:{}", dataList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dataList != null && dataList.size() > 0) {
                return DataFetcherResult.success(dataList.get(0));
            }
        }
        return DataFetcherResult.fail("");
    }

    @Override
    public String getName() {
        return ProtocolFetcher.class.getCanonicalName();
    }
}
