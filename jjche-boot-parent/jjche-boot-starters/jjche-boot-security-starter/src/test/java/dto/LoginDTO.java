package dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 登录入参
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
@NoArgsConstructor
public class LoginDTO implements Serializable {
    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;
    @ApiModelProperty(value = "密码", example = "admin")
    private String password;
}
