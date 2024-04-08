package org.jjche.cloud;

import org.jjche.cloud.config.FeignConfig;
import org.jjche.cloud.gray.config.VersionServiceInstanceListSupplierConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FeignConfig.class})
@LoadBalancerClients(defaultConfiguration = VersionServiceInstanceListSupplierConfiguration.class)
@EnableDiscoveryClient
public class CloudAutoConfiguration {
}
