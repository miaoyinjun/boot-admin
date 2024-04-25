package org.jjche.security.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>RequestMethodEnum class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2020-06-10
 */
@Getter
@AllArgsConstructor
public enum RequestMethodEnum {

    /**
     * 搜寻 @IgnoreGetMapping
     */
    GET("GET"),

    /**
     * 搜寻 @IgnorePostMapping
     */
    POST("POST"),

    /**
     * 搜寻 @IgnorePutMapping
     */
    PUT("PUT"),

    /**
     * 搜寻 @IgnorePatchMapping
     */
    PATCH("PATCH"),

    /**
     * 搜寻 @IgnoreDeleteMapping
     */
    DELETE("DELETE"),

    /**
     * 否则就是所有 Request 接口都放行
     */
    ALL("All");

    /**
     * Request 类型
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link RequestMethodEnum} object.
     */
    private final String type;

    public static RequestMethodEnum find(String type) {
        for (RequestMethodEnum value : RequestMethodEnum.values()) {
            if (type.equals(value.getType())) {
                return value;
            }
        }
        return ALL;
    }
}
