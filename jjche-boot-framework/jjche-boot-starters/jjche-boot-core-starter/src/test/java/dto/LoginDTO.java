package dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 登录入参，所有需要验证的属性，message不能为空
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class LoginDTO implements Serializable {
    public static final String NICKNAME_MSG = "用户昵称不能为空";
    public static final String PHONE_MSG = "手机号不能为空";
    @NotBlank(message = NICKNAME_MSG)
    private String nickname;
    @NotBlank(message = PHONE_MSG)
    @Length(min = 11, max = 11, message = "手机号格式不正确")
    private String telephone;
}
