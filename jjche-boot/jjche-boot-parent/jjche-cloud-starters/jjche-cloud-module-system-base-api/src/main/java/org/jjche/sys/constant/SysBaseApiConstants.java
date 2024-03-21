package org.jjche.sys.constant;

/**
 * <p>
 * API定义
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
public interface SysBaseApiConstants {
    /**
     * 系统服务 {@value}
     */
    String NAME = "jjche-cloud-system";

    /**
     * 系统服务 {@value}
     */
    String FEIGN_URL = "${FEIGN_URL_SYSTEM:}";
}
