package org.jjche.sys.modules.app.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 应用密钥
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Data
public class SecurityAppKeyVO implements Serializable {
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "描述")
    private String comment;
    @ApiModelProperty(value = "应用id")
    private String appId;
    @ApiModelProperty(value = "应用密钥")
    private String appSecret;
    @ApiModelProperty(value = "加密密钥")
    private String encKey;
    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Boolean enabled;
    @ApiModelProperty(value = "地址")
    private String urls;
    @ApiModelProperty(value = "白名单")
    private String whiteIp;
    @ApiModelProperty(value = "限速（N/秒）0不限制")
    private Integer limitCount;
    @ApiModelProperty(value = "创建时间")
    private Timestamp gmtCreate;
}
