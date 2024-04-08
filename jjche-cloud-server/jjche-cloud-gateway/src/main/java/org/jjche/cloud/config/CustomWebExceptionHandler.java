package org.jjche.cloud.config;

import cn.hutool.log.StaticLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.wrapper.response.R;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 全局异常
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-01
 */
@Order(-1)
@RequiredArgsConstructor
@Component
public class CustomWebExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // header set
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    try {
                        HttpStatus status = response.getStatusCode();
                        R r = R.error();
                        if (HttpStatus.SERVICE_UNAVAILABLE == status) {
                            r = R.errorServiceUnAvailable();
                        }
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(r));
                    } catch (JsonProcessingException e) {
                        StaticLog.warn("Error writing response{}", ex);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}