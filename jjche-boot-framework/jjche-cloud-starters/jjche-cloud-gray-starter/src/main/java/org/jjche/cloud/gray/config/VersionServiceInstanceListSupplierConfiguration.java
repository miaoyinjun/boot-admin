package org.jjche.cloud.gray.config;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * 替换默认服务实例筛选逻辑
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-26
 */
public class VersionServiceInstanceListSupplierConfiguration {
    @Bean
    ServiceInstanceListSupplier serviceInstanceListSupplier(ConfigurableApplicationContext context) {
        ServiceInstanceListSupplier delegate = ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .withCaching()
                .build(context);
        return new VersionServiceInstanceListSupplier(delegate);
    }
}
