package org.jjche.sys.modules.security.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 获取手机验证码
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-03-23
 */
@Data
public class SmsCodeDTO {
    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号格式不正确")
    @Length(message = "手机号格式不正确", max = 11, min = 11)
    private String phone;
    @ApiModelProperty(value = "图形验证码", required = true)
    @NotBlank(message = "图形验证码不能为空")
    private String captchaCode;
    @ApiModelProperty(value = "图形验证码uuid", required = true)
    @NotBlank(message = "图形验证码uuid不能为空")
    private String captchaCodeUuid;
}
