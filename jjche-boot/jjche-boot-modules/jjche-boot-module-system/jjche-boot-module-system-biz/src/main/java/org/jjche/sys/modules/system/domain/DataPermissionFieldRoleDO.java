package org.jjche.sys.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>
 * 数据字段角色
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-04
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_data_permission_field_role")
public class DataPermissionFieldRoleDO extends BaseEntity {
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 数据字段权限ID
     */
    private Long dataPermissionFieldId;

    /**
     * 可见
     */
    private Boolean isAccessible;
    /**
     * 编辑
     */
    private Boolean isEditable;
}
