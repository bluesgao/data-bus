package com.wwyt.databus.plugin.common.enums;

/**
 * 数据源配置枚举
 *
 * @author bei.wu
 */
public enum DataSourceType {

    /**
     * mysql
     */
    MYSQL,
    /**
     * redis
     */
    REDIS,
    /**
     * es
     */
    ES,
    /**
     * 未知类型
     */
    UNKNOWN;

    /**
     * DataSource配置文件中type类型
     *
     * @param type 数据源类型
     * @return DataSourceType
     */
    public static DataSourceType getDataSourceEnum(String type) {
        for (DataSourceType ds : DataSourceType.values()) {
            if (type.toUpperCase().equals(ds.name())) {
                return ds;
            }
        }
        return DataSourceType.UNKNOWN;
    }
}
