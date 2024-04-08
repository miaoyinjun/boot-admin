package org.jjche.core.exception;

import lombok.Getter;

/**
 * <p>
 * 获取授权信息错误
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-24
 */
@Getter
public class AuthenticationTokenNotFoundException extends RuntimeException {
    /**
     * <p>Constructor for AuthenticationTokenNotFoundException.</p>
     *
     * @param msg a {@link java.lang.String} object.
     */
    public AuthenticationTokenNotFoundException(String msg) {
        super(msg);
    }
}
