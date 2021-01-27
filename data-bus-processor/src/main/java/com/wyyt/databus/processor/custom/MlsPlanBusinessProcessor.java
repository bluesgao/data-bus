package com.wyyt.databus.processor.custom;

import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：MlsPlanBusinessProcessor
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class MlsPlanBusinessProcessor implements DataProcessor {

    @Override
    public DataProcessorResult process(Map<String, Object> params, String event, Map<String, Object> data) {
        log.info("MlsPlanBusinessProcessor ****开始处理****");
        DataSource dataSource = getDataSourceConn(params);
        if (dataSource == null) {
            return DataProcessorResult.fail("获取数据连接conn错误");
        }

        if (event.equalsIgnoreCase(EventType.INSERT.getEvent()) || event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            String purchaseSql = existPURCHASE(dataSource, data) ? buildUpdateSqlByPURCHASE(data) : buildInsertSqlByPURCHASE(data);

            if (purchaseSql == null) {
                return DataProcessorResult.fail("buildSqlByPURCHASE生成sql失败");
            }
            try {
                JdbcUtils.execute(dataSource, purchaseSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String finSql = existFIN(dataSource, data) ? buildUpdateSqlByFIN(data) : buildInsertSqlByFIN(data);

            if (finSql == null) {
                return DataProcessorResult.fail("buildSqlByFIN生成sql失败");
            }
            try {
                JdbcUtils.execute(dataSource, finSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return DataProcessorResult.success();

        } else if (event.equalsIgnoreCase(EventType.DELETE.getEvent())) {
            //todo
        }
        return DataProcessorResult.fail("");
    }


    @Override
    public String getName() {
        return MlsPlanBusinessProcessor.class.getCanonicalName();
    }

    private boolean existPURCHASE(DataSource dataSource, Map<String, Object> data) {
        //判断数据是否存在
        List<Object> queryParams = new ArrayList<>();
        queryParams.add(data.get("protocol_id"));
        queryParams.add(data.get("order_company_id"));
        List<Map<String, Object>> queryResult = null;
        try {
            queryResult = JdbcUtils.executeQuery(dataSource, "select * from mls_plan_business where plan_id=? and buyer_company_id=?", queryParams);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (queryResult != null && queryResult.size() > 0) {
            return true;
        }
        return false;
    }

    private boolean existFIN(DataSource dataSource, Map<String, Object> data) {
        //判断数据是否存在
        List<Object> queryParams = new ArrayList<>();
        queryParams.add(data.get("protocol_id"));
        queryParams.add(data.get("fin_company_id"));
        List<Map<String, Object>> queryResult = null;
        try {
            queryResult = JdbcUtils.executeQuery(dataSource, "select * from mls_plan_business where plan_id=? and buyer_company_id=?", queryParams);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (queryResult != null && queryResult.size() > 0) {
            return true;
        }
        return false;
    }

    private String buildInsertSqlByPURCHASE(Map<String, Object> data) {
        Map<String, String> fieldMappings = new HashMap<>(8);
        fieldMappings.put("protocol_id", "plan_id");
        fieldMappings.put("order_company_id", "buyer_company_id");
        fieldMappings.put("order_company_name", "buyer_company_name");
        fieldMappings.put("order_user_id", "buyer_user_id");
        fieldMappings.put("order_user_name", "buyer_user_name");
        fieldMappings.put("order_user_phone", "buyer_user_phone");
        fieldMappings.put("fin_company_id", "seller_company_id");
        fieldMappings.put("fin_company_name", "seller_company_name");

        //从data中获取数据
        Map<String, Object> fieldAndValues = populateFieldAndValues(fieldMappings, data);
        if (fieldAndValues == null || fieldAndValues.size() == 0) {
            log.error("populateFieldAndValues为空,fieldMappings:{},data:{}", JSON.toJSONString(fieldAndValues), JSON.toJSONString(data));
            return null;
        }

        //添加固定值
        //fieldAndValues.put("id", UUID.randomUUID().getLeastSignificantBits());
        fieldAndValues.put("buyer_company_type", "PURCHASE");
        fieldAndValues.put("seller_company_type", "FIN");
        //fieldAndValues.put("business_type", 0);
        fieldAndValues.put("business_module", 0);

        SqlEntity insertSqlEntity = new SqlEntity("mls_plan_business", fieldAndValues, null, true);
        return SqlBuilder.insert(insertSqlEntity);
    }

    private String buildInsertSqlByFIN(Map<String, Object> data) {
        Map<String, String> fieldMappings = new HashMap<>(8);
        fieldMappings.put("protocol_id", "plan_id");
        fieldMappings.put("fin_company_id", "buyer_company_id");
        fieldMappings.put("fin_company_name", "buyer_company_name");

        fieldMappings.put("supplier_company_id", "seller_company_id");
        fieldMappings.put("supplier_company_name", "seller_company_name");

        fieldMappings.put("supplier_user_id", "seller_user_id");
        fieldMappings.put("supplier_user_name", "seller_user_name");
        fieldMappings.put("supplier_user_phone", "seller_user_phone");

        //从data中获取数据
        Map<String, Object> fieldAndValues = populateFieldAndValues(fieldMappings, data);
        if (fieldAndValues == null || fieldAndValues.size() == 0) {
            log.error("populateFieldAndValues为空,fieldMappings:{},data:{}", JSON.toJSONString(fieldAndValues), JSON.toJSONString(data));
            return null;
        }

        //添加固定值
        fieldAndValues.put("buyer_company_type", "FIN");
        fieldAndValues.put("seller_company_type", "SUPPLIER");
        //fieldAndValues.put("business_type", 0);
        fieldAndValues.put("business_module", 1);

        SqlEntity insertSqlEntity = new SqlEntity("mls_plan_business", fieldAndValues, null, true);
        return SqlBuilder.insert(insertSqlEntity);
    }

    private String buildUpdateSqlByPURCHASE(Map<String, Object> data) {
        Map<String, String> fieldMappings = new HashMap<>(8);
        fieldMappings.put("protocol_id", "plan_id");
        fieldMappings.put("order_company_id", "buyer_company_id");
        fieldMappings.put("order_company_name", "buyer_company_name");
        fieldMappings.put("order_user_id", "buyer_user_id");
        fieldMappings.put("order_user_name", "buyer_user_name");
        fieldMappings.put("buyer_user_phone", "order_user_phone");
        fieldMappings.put("seller_company_id", "fin_company_id");
        fieldMappings.put("seller_company_name", "fin_company_name");

        //从data中获取数据
        Map<String, Object> fieldAndValues = populateFieldAndValues(fieldMappings, data);
        if (fieldAndValues == null || fieldAndValues.size() == 0) {
            log.error("populateFieldAndValues为空,fieldMappings:{},data:{}", JSON.toJSONString(fieldAndValues), JSON.toJSONString(data));
            return null;
        }

        //添加固定值
        fieldAndValues.put("buyer_company_type", "PURCHASE");
        fieldAndValues.put("seller_company_type", "FIN");
        //fieldAndValues.put("business_type", 0);
        fieldAndValues.put("business_module", 0);

        //where条件
        Map<String, String> whereFieldMappings = new HashMap<>(8);
        whereFieldMappings.put("protocol_id", "plan_id");
        whereFieldMappings.put("order_company_id", "buyer_company_id");
        Map<String, Object> whereFieldAndValues = populateFieldAndValues(whereFieldMappings, data);
        SqlEntity sqlEntity = new SqlEntity("mls_plan_business", fieldAndValues, whereFieldAndValues);
        return SqlBuilder.update(sqlEntity);
    }

    private String buildUpdateSqlByFIN(Map<String, Object> data) {
        Map<String, String> fieldMappings = new HashMap<>(8);
        fieldMappings.put("protocol_id", "plan_id");
        fieldMappings.put("fin_company_id", "buyer_company_id");
        fieldMappings.put("fin_company_name", "buyer_company_name");

        fieldMappings.put("supplier_company_id", "seller_company_id");
        fieldMappings.put("supplier_company_name", "seller_company_name");

        fieldMappings.put("supplier_user_id", "seller_user_id");
        fieldMappings.put("supplier_user_name", "seller_user_name");
        fieldMappings.put("supplier_user_phone", "seller_user_phone");

        //从data中获取数据
        Map<String, Object> fieldAndValues = populateFieldAndValues(fieldMappings, data);
        if (fieldAndValues == null || fieldAndValues.size() == 0) {
            log.error("populateFieldAndValues为空,fieldMappings:{},data:{}", JSON.toJSONString(fieldAndValues), JSON.toJSONString(data));
            return null;
        }

        //添加固定值
        fieldAndValues.put("buyer_company_type", "FIN");
        fieldAndValues.put("seller_company_type", "SUPPLIER");
        //fieldAndValues.put("business_type", 0);
        fieldAndValues.put("business_module", 1);

        //where条件
        Map<String, String> whereFieldMappings = new HashMap<>(8);
        whereFieldMappings.put("protocol_id", "plan_id");
        whereFieldMappings.put("fin_company_id", "buyer_company_id");
        Map<String, Object> whereFieldAndValues = populateFieldAndValues(whereFieldMappings, data);
        SqlEntity sqlEntity = new SqlEntity("mls_plan_business", fieldAndValues, whereFieldAndValues);
        return SqlBuilder.update(sqlEntity);
    }

    private DataSource getDataSourceConn(Map<String, Object> params) {
        JdbcProps jdbcProps = new JdbcProps();
        jdbcProps.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcProps.setUrl("jdbc:mysql://gyl.mysql.dev.wyyt:6612/wyw_dev?tinyInt1isBit=false&transformedBitIsBoolean=false");
        jdbcProps.setUsername("zyc");
        jdbcProps.setPassword("XNtyEFrgMwR5DYtBEjBG");
        DataSource dataSource = JdbcBuilder.build(jdbcProps);
        return dataSource;
    }

    private Map<String, Object> populateFieldAndValues(Map<String, String> fieldMappings, Map<String, Object> data) {
        if (fieldMappings == null || fieldMappings.size() == 0 || data == null || data.size() == 0) {
            return null;
        }

        Map<String, Object> fieldAndValues = new HashMap<>(8);
        for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
            String oldFieldName = entry.getKey();
            String newFieldName = entry.getValue();
            Object value = data.get(oldFieldName);
            if (value != null) {
                fieldAndValues.put(newFieldName, value);
            }
        }
        return fieldAndValues;
    }
}
