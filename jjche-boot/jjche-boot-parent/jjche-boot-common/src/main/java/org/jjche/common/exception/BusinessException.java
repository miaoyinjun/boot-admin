package org.jjche.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jjche.common.exception.enums.IBaseErrorCodeEnum;

/**
 * <p>
 * 业务逻辑异常
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    /**
     * 业务错误码
     *
     * @see BusinessErrorCodeRange
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public BusinessException() {
    }

    public BusinessException(IBaseErrorCodeEnum errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public BusinessException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BusinessException setMessage(String message) {
        this.message = message;
        return this;
    }

}
