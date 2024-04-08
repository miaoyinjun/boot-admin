package org.jjche.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 应用密钥
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Data
public class SecurityAppKeyBasicVO implements Serializable {
    @ApiModelProperty(value = "应用密钥")
    private String appSecret;
    @ApiModelProperty(value = "加密密钥")
    private String encKey;
    @ApiModelProperty(value = "地址")
    private String urls;
    @ApiModelProperty(value = "白名单")
    private String whiteIp;
    @ApiModelProperty(value = "限速（N/秒）0不限制")
    private Integer limitCount;
}