package org.jjche.security;

import org.jjche.security.config.ElPermissionConfig;
import org.jjche.security.config.SecurityConfig;
import org.jjche.security.handler.JwtAuthenticationAccessDeniedHandler;
import org.jjche.security.handler.JwtAuthenticationEntryPoint;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.security.TokenProvider;
import org.jjche.security.service.JwtUserServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 权限配置入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Configuration
@Import({
        SecurityProperties.class,
        JwtAuthenticationAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class, SecurityConfig.class,
        TokenProvider.class, ElPermissionConfig.class, JwtUserServiceImpl.class
})
public class SecurityAutoConfiguration {
}