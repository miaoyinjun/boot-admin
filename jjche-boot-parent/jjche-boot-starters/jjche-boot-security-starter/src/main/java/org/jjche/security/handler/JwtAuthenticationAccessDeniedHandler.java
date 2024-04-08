package org.jjche.security.handler;

import org.jjche.common.util.HttpUtil;
import org.jjche.common.wrapper.response.R;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 登陆状态下，不允许访问
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Component
public class JwtAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        HttpUtil.printJson(response, R.userAccessDeniedError());
    }
}
