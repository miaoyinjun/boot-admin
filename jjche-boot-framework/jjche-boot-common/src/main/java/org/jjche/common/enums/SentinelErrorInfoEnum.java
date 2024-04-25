package org.jjche.common.enums;

import org.jjche.common.wrapper.constant.HttpStatusConstant;

/**
 * <p>
 * Sentinel异常枚举
 * </p>
 *
 * @author miaoyj
 * @since 2022-12-30
 */
public enum SentinelErrorInfoEnum {

    /**
     * 流控异常
     */
    FlowException(HttpStatusConstant.CODE_SENTINEL_FLOW, HttpStatusConstant.MSG_SENTINEL_FLOW),

    /**
     * 热点参数异常
     */
    ParamFlowException(HttpStatusConstant.CODE_SENTINEL_PARAM_FLOW, HttpStatusConstant.MSG_SENTINEL_PARAM_FLOW),

    /**
     * 系统规则限流或降级
     */
    SystemBlockException(HttpStatusConstant.CODE_SENTINEL_SYSTEM_BLOCK, HttpStatusConstant.MSG_SENTINEL_SYSTEM_BLOCK),

    /**
     * 授权规则不通过
     */
    AuthorityException(HttpStatusConstant.CODE_SENTINEL_AUTHORITY, HttpStatusConstant.MSG_SENTINEL_AUTHORITY),

    /**
     * 服务降级
     */
    DegradeException(HttpStatusConstant.CODE_SENTINEL_DEGRADE, HttpStatusConstant.MSG_SENTINEL_DEGRADE),

    /**
     * 未知异常
     */
    UnknownError(HttpStatusConstant.CODE_SENTINEL_UNKNOWN, HttpStatusConstant.MSG_SENTINEL_UNKNOWN);


    /**
     * 错误信息
     */
    String error;

    /**
     * 错误代码
     */
    Integer code;


    /**
     * 构造器
     *
     * @param code  错误代码
     * @param error 错误信息
     */
    SentinelErrorInfoEnum(Integer code, String error) {
        this.code = code;
        this.error = error;
    }

    /**
     * 根据异常名称匹配
     *
     * @param throwable 异常
     * @return String 错误信息
     */
    public static SentinelErrorInfoEnum getErrorByException(Throwable throwable) {
        if (throwable == null) {
            return null;
        }

        String exceptionClass = throwable.getClass().getSimpleName();
        for (SentinelErrorInfoEnum e : SentinelErrorInfoEnum.values()) {
            if (exceptionClass.equals(e.name())) {
                return e;
            }
        }
        return null;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
