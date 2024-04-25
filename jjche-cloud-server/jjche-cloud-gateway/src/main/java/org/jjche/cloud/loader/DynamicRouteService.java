package org.jjche.cloud.loader;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <p>
 * 动态更新路由网关service
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-01
 */
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private InMemoryRouteDefinitionRepository repository;
    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;
    /**
     * 发布事件
     */

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    public synchronized void delete(String id) {
        try {
            repository.delete(Mono.just(id)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * 更新路由
     *
     * @param definitions
     * @return
     */
    public synchronized String updateList(List<MyRouteDefinition> definitions) {
        StaticLog.info("gateway update route {}", definitions);
        // 删除缓存routerDefinition
        List<RouteDefinition> routeDefinitionsExits = routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
        if (CollUtil.isNotEmpty(routeDefinitionsExits)) {
            routeDefinitionsExits.forEach(routeDefinition -> {
                StaticLog.info("delete routeDefinition:{}", routeDefinition);
                delete(routeDefinition.getId());
            });
        }
        definitions.forEach(definition -> {
            updateById(definition);
        });
        return "success";
    }

    /**
     * 更新路由
     *
     * @param definition
     * @return
     */
    public synchronized String updateById(RouteDefinition definition) {
        try {
            StaticLog.info("gateway update route {}", definition);
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception e) {
            return "update fail,not find route  routeId: " + definition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "update route fail";
        }
    }

    /**
     * 增加路由
     *
     * @param definition
     * @return
     */
    public synchronized String add(RouteDefinition definition) {
        StaticLog.info("gateway add route {}", definition);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        //todo 会卡住
//        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }
}