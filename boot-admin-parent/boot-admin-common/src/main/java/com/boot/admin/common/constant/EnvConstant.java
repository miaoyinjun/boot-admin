package com.boot.admin.common.constant;

/**
 * <p>
 * 运行环境定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public interface EnvConstant {
    /**
     * 开发 {@value}
     */
    String DEV = "dev";
    /**
     * 测试 {@value}
     */
    String QA = "qa";
    /**
     * 预生产 {@value}
     */
    String DEMO = "demo";
    /**
     * 生产 {@value}
     */
    String PROD = "prod";
}
