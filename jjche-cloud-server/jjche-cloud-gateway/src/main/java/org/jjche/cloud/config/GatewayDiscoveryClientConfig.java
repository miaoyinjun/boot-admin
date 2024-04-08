package org.jjche.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.discovery.GatewayDiscoveryClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义网关注册中心配置
 * 这里好像只会影响swagger的配置
 * /jjche-cloud-system/api/sys/auth/code
 * </p>
 *
 * @author miaoyj
 * @since 2022-05-10
 */
@Configuration
public class GatewayDiscoveryClientConfig<main> extends GatewayDiscoveryClientAutoConfiguration {

    @Value("${spring.cloud.gateway.api-prefix}")
    private String prefix;

    @Bean
    @Override
    public DiscoveryLocatorProperties discoveryLocatorProperties() {
        DiscoveryLocatorProperties properties = new DiscoveryLocatorProperties();
        properties.setPredicates(myInitPredicates());
        properties.setFilters(myInitFilters());
        return properties;
    }

    public List<PredicateDefinition> myInitPredicates() {
        ArrayList<PredicateDefinition> definitions = new ArrayList();
        PredicateDefinition predicate = new PredicateDefinition();
        //定义路由路径的匹配规则
        predicate.setName(NameUtils.normalizeRoutePredicateName(PathRoutePredicateFactory.class));
        String pattern = "'" + "/'+serviceId+'" + prefix + "/**'";

        predicate.addArg("pattern", pattern);
        definitions.add(predicate);
        return definitions;
    }

    public List<FilterDefinition> myInitFilters() {
        ArrayList<FilterDefinition> definitions = new ArrayList();
        FilterDefinition filter = new FilterDefinition();
        //重新请求路径
        filter.setName(NameUtils.normalizeFilterFactoryName(RewritePathGatewayFilterFactory.class));
        String regex = "'" + "/' + serviceId + '" + prefix + "/(?<remaining>.*)'";
        String replacement = "'/${remaining}'";
        filter.addArg("regexp", regex);
        filter.addArg("replacement", replacement);
        definitions.add(filter);

        return definitions;
    }
}
