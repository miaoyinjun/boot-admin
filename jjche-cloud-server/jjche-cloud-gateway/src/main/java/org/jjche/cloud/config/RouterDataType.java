package org.jjche.cloud.config;

/**
 * <p>
 * nocos配置方式枚举
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-01
 */
public enum RouterDataType {
    /**
     * 数据库加载路由配置
     */
    database,
    /**
     * 本地yml加载路由配置
     */
    yml,
    /**
     * nacos加载路由配置
     */
    nacos
}
