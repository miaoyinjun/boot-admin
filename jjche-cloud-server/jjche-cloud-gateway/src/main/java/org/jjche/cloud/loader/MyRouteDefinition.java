package org.jjche.cloud.loader;

import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * <p>
 * 自定义RouteDefinition
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-01
 */
public class MyRouteDefinition extends RouteDefinition {
    /**
     * 路由状态
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
