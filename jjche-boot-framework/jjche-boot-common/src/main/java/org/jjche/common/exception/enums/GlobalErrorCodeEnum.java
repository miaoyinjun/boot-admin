package org.jjche.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.wrapper.constant.HttpStatusConstant;

/**
 * 全局错误码枚举
 * 0-999 系统异常编码保留
 *
 * 一般情况下，使用 HTTP 响应状态码 https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 * 虽然说，HTTP 响应状态码作为业务使用表达能力偏弱，但是使用在系统层面还是非常不错的
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum GlobalErrorCodeEnum implements IBaseErrorCodeEnum{
    /**
     * OK
     */
    SUCCESS(HttpStatusConstant.CODE_OK, HttpStatusConstant.MSG_OK),
    /**
     * 内部错误
     */
    UNKNOWN_ERROR(HttpStatusConstant.CODE_UNKNOWN_ERROR, HttpStatusConstant.MSG_UNKNOWN_ERROR),
    /**
     * 服务不可用
     */
    UNKNOWN_UNAVAILABLE_ERROR(HttpStatusConstant.CODE_UNAVAILABLE_ERROR, HttpStatusConstant.MSG_UNAVAILABLE_ERROR),
    /**
     * 参数错误
     */
    PARAMETER_ERROR(HttpStatusConstant.CODE_PARAMETER_ERROR, HttpStatusConstant.MSG_PARAMETER_ERROR),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(HttpStatusConstant.CODE_REQUEST_TIMEOUT, HttpStatusConstant.MSG_REQUEST_TIMEOUT),
    /**
     * 签名错误
     */
    SIGN_ERROR(HttpStatusConstant.CODE_SIGN_ERROR, HttpStatusConstant.MSG_SIGN_ERROR),
    /**
     * 请不要频繁操作
     */
    REQUEST_LIMIT(HttpStatusConstant.CODE_REQUEST_LIMIT, HttpStatusConstant.MSG_REQUEST_LIMIT),
    /**
     * 找不到请求地址
     */
    NOT_FOUND(HttpStatusConstant.CODE_NOT_FOUND, HttpStatusConstant.MSG_NOT_FOUND),
    /**
     * 不允许的请求方法
     */
    METHOD_NOT_ALLOWED(HttpStatusConstant.CODE_METHOD_NOT_ALLOWED, HttpStatusConstant.MSG_METHOD_NOT_ALLOWED),
    /**
     * 未授权
     */
    TOKEN_ERROR(HttpStatusConstant.CODE_TOKEN_ERROR, HttpStatusConstant.MSG_TOKEN_ERROR),
    /**
     * 用户名或密码错误
     */
    USERNAME_NOT_FOUND_OR_BAD_CREDENTIALS(HttpStatusConstant.CODE_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS, HttpStatusConstant.MSG_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS),
    /**
     * 账户已被禁用
     */
    USER_DISABLED(HttpStatusConstant.CODE_USER_DISABLED, HttpStatusConstant.MSG_USER_DISABLED),
    /**
     * 账户被锁定
     */
    USER_LOCKED(HttpStatusConstant.CODE_USER_LOCKED, HttpStatusConstant.MSG_USER_LOCKED),
    /**
     * 账户过期
     */
    USERNAME_EXPIRED(HttpStatusConstant.CODE_USERNAME_EXPIRED, HttpStatusConstant.MSG_USERNAME_EXPIRED),
    /**
     * 密码过期
     */
    USER_CREDENTIALS_EXPIRED(HttpStatusConstant.CODE_USER_CREDENTIALS_EXPIRED, HttpStatusConstant.MSG_USER_CREDENTIALS_EXPIRED),
    /**
     * 授权过期
     */
    TOKEN_EXPIRED(HttpStatusConstant.CODE_TOKEN_EXPIRED, HttpStatusConstant.MSG_TOKEN_EXPIRED),
    /**
     * 不允许访问
     */
    USER_ACCESS_DENIED(HttpStatusConstant.CODE_USER_ACCESS_DENIED, HttpStatusConstant.MSG_USER_ACCESS_DENIED),
    /**
     * 找不到认证信息
     */
    TOKEN_NOT_FOUND(HttpStatusConstant.CODE_TOKEN_NOT_FOUND, HttpStatusConstant.MSG_TOKEN_NOT_FOUND),
    /**
     * 白名单限制
     */
    WHITE_IP(HttpStatusConstant.CODE_WHITE_IP, HttpStatusConstant.MSG_CODE_WHITE_IP),

    /**
     * 错误码
     */
    NOT_IMPLEMENTED(501, "功能未实现/未开启");

    private final int code;
    private final String msg;
}
