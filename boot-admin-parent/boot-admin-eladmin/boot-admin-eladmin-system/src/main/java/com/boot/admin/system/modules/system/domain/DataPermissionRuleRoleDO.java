package com.boot.admin.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.mybatis.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 数据规则权限
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-01
 * @version 1.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_data_permission_rule_role")
public class DataPermissionRuleRoleDO extends BaseEntity {
    /**
    * 角色ID
    */
    private Long roleId;
    /**
    * 菜单ID
    */
    private Long menuId;
    /**
    * 数据规则权限ID
    */
    private Long dataPermissionRuleId;
}
