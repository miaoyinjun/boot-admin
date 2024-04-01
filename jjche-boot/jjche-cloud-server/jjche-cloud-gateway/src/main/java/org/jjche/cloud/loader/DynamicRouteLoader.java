package org.jjche.cloud.loader;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import org.jjche.cloud.config.GatewayRoutersConfiguration;
import org.jjche.cloud.config.RouterDataType;
import org.jjche.common.base.BaseMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 动态路由加载器
 *
 * @author : zyf
 * @date :2020-11-10
 */
@Component
@DependsOn({"gatewayRoutersConfiguration"})
public class DynamicRouteLoader {
    @Value("${spring.cloud.gateway.api-prefix}")
    private String prefix;

    private InMemoryRouteDefinitionRepository repository;
    private DynamicRouteService dynamicRouteService;
    private ConfigService configService;

    public DynamicRouteLoader(InMemoryRouteDefinitionRepository repository, DynamicRouteService dynamicRouteService) {
        this.repository = repository;
        this.dynamicRouteService = dynamicRouteService;
    }

    @PostConstruct
    public void init() {
        init(null);
    }

    public void init(BaseMap baseMap) {
        String dataType = GatewayRoutersConfiguration.DATA_TYPE;
        StaticLog.info("初始化路由，dataType：" + dataType);
        if (RouterDataType.nacos.toString().endsWith(dataType)) {
            loadRoutesByNacos();
        }
    }

    /**
     * 刷新路由
     *
     * @return
     */
    public Mono<Void> refresh(BaseMap baseMap) {
        String dataType = GatewayRoutersConfiguration.DATA_TYPE;
        if (!RouterDataType.yml.toString().endsWith(dataType)) {
            this.init(baseMap);
        }
        return Mono.empty();
    }

    /**
     * 从nacos中读取路由配置
     *
     * @return
     */
    private void loadRoutesByNacos() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            StaticLog.warn("initConfigService fail");
        }
        try {
            String configInfo = configService.getConfig(GatewayRoutersConfiguration.DATA_ID, GatewayRoutersConfiguration.ROUTE_GROUP, GatewayRoutersConfiguration.DEFAULT_TIMEOUT);
            if (StrUtil.isNotBlank(configInfo)) {
                StaticLog.info("获取网关当前配置:\r\n{}", configInfo);
                routes = JSONUtil.toList(configInfo, RouteDefinition.class);
//                List<RouteDefinition> realRoutes = CollUtil.newArrayList();
//                List<RouteDefinition> ruleOutRoutes = CollUtil.newArrayList();
//                //单独处理的服务
//                List<String> serviceNameList = CollUtil.newArrayList("JJCHE-CLOUD-GATEWAY", "JJCHE-CLOUD-MONITOR");
//                for (RouteDefinition definition : routes) {
//                    String id = definition.getId();
//                    if (CollUtil.contains(serviceNameList, id)) {
//                        ruleOutRoutes.add(definition);
//                    } else {
//                        realRoutes.add(definition);
//                    }
//                }
//                routes = realRoutes;
//                for (RouteDefinition definition : ruleOutRoutes) {
//                    dynamicRouteService.add(definition);
//                }
            }
        } catch (NacosException e) {
            StaticLog.error("初始化网关路由时发生错误", e);
            e.printStackTrace();
        }
        for (RouteDefinition definition : routes) {
            /**
             * 使用json配置文件，这里不做处理
             */
            /**
             //为匹配映射/api/**，增加/api前缀
             List<PredicateDefinition> predicates = definition.getPredicates();
             for (PredicateDefinition predicate : predicates) {
             Map<String, String> args = predicate.getArgs();
             for (String s : args.keySet()) {
             String value = args.get(s);
             args.put(s, prefix + value);
             }
             }
             //请求微服务时，删除/api前缀
             List<FilterDefinition> filters = definition.getFilters();
             FilterDefinition filter = new FilterDefinition();
             //重新请求路径
             filter.setName(NameUtils.normalizeFilterFactoryName(StripPrefixGatewayFilterFactory.class));
             Map<String, String> map = MapUtil.newConcurrentHashMap();
             map.put(StripPrefixGatewayFilterFactory.PARTS_KEY, "1");
             filter.setArgs(map);
             filters.add(filter);
             */
            StaticLog.info("update route : {}", definition.toString());
            dynamicRouteService.add(definition);
        }
        dynamicRouteByNacosListener(GatewayRoutersConfiguration.DATA_ID, GatewayRoutersConfiguration.ROUTE_GROUP);
    }

    /**
     * 监听Nacos下发的动态路由配置
     *
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    StaticLog.info("进行网关更新:\n\r{}", configInfo);
                    List<MyRouteDefinition> definitionList = JSONUtil.toList(configInfo, MyRouteDefinition.class);
                    StaticLog.info("update route : {}", definitionList.toString());
                    dynamicRouteService.updateList(definitionList);
                }

                @Override
                public Executor getExecutor() {
                    StaticLog.info("getExecutor\n\r");
                    return null;
                }
            });
        } catch (Exception e) {
            StaticLog.error("从nacos接收动态路由配置出错!!!", e);
        }
    }

    /**
     * 创建ConfigService
     *
     * @return
     */
    private ConfigService createConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayRoutersConfiguration.SERVER_ADDR);
            properties.setProperty("namespace", GatewayRoutersConfiguration.NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            StaticLog.error("创建ConfigService异常", e);
            return null;
        }
    }
}
