package org.jjche.common.wrapper.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.abs.AbstractR;
import org.jjche.common.exception.enums.GlobalErrorCodeEnum;
import org.jjche.common.wrapper.constant.HttpStatusConstant;

import java.io.Serializable;


/**
 * <p>
 * response包装操作类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class R<T> extends AbstractR implements Serializable {

    /**
     * 编号
     */
    @ApiModelProperty(value = HttpStatusConstant.CODE_OK
            + ":" + HttpStatusConstant.MSG_OK + ";",
            example = HttpStatusConstant.CODE_OK + "")
    private int code;

    /**
     * 信息
     */
    @ApiModelProperty(example = HttpStatusConstant.MSG_OK + "")
    private String message;

    /**
     * 结果数据
     */
    private T data;

    /**
     * 成功
     */
    private boolean success;

    /**
     * Instantiates a new wrapper. default code=200
     */
    public R() {
        this(GlobalErrorCodeEnum.SUCCESS.getCode(), GlobalErrorCodeEnum.SUCCESS.getMsg());
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param code    the code
     * @param message the message
     */
    R(int code, String message) {
        this(code, message, null);
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param code    the code
     * @param message the message
     * @param result  the result
     */
    R(int code, String message, T result) {
        super();
        this.code(code).message(message).data(result);
    }

    /**
     * Wrap.
     *
     * @param <E>      the element type
     * @param codeEnum the code enum
     * @param message  the message
     * @return the wrapper
     */
    public static <E> R<E> wrap(GlobalErrorCodeEnum codeEnum, String message) {
        return new R(codeEnum.getCode(), message);
    }

    /**
     * Wrap.
     *
     * @param <E>      the element type
     * @param codeEnum the codeEnum
     * @return the wrapper
     */
    public static <E> R<E> wrap(GlobalErrorCodeEnum codeEnum) {
        return wrap(codeEnum, codeEnum.getMsg());
    }

    public static <E> R<E> wrap(int code, String message) {
        return new R(code, message);
    }

    public static <E> R<E> wrap(int code) {
        return wrap(code, null);
    }

    /**
     * <p>
     * 内部错误
     * </p>
     *
     * @param <E> a E object.
     * @return 详细
     */
    public static <E> R<E> error() {
        return wrap(GlobalErrorCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * <p>
     * 服务不可用
     * </p>
     *
     * @param <E> a E object.
     * @return 详细
     */
    public static <E> R<E> errorServiceUnAvailable() {
        return wrap(GlobalErrorCodeEnum.UNKNOWN_UNAVAILABLE_ERROR);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param msg 错误信息
     * @param <E> a E object.
     * @return 详细
     */
    public static <E> R<E> parameterError(String msg) {
        return wrap(GlobalErrorCodeEnum.PARAMETER_ERROR, msg);
    }

    /**
     * <p>
     * 请求超时
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> requestTimeout() {
        return wrap(GlobalErrorCodeEnum.REQUEST_TIMEOUT);
    }

    /**
     * <p>
     * 签名错误
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> signError() {
        return wrap(GlobalErrorCodeEnum.SIGN_ERROR);
    }

    /**
     * <p>
     * 不要频繁操作
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> requestLimit() {
        return wrap(GlobalErrorCodeEnum.REQUEST_LIMIT);
    }


    /**
     * <p>
     * 找不到请求地址
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> notFound() {
        return wrap(GlobalErrorCodeEnum.NOT_FOUND);
    }


    /**
     * <p>
     * 不允许的请求方法
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> methodNotAllowed() {
        return wrap(GlobalErrorCodeEnum.METHOD_NOT_ALLOWED);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> ok() {
        return new R<>();
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param o   对象
     * @param <E> a E object.
     * @return 详细
     */
    public static <E> R<E> ok(E o) {
        return new R<>(GlobalErrorCodeEnum.SUCCESS.getCode(), GlobalErrorCodeEnum.SUCCESS.getMsg(), o);
    }

    /**
     * <p>
     * 用户名或密码错误
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> userError() {
        return wrap(GlobalErrorCodeEnum.USERNAME_NOT_FOUND_OR_BAD_CREDENTIALS);
    }

    /**
     * <p>
     * 账户已被禁用
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> userDisabledError() {
        return wrap(GlobalErrorCodeEnum.USER_DISABLED);
    }

    /**
     * <p>
     * 账户被锁定
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> userLockedError() {
        return wrap(GlobalErrorCodeEnum.USER_LOCKED);
    }

    /**
     * <p>
     * 账户过期
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> userNameExpiredError() {
        return wrap(GlobalErrorCodeEnum.USERNAME_EXPIRED);
    }

    /**
     * <p>
     * 密码过期
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> userCredentialsExpiredError() {
        return wrap(GlobalErrorCodeEnum.USER_CREDENTIALS_EXPIRED);
    }

    /**
     * <p>
     * 授权过期
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> tokenExpiredError() {
        return wrap(GlobalErrorCodeEnum.TOKEN_EXPIRED);
    }

    /**
     * <p>
     * 未授权，token为空，token无法解析
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> tokenError() {
        return wrap(GlobalErrorCodeEnum.TOKEN_ERROR);
    }

    /**
     * <p>
     * 不允许访问
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     */
    public static <E> R<E> userAccessDeniedError() {
        return wrap(GlobalErrorCodeEnum.USER_ACCESS_DENIED);
    }

    /**
     * <p>
     * 找不到认证信息
     * </p>
     *
     * @param <E> a E object.
     * @return /
     */
    public static <E> R<E> tokenNotFoundError() {
        return wrap(GlobalErrorCodeEnum.TOKEN_NOT_FOUND);
    }

    /**
     * <p>
     * 白名单限制
     * </p>
     *
     * @return /
     */
    public static <E> R<E> whiteIpError() {
        return wrap(GlobalErrorCodeEnum.WHITE_IP);
    }

    /**
     * Sets the 编号 , 返回自身的引用.
     *
     * @param code the new 编号
     * @return the wrapper
     */
    private R<T> code(int code) {
        this.setCode(code);
        return this;
    }

    /**
     * Sets the 信息 , 返回自身的引用.
     *
     * @param message the new 信息
     * @return the wrapper
     */
    private R<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * Sets the 结果数据 , 返回自身的引用.
     *
     * @param data the new 结果数据
     * @return the wrapper
     */
    public R<T> data(T data) {
        this.setData(data);
        return this;
    }

    /**
     * 判断是否成功： 依据 R.SUCCESS_CODE == this.code
     *
     * @return code =200,true;否则 false.
     */
    public boolean getSuccess() {
        return GlobalErrorCodeEnum.SUCCESS.getCode() == this.code;
    }
}
