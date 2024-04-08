package org.jjche.common.exception;

/**
 * <p>
 * Feign调用R异常
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-13
 */
public class FeignRException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for FeignRException.</p>
     *
     * @param msg a {@link java.lang.String} object.
     */
    public FeignRException(String msg) {
        super(msg);
    }
}
