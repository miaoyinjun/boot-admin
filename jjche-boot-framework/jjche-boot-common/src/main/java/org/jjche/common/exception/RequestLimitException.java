package org.jjche.common.exception;

/**
 * <p>
 * 请求次数限制
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-08
 */
public class RequestLimitException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for RequestTimeoutException.</p>
     */
    public RequestLimitException() {
    }
}
