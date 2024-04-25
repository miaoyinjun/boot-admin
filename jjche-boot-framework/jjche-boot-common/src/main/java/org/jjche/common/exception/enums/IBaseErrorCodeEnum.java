package org.jjche.common.exception.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * <p>
 * 错误枚举基类
 * </p>
 *
 * @author miaoyj
 * @since 2022-06-29
 */
public interface IBaseErrorCodeEnum extends Serializable {
    /**
     * <p>
     * 消息
     * </p>
     *
     * @return /
     */
    String getMsg();

    /**
     * <p>
     * 编码
     * </p>
     *
     * @return /
     */
    @JsonValue
    int getCode();
}
