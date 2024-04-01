package org.jjche.security.property;

import lombok.Data;
import org.jjche.common.constant.SecurityConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt参数配置
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019年11月28日
 */
@Data
@ConfigurationProperties(prefix = SecurityConstant.PROPERTY_SECURITY_PACKAGE_PREFIX)
public class SecurityProperties {
    /**
     * 令牌
     */
    private SecurityJwtProperties jwt;
    /**
     * 登录
     */
    private SecurityLoginProperties login;
    /**
     * rsa
     */
    private SecurityRsaProperties rsa;

}
