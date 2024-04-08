package org.jjche.security.handler;

import org.jjche.common.util.HttpUtil;
import org.jjche.common.wrapper.response.R;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 认证异常
 * </p>
 * <p>
 * 解决不提供token时，返回403问题
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        HttpUtil.printJson(response, R.tokenError());
    }
}
