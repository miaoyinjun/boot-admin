package org.jjche.swagger.property;

import lombok.Data;

/**
 * <p>
 * jwt属性配置类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Data
public class SwaggerSecurityJwtProperties {
    /**
     * 是否开启
     */
    private boolean enabled;
    /**
     * 认证header
     */
    private String tokenHeader;

    /**
     * 认证前缀
     */
    private String tokenStartWith;
}
