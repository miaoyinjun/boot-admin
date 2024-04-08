package org.jjche.sys.modules.security.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.jjche.common.dto.JwtUserDTO;

import java.io.Serializable;

/**
 * <p>
 * 登录出参
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-21
 */
@Data
@AllArgsConstructor
public class LoginVO implements Serializable {
    JwtUserDTO user;
    private String token;
}
