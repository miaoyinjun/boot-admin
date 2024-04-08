package org.jjche.sys.modules.system.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 角色-用户
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-22
 */
@Data
public class UserRoleDTO {
    @NotNull(message = "角色不能为空")
    private Long roleId;
    @NotNull(message = "用户不能为空")
    private Long userId;
}
