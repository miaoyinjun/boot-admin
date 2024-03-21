package org.jjche.sys.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.sys.modules.system.api.enums.DataScopeEnum;
import org.jjche.mybatis.base.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * 角色
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role", resultMap = "BaseResultMap")
public class RoleDO extends BaseEntity {

    @TableField(exist = false)
    @ApiModelProperty(value = "菜单", hidden = true)
    private Set<MenuDO> menus;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门", hidden = true)
    private Set<DeptDO> depts;

    @NotBlank
    @ApiModelProperty(value = "名称", hidden = true)
    private String name;

    @ApiModelProperty(value = "数据权限，全部 、 本级 、 自定义")
    private DataScopeEnum dataScope = DataScopeEnum.DATA_SCOPE_DEPT;

    @ApiModelProperty(value = "级别，数值越小，级别越大")
    private Integer level = 3;

    @ApiModelProperty(value = "描述")
    private String description;

    @NotBlank(message = "标识 不能为空")
    @ApiModelProperty(value = "标识")
    private String code;
}
