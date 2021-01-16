package com.bluesgao.databus.core.util.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Valid配置文件校验异常消息实体
 * @author bei.wu
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ValidError {

    /**
     * 字段名称
     */
    private String field;
    /**
     * 错误的消息
     */
    private String message;
}
