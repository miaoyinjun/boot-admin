package org.jjche.cat.conf;

import org.jjche.cat.integration.feign.FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * feign 配置
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-20
 */
@Configuration
public class FeignAutoConfig {
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

}
