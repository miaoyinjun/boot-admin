package org.jjche.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * <p>
 * 枚举基类
 * </p>
 *
 * @author miaoyj
 * @since 2022-06-29
 */
public interface IBaseEnum extends Serializable {
    /**
     * <p>
     * 描述
     * </p>
     *
     * @return /
     */
    String getDesc();

    /**
     * <p>
     * 值
     * </p>
     *
     * @return /
     */
    @JsonValue
    String getValue();
}
