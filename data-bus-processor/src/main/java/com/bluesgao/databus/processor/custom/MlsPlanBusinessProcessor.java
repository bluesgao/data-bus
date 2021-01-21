package com.bluesgao.databus.processor.custom;

import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.bluesgao.databus.ds.JdbcBuilder;
import com.bluesgao.databus.ds.JdbcProps;
import com.bluesgao.databus.plugin.common.enums.EventType;
import com.bluesgao.databus.plugin.processor.DataProcessor;
import com.bluesgao.databus.plugin.processor.DataProcessorResult;
import com.bluesgao.databus.util.sql.SqlBuilder;
import com.bluesgao.databus.util.sql.SqlEntity;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
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
        DataSource dataSource = buildDataSource(params);
        if (event.equalsIgnoreCase(EventType.INSERT.getEvent())) {

            String purchaseSql = buildInsertSqlByPURCHASE(data);
            if (purchaseSql == null) {
                return DataProcessorResult.fail("buildInsertSqlByPURCHASE生成sql失败");
            }

            String finSql = buildInsertSqlByFIN(data);
            if (finSql == null) {
                return DataProcessorResult.fail("buildInsertSqlByFIN生成sql失败");
            }

            try {
                JdbcUtils.execute(dataSource, purchaseSql);
                JdbcUtils.execute(dataSource, finSql);
                return DataProcessorResult.success();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if (event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            String purchaseSql = buildUpdateSqlByPURCHASE(data);
            if (purchaseSql == null) {
                return DataProcessorResult.fail("buildUpdateSqlByPURCHASE生成sql失败");
            }

            String finSql = buildUpdateSqlByFIN(data);
            if (finSql == null) {
                return DataProcessorResult.fail("buildUpdateSqlByFIN生成sql失败");
            }

            try {
                JdbcUtils.execute(dataSource, purchaseSql);
                JdbcUtils.execute(dataSource, finSql);
                return DataProcessorResult.success();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (event.equalsIgnoreCase(EventType.DELETE.getEvent())) {
            //todo
        }
        return DataProcessorResult.fail("");
    }


    @Override
    public String getName() {
        return MlsPlanBusinessProcessor.class.getCanonicalName();
    }

    private String buildInsertSqlByPURCHASE(Map<String, Object> data) {
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
        fieldAndValues.put("id", "UUID_SHORT()");
        fieldAndValues.put("buyer_company_type", "PURCHASE");
        fieldAndValues.put("seller_company_type", "FIN");
        //fieldAndValues.put("business_type", 0);
        fieldAndValues.put("business_module", 0);

        SqlEntity insertSqlEntity = new SqlEntity("mls_plan_business", fieldAndValues, null);
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

        SqlEntity insertSqlEntity = new SqlEntity("mls_plan_business", fieldAndValues, null);
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

    private DataSource buildDataSource(Map<String, Object> params) {
        JdbcProps jdbcProps = new JdbcProps();
        jdbcProps.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcProps.setUrl("jdbc:mysql://gyl.mysql.dev.wyyt:6612/wyw_dev?tinyInt1isBit=false&transformedBitIsBoolean=false");
        jdbcProps.setUsername("zyc");
        jdbcProps.setPassword("XNtyEFrgMwR5DYtBEjBG");
        return JdbcBuilder.build(jdbcProps);
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

    private String upsertSql(String table, Map<String, Object> data) {
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
