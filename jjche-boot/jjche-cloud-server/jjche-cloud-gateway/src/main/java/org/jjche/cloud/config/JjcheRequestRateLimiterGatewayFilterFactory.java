package org.jjche.cloud.config;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.wrapper.response.R;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>
 * 路由限流自定义返回json配置
 * </p>
 *
 * @author miaoyj
 * @since 2022-12-28
 */
@Component
public class JjcheRequestRateLimiterGatewayFilterFactory extends RequestRateLimiterGatewayFilterFactory {
    private final RateLimiter defaultRateLimiter;
    private final KeyResolver defaultKeyResolver;

    public JjcheRequestRateLimiterGatewayFilterFactory(RateLimiter defaultRateLimiter, KeyResolver defaultKeyResolver) {
        super(defaultRateLimiter, defaultKeyResolver);
        this.defaultRateLimiter = defaultRateLimiter;
        this.defaultKeyResolver = defaultKeyResolver;
    }

    @Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = getOrDefault(config.getKeyResolver(), defaultKeyResolver);
        RateLimiter<Object> limiter = getOrDefault(config.getRateLimiter(), defaultRateLimiter);
        return (exchange, chain) -> resolver.resolve(exchange).flatMap(key -> {
            String routeId = config.getRouteId();
            if (routeId == null) {
                Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                routeId = route.getId();
            }

            String finalRouteId = routeId;
            return limiter.isAllowed(routeId, key).flatMap(response -> {

                for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                    exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
                }

                if (response.isAllowed()) {
                    return chain.filter(exchange);
                }

                StaticLog.warn("已限流: {}", finalRouteId);
                ServerHttpResponse httpResponse = exchange.getResponse();
                httpResponse.setStatusCode(config.getStatusCode());
                if (!httpResponse.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE)) {
                    httpResponse.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                }
                String limitMsg = JSONUtil.toJsonStr(R.requestLimit());
                DataBuffer buffer = httpResponse.bufferFactory().wrap(limitMsg.getBytes(StandardCharsets.UTF_8));
                return httpResponse.writeWith(Mono.just(buffer));
            });
        });
    }

    private <T> T getOrDefault(T configValue, T defaultValue) {
        return (configValue != null) ? configValue : defaultValue;
    }
}