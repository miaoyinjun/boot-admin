package org.jjche.sys.config;

import org.jjche.core.constant.FileConstant;
import org.jjche.security.config.AuthorizeRequestsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * <p>
 * 系统权限配置
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-30
 */
@Configuration("sysSecurityConfiguration")
public class SysSecurityConfiguration {

    @Bean("sysAuthorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {

            @Override
            public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
                //webSocket不限制权限
                registry.antMatchers("/**/webSocket/**").permitAll();
                // 访问头像和文件不限制权限
                registry.antMatchers(FileConstant.AVATAR_PATH_MATCH, FileConstant.FILE_PATH_MATCH).permitAll();
            }
        };
    }
}
