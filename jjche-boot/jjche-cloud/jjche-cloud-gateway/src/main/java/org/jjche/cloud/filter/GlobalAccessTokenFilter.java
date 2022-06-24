package org.jjche.cloud.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.api.CommonAPI;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.util.HttpUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * <p>
 * 全局token
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-01
 */
@Component
public class GlobalAccessTokenFilter implements GlobalFilter, Ordered {
    private static CommonAPI commonAPI;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst(SecurityConstant.HEADER_AUTH);
        if (StrUtil.isNotBlank(token)) {
            if (ObjectUtil.isNull(commonAPI)) {
                commonAPI = SpringUtil.getBean(CommonAPI.class);
            }
            CompletableFuture<JwtUserDto> completableFuture = CompletableFuture.supplyAsync(() -> {
                return commonAPI.getUserDetails(token);
            });
            JwtUserDto userDetails = null;
            try {
                userDetails = completableFuture.get();
                // 设置用户信息到请求
                Consumer<HttpHeaders> headersConsumer = getUserHeadersConsumer(userDetails);
                ServerHttpRequest mutableReq = exchange.getRequest().mutate().headers(headersConsumer).build();
                exchange = exchange.mutate().request(mutableReq).build();
            } catch (Exception ex) {
                StaticLog.warn("调用验证用户接口错误{}", ex);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }


    /**
     * <p>
     * 获取用户信息到header
     * </p>
     *
     * @param userDetails 用户信息
     * @return /
     */
    private Consumer<HttpHeaders> getUserHeadersConsumer(JwtUserDto userDetails) {
        return httpHeaders -> {
            Map<String, Object> userHeaders = HttpUtil.getUserHeaders(userDetails);
            for (String key : userHeaders.keySet()) {
                Object value = userHeaders.get(key);
                if (value instanceof Collection) {
                    httpHeaders.addAll(key, (List<? extends String>) value);
                } else {
                    httpHeaders.add(key, String.valueOf(value));
                }
            }
        };
    }
}