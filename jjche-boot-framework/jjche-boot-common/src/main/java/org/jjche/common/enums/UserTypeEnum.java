package org.jjche.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 用户 类型枚举
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-18
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    /**
     * 主键
     */
    PWD("PWD", "密码用户"),
    SMS("SMS", "短信用户"),
    ;

    private final String value;
    private final String desc;
}
