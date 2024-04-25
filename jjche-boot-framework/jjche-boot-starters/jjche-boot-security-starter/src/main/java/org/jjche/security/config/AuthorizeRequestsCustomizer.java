package org.jjche.security.config;

import org.springframework.core.Ordered;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * <p>
 *  * 自定义的 URL 的安全配置
 *  * 目的：每个 Maven Module 可以自定义规则！
 *  *
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-30
 */
public abstract class AuthorizeRequestsCustomizer
        implements Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry>, Ordered {
    @Override
    public int getOrder() {
        return 0;
    }

}
