package org.jjche.cloud.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jjche.common.constant.MediaTypeConstant;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.common.wrapper.response.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Sentinel异常自定义
 * </p>
 *
 * @author miaoyj
 * @since 2022-12-30
 */
@Configuration
public class SentinelBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        R res = ThrowableUtil.getSentinelError(e);
        httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        httpServletResponse.setContentType(MediaTypeConstant.APPLICATION_JSON_UTF8);
        new ObjectMapper()
                .writeValue(httpServletResponse.getWriter(), res);
    }

    @PostConstruct
    public void init() {
        new SentinelBlockHandler();
    }

}