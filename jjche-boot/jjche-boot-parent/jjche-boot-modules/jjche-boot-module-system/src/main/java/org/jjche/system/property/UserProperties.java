package org.jjche.system.property;

import lombok.Data;

/**
 * <p>
 * 用户配置属性类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2021-01-05
 */
@Data
public class UserProperties {
    /**
     * 密码
     */
    private PasswordProperties password;

    /**
     * 账号
     */
    private AccountProperties account;
}
