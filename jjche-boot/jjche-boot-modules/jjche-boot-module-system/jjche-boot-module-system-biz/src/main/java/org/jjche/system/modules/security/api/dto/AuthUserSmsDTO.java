package org.jjche.system.modules.security.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 短信登录
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-11
 */
@Getter
@Setter
public class AuthUserSmsDTO {
    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号格式不正确")
    @Length(message = "手机号格式不正确", max = 11, min = 11)
    private String phone;
    @ApiModelProperty(value = "手机验证码", required = true)
    @NotBlank(message = "手机验证码不能为空")
    private String smsCode;
    @ApiModelProperty(value = "手机验证码uuid", required = true)
    @NotBlank(message = "手机验证码uuid不能为空")
    private String smsCodeUuid;
}
