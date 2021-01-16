package com.bluesgao.databus.plugin;

import java.util.Map;

public interface ClientBuilder {
    /**
     * 获取DataSource客户端
     *
     * @return
     */
    Object getClient();

    /**
     * 获取DataSource类型 class全限定名
     *
     * @return
     */
    String getType();

    /**
     * 获取DataSource唯一名称
     *
     * @return
     */
    String getName();

    /**
     * dataSource 执行接口
     *
     * @param data
     * @param eventType
     * @return
     */
    boolean execute(Map<String, Object> data, String eventType);
}
