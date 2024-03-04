package org.jjche.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 角色，部门映射表
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-07-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_roles_depts")
public class RoleDeptDO implements Serializable {

    /**
     * 角色
     */
    @TableId
    private Long roleId;

    /**
     * 部门
     */
    private Long deptId;
}
