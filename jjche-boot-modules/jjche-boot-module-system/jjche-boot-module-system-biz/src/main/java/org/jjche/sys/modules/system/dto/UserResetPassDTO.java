package org.jjche.sys.modules.system.dto;

import lombok.Data;

/**
 * <p>
 * 修改密码入参
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-12-21
 */
@Data
public class UserResetPassDTO {
    private String username;
    private String newPass;
}
