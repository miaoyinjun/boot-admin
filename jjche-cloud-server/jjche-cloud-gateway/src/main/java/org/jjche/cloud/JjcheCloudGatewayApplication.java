package org.jjche.cloud;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import org.jjche.cloud.gray.config.VersionServiceInstanceListSupplierConfiguration;
import org.jjche.cloud.loader.DynamicRouteLoader;
import org.jjche.common.constant.SpringPropertyConstant;
import org.jjche.sys.api.SysBaseApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.GatewayDiscoveryClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-03
 */
@EnableFeignClients(clients = SysBaseApi.class)
@EnableDiscoveryClient
@LoadBalancerClients(defaultConfiguration = VersionServiceInstanceListSupplierConfiguration.class)
@SpringBootApplication(exclude = {GatewayDiscoveryClientAutoConfiguration.class})
public class JjcheCloudGatewayApplication implements CommandLineRunner {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(JjcheCloudGatewayApplication.class, args);
        try {
            Environment env = application.getEnvironment();
            String serverPort = env.getProperty("server.port");
            String contextPath = Optional
                    .ofNullable(env.getProperty("server.servlet.context-path"))
                    .filter(StrUtil::isNotBlank)
                    .orElse("/");
            String hostAddress = "localhost";
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                StaticLog.warn("The host name could not be determined, using `localhost` as fallback");
            }

            StaticLog.info("\n----------------------------------------------------------\n\t" +
                            "Application '{}' is running! Access URLs:\n\t" +
                            "Local: \t\thttp://localhost:{}{}\n\t" +
                            "External: \thttp://{}:{}{}\n----------------------------------------------------------",
                    env.getProperty(SpringPropertyConstant.APP_NAME),
                    serverPort,
                    contextPath,
                    hostAddress,
                    serverPort,
                    contextPath,
                    hostAddress,
                    serverPort,
                    contextPath,
                    env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
