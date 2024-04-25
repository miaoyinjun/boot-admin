package org.jjche.cloud.fallback.sentinel;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.common.wrapper.response.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;

/**
 * <p>
 * Sentinel异常自定义
 * </p>
 *
 * @author miaoyj
 * @since 2022-12-30
 */
@Configuration
public class GatewaySentinelExceptionConfig {

    @PostConstruct
    public void init() {

        BlockRequestHandler blockRequestHandler = (serverWebExchange, ex) -> {
            R res = ThrowableUtil.getSentinelError(ex);
            String limitMsg = JSONUtil.toJsonStr(res);
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).contentType(MediaType.APPLICATION_JSON).bodyValue(limitMsg);
        };

        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
}