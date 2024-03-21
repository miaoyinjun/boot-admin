package org.jjche.sys.modules.app.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.jjche.common.base.BaseDTO;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 应用密钥
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityAppKeyDTO extends BaseQueryCriteriaDTO implements BaseDTO {
    @ApiModelProperty(value = "ID")
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 50, message = "名称最大长度不能超过50")
    private String name;
    @ApiModelProperty(value = "描述")
    @Length(max = 255, message = "描述最大长度不能超过255")
    private String comment;
    @ApiModelProperty(value = "应用id")
    @NotBlank(message = "应用id不能为空")
    @Length(max = 255, message = "应用id最大长度不能超过255")
    private String appId;
    @ApiModelProperty(value = "应用密钥")
    @NotBlank(message = "应用密钥不能为空")
    @Length(max = 255, message = "应用密钥最大长度不能超过255")
    private String appSecret;
    @ApiModelProperty(value = "加密密钥")
    @NotBlank(message = "加密密钥不能为空")
    @Length(max = 255, message = "加密密钥最大长度不能超过255")
    private String encKey;
    private Boolean enabled;
    @ApiModelProperty(value = "地址")
    @NotBlank(message = "地址不能为空")
    @Length(max = 500, message = "地址最大长度不能超过500")
    private String urls;
    @ApiModelProperty(value = "白名单")
    @Length(max = 500, message = "白名单最大长度不能超过500")
    private String whiteIp;
    @ApiModelProperty(value = "限速（N/秒）0不限制")
    private Integer limitCount;
}
