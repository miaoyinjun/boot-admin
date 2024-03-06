package org.jjche.system.modules.security.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 登录验证码出参
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-21
 */
@Data
@AllArgsConstructor
public class LoginCodeVO implements Serializable {
    private String img;
    private String uuid;
}
