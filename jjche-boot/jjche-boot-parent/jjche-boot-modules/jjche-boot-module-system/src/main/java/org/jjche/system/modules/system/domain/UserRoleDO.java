package org.jjche.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 用户，角色映射表
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-07-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_users_roles")
public class UserRoleDO implements Serializable {

    /**
     * 用户
     */
    @TableId
    private Long userId;

    /**
     * 岗位
     */
    private Long roleId;
}
