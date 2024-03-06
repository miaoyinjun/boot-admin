package org.jjche.filter.enc.api.enums;

import org.jjche.common.constant.FilterEncConstant;

/**
 * <p>
 * 加密定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-12
 */
public enum FilterEncEnum {
    /**
     * 应用标识
     */
    APP_ID(FilterEncConstant.APP_ID, FilterEncConstant.APP_ID_DESC, "appId无效"),
    /**
     * Unix时间戳(毫秒)
     */
    TIMESTAMP(FilterEncConstant.TIMESTAMP, FilterEncConstant.TIMESTAMP_DESC, "timestamp无效"),
    /**
     * 随机数
     */
    NONCE(FilterEncConstant.NONCE, FilterEncConstant.NONCE_DESC, "nonce无效"),
    /**
     * 签名
     */
    SIGN(FilterEncConstant.SIGN, FilterEncConstant.SIGN_DESC, "sign无效"),
    ;
    /**
     * 标识
     */
    private String key;

    /**
     * 描述
     */
    private String des;

    /**
     * 错误信息
     */
    private String errMsg;

    FilterEncEnum(String key, String des, String errMsg) {
        this.key = key;
        this.des = des;
        this.errMsg = errMsg;
    }

    /**
     * <p>Getter for the field <code>key</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>Getter for the field <code>des</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDes() {
        return des;
    }

    /**
     * <p>Getter for the field <code>errMsg</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getErrMsg() {
        return errMsg;
    }
}
