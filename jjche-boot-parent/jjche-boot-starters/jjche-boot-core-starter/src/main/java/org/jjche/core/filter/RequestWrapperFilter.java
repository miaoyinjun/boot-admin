package org.jjche.core.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 通过过滤器将ServletRequest封装成ContentCachingRequestWrapper，body被读取后，会被它缓存。
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-29
 */
@Component
public class RequestWrapperFilter extends OncePerRequestFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new ContentCachingRequestWrapper(httpServletRequest), httpServletResponse);
    }
}