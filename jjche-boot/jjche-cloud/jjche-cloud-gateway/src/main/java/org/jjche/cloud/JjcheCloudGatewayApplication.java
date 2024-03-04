package org.jjche.cloud;

import org.jjche.cloud.gray.config.VersionServiceInstanceListSupplierConfiguration;
import org.jjche.cloud.loader.DynamicRouteLoader;
import org.jjche.system.modules.ISysBaseAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.GatewayDiscoveryClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.Resource;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-03
 */
@EnableFeignClients(clients = ISysBaseAPI.class)
@EnableDiscoveryClient
@LoadBalancerClients(defaultConfiguration = VersionServiceInstanceListSupplierConfiguration.class)
@SpringBootApplication(exclude = {GatewayDiscoveryClientAutoConfiguration.class})
public class JjcheCloudGatewayApplication implements CommandLineRunner {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;

    public static void main(String[] args) {
        SpringApplication.run(JjcheCloudGatewayApplication.class, args);
    }

    /**
     * 容器初始化后加载路由
     *
     * @param strings
     */
    @Override
    public void run(String... strings) {
        dynamicRouteLoader.refresh(null);
    }
}
