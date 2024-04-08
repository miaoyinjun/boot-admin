package org.jjche.sys.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 角色，菜单映射表
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-07-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_roles_menus")
public class RoleMenuDO implements Serializable {


    /**
     * 角色
     */
    @TableId
    private Long roleId;

    /**
     * 菜单
     */
    private Long menuId;
}
