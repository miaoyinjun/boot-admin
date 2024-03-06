package org.jjche.system.modules.security.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * <p>AuthUserDto class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-30
 */
@Getter
@Setter
public class AuthUserDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    /**
     * 验证码-手输
     */
    private String code;
    /**
     * 验证码-滑动
     */
    private String captchaVerification;

    private String uuid = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{username=" + username + ", password= ******}";
    }
}
