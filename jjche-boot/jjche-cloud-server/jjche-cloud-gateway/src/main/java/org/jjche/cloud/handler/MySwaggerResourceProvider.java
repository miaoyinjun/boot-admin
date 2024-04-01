package org.jjche.cloud.handler;

import cn.hutool.log.StaticLog;
import org.jjche.common.constant.SpringPropertyConstant;
import org.jjche.common.constant.SwaggerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 聚合各个服务的swagger接口
 */
@Component
@Primary
public class MySwaggerResourceProvider implements SwaggerResourcesProvider {
    @Value("${spring.cloud.gateway.api-prefix}")
    private String prefix;

    /**
     * 网关路由
     */
    private final RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${" + SpringPropertyConstant.APP_NAME + "}")
    private String self;

    @Autowired
    public MySwaggerResourceProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routeHosts = new ArrayList<>();
        // 获取所有可用的host：serviceId
        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                .filter(route -> !self.equals(route.getUri().getHost()))
                .subscribe(route -> routeHosts.add(route.getUri().getHost()));

        // 记录已经添加过的server，存在同一个应用注册了多个服务在nacos上
        Set<String> dealed = new HashSet<>();
        routeHosts.forEach(instance -> {
            // 拼接url
            String url = "/" + instance.toLowerCase() + prefix + SwaggerConstant.SWAGGER_2_URL;
            if (!dealed.contains(url)) {
                dealed.add(url);
                StaticLog.info(" Gateway add SwaggerResource: {}", url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setSwaggerVersion("2.0");
                swaggerResource.setName(instance);
                //Swagger排除监控
                if (instance.indexOf("jjche-cloud-monitor") == -1) {
                    resources.add(swaggerResource);
                }
            }
        });
        return resources;
    }
}