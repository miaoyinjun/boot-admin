package org.jjche.common.wrapper.constant;

/**
 * <p>
 * httpStatus状态码定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-09
 */
public interface HttpStatusConstant {

    /**
     * 服务内部错误 {@value}
     */
    String HTTP_INTERNAL_ERROR = "服务内部错误";

    /**
     * 验证错误 {@value}
     */
    String HTTP_BAD_REQUEST = "验证错误";

    /**
     * 授权过期，不允许访问 {@value}
     */
    String HTTP_FORBIDDEN = "授权过期，不允许访问";


    /**
     * 成功 {@value}
     */
    int CODE_OK = 2001;

    /**
     * 成功 {@value}
     */
    String MSG_OK = "OK";

    /**
     * 内部错误 {@value}
     */
    int CODE_UNKNOWN_ERROR = 5001;

    /**
     * 内部错误 {@value}
     */
    String MSG_UNKNOWN_ERROR = "未知异常，请稍后重试";

    /**
     * 服务不可用 {@value}
     */
    int CODE_UNAVAILABLE_ERROR = 5031;

    /**
     * 服务不可用 {@value}
     */
    String MSG_UNAVAILABLE_ERROR = "服务不可用，请稍后重试";

    /**
     * 参数错误 {@value}
     */
    int CODE_PARAMETER_ERROR = 4001;

    /**
     * 参数错误 {@value}
     */
    String MSG_PARAMETER_ERROR = "参数错误";

    /**
     * 请求超时 {@value}
     */
    int CODE_REQUEST_TIMEOUT = 4033;

    /**
     * 请求超时 {@value}
     */
    String MSG_REQUEST_TIMEOUT = "请求超时";

    /**
     * 签名错误 {@value}
     */
    int CODE_SIGN_ERROR = 4032;

    /**
     * 签名错误 {@value}
     */
    String MSG_SIGN_ERROR = "签名无效";

    /**
     * 请不要频繁操作 {@value}
     */
    int CODE_REQUEST_LIMIT = 4035;

    /**
     * 请不要频繁操作 {@value}
     */
    String MSG_REQUEST_LIMIT = "请不要频繁操作";


    /**
     * 404 {@value}
     */
    int CODE_NOT_FOUND = 4041;

    /**
     * 404 {@value}
     */
    String MSG_NOT_FOUND = "找不到请求地址";

    /**
     * 405 {@value}
     */
    int CODE_METHOD_NOT_ALLOWED = 4051;

    /**
     * 405 {@value}
     */
    String MSG_METHOD_NOT_ALLOWED = "不允许的请求方法";

    /**
     * 未授权 {@value}
     */
    int CODE_TOKEN_ERROR = 4011;

    /**
     * 未授权 {@value}
     */
    String MSG_TOKEN_ERROR = "未授权或授权过期";

    /**
     * 授权过期 {@value}
     */
    int CODE_TOKEN_EXPIRED = 4012;

    /**
     * 授权过期 {@value}
     */
    String MSG_TOKEN_EXPIRED = "授权过期";

    /**
     * 不允许访问 {@value}
     */
    int CODE_USER_ACCESS_DENIED = 4031;

    /**
     * 不允许访问 {@value}
     */
    String MSG_USER_ACCESS_DENIED = "不允许访问";

    /**
     * 白名单限制 {@value}
     */
    int CODE_WHITE_IP = 4034;

    /**
     * 白名单限制 {@value}
     */
    String MSG_CODE_WHITE_IP = "白名单限制";

    /**
     * 找不到认证信息 {@value}
     */
    int CODE_TOKEN_NOT_FOUND = 4013;

    /**
     * 找不到认证信息 {@value}
     */
    String MSG_TOKEN_NOT_FOUND = "找不到认证信息";

    /**
     * 用户名或密码错误 {@value}
     */
    int CODE_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS = 4003;

    /**
     * 用户名或密码错误 {@value}
     */
    String MSG_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS = "用户名或密码错误";

    /**
     * 账户已被禁用 {@value}
     */
    int CODE_USER_DISABLED = 4007;

    /**
     * 账户已被禁用 {@value}
     */
    String MSG_USER_DISABLED = "账户已被禁用，请联系管理员";

    /**
     * 账户已被锁定 {@value}
     */
    int CODE_USER_LOCKED = 4008;

    /**
     * 账户已被锁定 {@value}
     */
    String MSG_USER_LOCKED = "账户已被锁定，请联系管理员";

    /**
     * 账户已过期 {@value}
     */
    int CODE_USERNAME_EXPIRED = 4009;

    /**
     * 账户已过期 {@value}
     */
    String MSG_USERNAME_EXPIRED = "账户已过期，请联系管理员";

    /**
     * 密码已过期 {@value}
     */
    int CODE_USER_CREDENTIALS_EXPIRED = 4010;

    /**
     * 密码过期 {@value}
     */
    String MSG_USER_CREDENTIALS_EXPIRED = "密码已过期，请联系管理员";

    /**
     * 流控异常 {@value}
     */
    int CODE_SENTINEL_FLOW = 4291;

    /**
     * 流控异常 {@value}
     */
    String MSG_SENTINEL_FLOW = "访问频繁，请稍候再试";

    /**
     * 热点参数异常 {@value}
     */
    int CODE_SENTINEL_PARAM_FLOW = 4292;

    /**
     * 热点参数异常 {@value}
     */
    String MSG_SENTINEL_PARAM_FLOW = "热点参数限流";

    /**
     * 系统规则限流或降级 {@value}
     */
    int CODE_SENTINEL_SYSTEM_BLOCK = 4293;

    /**
     * 系统规则限流或降级 {@value}
     */
    String MSG_SENTINEL_SYSTEM_BLOCK = "系统规则限流或降级";

    /**
     * 授权规则不通过 {@value}
     */
    int CODE_SENTINEL_AUTHORITY = 4294;

    /**
     * 授权规则不通过 {@value}
     */
    String MSG_SENTINEL_AUTHORITY = "授权规则不通过";

    /**
     * 服务降级 {@value}
     */
    int CODE_SENTINEL_DEGRADE = 4295;

    /**
     * 流控异常 {@value}
     */
    String MSG_SENTINEL_DEGRADE = "服务降级";

    /**
     * 未知异常 {@value}
     */
    int CODE_SENTINEL_UNKNOWN = 4296;

    /**
     * 流控异常 {@value}
     */
    String MSG_SENTINEL_UNKNOWN = "未知异常";

    /**
     * 业务异常 {@value}
     */
    String MSG_BUSINESS_ERROR = "业务验证异常";
}
