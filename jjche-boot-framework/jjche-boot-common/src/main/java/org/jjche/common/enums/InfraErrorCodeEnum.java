package org.jjche.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.exception.enums.IBaseErrorCodeEnum;

/**
 * DEMO逻辑错误码枚举
 *
 */
@Getter
@AllArgsConstructor
public enum InfraErrorCodeEnum implements IBaseErrorCodeEnum {
    // ==========  通用模块 1-001-000-000 ==========
    COMMON_APP_NOT_FOUND_ERROR(1001000000, "找不到SpringBootApplication注解"),
    COMMON_FILE_SIZE_ERROR(1001000001, "文件超出规定大小"),
    COMMON_RANGE_PARAM_ERROR(1001000002, "区间参数长度必须是2"),
    COMMON_COMMIT_REPEAT_ERROR(1001000003, "请勿重复提交"),

    // ==========  加密模块 1-001-001-000 ==========
    ENC_APPID_ERROR(1001001000, "appId无效"),
    ENC_TIMESTAMP_ERROR(1001001001, "timestamp无效"),
    ENC_NONCE_ERROR(1001001002, "nonce无效"),
    ENC_SIGN_ERROR(1001001003, "sign无效");


    private final int code;
    private final String msg;
}
