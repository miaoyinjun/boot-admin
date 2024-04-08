package org.jjche.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import org.jjche.common.util.StrUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 获取 HttpServletRequest
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-24
 */
public class RequestHolder {

    /**
     * <p>getHttpServletRequest.</p>
     *
     * @return a {@link javax.servlet.http.HttpServletRequest} object.
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * <p>
     * 获取头部信息
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static String getHeader(String headerName) {
        return getHttpServletRequest().getHeader(headerName);
    }

    /**
     * <p>
     * 获取头部信息
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static boolean getBooleanHeader(String headerName) {
        String str = getHeader(headerName);
        Boolean result = false;
        if (StrUtil.isNotBlank(str)) {
            result = Boolean.parseBoolean(str);
        }
        return result;
    }

    /**
     * <p>
     * 获取头部信息
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static Long getLongHeader(String headerName) {
        String str = getHeader(headerName);
        Long result = 0L;
        if (StrUtil.isNotBlank(str)) {
            result = Long.parseLong(str);
        }
        return result;
    }

    /**
     * <p>
     * 获取头部信息
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static Long getHeaderLong(String headerName) {
        return Convert.toLong(getHeader(headerName));
    }

    /**
     * <p>
     * 获取头部集合
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static Set<String> getHeaders(String headerName) {
        Enumeration<String> values = getHttpServletRequest().getHeaders(headerName);
        return CollUtil.newHashSet(true, values);
    }

    /**
     * <p>
     * 获取头部集合
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static Set<Long> getLongHeaders(String headerName) {
        Set<Long> result = new HashSet<>();
        Set<String> strs = getHeaders(headerName);
        if (CollUtil.isNotEmpty(strs)) {
            result = strs.stream().map(o -> Long.parseLong(o)).collect(Collectors.toSet());
        }
        return result;
    }
}
