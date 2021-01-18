package com.bluesgao.databus.core.binlog;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 日志格式
 * (name = binlog-test, partition = 0, leaderEpoch = 0, offset = 503, CreateTime = 1607931917686, serialized key size = -1, serialized value size = 270, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = {"fetcher":[{"id":"707","name":"name0"}],"database":"test","builder":1607931917000,"id":502,"isDdl":false,"mysqlType":{"id":"bigint(20)","name":"varchar(60)"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":-5,"name":12},"tableName":"t_user","ts":1607931917685,"mode":"INSERT"})
 * <p>
 * {
 * "fetcher":[
 * {
 * "id":"707",
 * "name":"name0"
 * }
 * ],
 * "database":"test",
 * "builder":1607931917000,
 * "id":502,
 * "isDdl":false,
 * "mysqlType":{
 * "id":"bigint(20)",
 * "name":"varchar(60)"
 * },
 * "old":null,
 * "pkNames":[
 * "id"
 * ],
 * "sql":"",
 * "sqlType":{
 * "id":-5,
 * "name":12
 * },
 * "tableName":"t_user",
 * "ts":1607931917685,
 * "mode":"INSERT"
 * }
 * <p>
 * <p>
 * (name = binlog-test, partition = 0, leaderEpoch = 0, offset = 504, CreateTime = 1607931922251, serialized key size = -1, serialized value size = 291, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = {"fetcher":[{"id":"1","name":"test1-bluesgao"}],"database":"test","builder":1607931922000,"id":503,"isDdl":false,"mysqlType":{"id":"bigint(20)","name":"varchar(60)"},"old":[{"name":"test1"}],"pkNames":["id"],"sql":"","sqlType":{"id":-5,"name":12},"tableName":"t_user","ts":1607931922250,"mode":"UPDATE"})
 * <p>
 * {
 * "fetcher":[
 * {
 * "id":"1",
 * "name":"test1-bluesgao"
 * }
 * ],
 * "database":"test",
 * "builder":1607931922000,
 * "id":503,
 * "isDdl":false,
 * "mysqlType":{
 * "id":"bigint(20)",
 * "name":"varchar(60)"
 * },
 * "old":[
 * {
 * "name":"test1"
 * }
 * ],
 * "pkNames":[
 * "id"
 * ],
 * "sql":"",
 * "sqlType":{
 * "id":-5,
 * "name":12
 * },
 * "tableName":"t_user",
 * "ts":1607931922250,
 * "mode":"UPDATE"
 * }
 */
@Data
public class Binlog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<Map<String, String>> data;

    /**
     * 数据库
     */
    private String database;
    /**
     * 物理表名
     */
    private String table;

    /**
     * 执行耗时(执行时间)
     */
    private Long es;

    //todo
    private Long id;

    /**
     * 是否DDL语句
     */
    private Boolean isDdl;

    //todo
    private Map<String, String> mysqlType;

    /**
     * 旧数据列表, 用于update, size和data的size一一对应
     */
    private List<Map<String, String>> old;

    /**
     * 主键
     */
    private List<String> pkNames;

    /**
     * 执行的sql, dml sql为空
     */
    private String sql;


    private Map<String, Integer> sqlType;

    /**
     * 同步时间
     */
    private Long ts;


    /**
     * SQL类型: INSERT UPDATE DELETE
     */
    private String type;


    /**
     * 全局递增ID
     */
    private String gtid;
}
