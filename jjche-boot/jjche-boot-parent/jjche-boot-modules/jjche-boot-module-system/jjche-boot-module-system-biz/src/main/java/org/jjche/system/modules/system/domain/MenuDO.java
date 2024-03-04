package org.jjche.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>MenuDO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-17
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_menu")
public class MenuDO extends BaseEntity {

    @ApiModelProperty(value = "菜单标题")
    private String title;

    @ApiModelProperty(value = "菜单组件名称")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer menuSort = 999;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "菜单类型，目录、菜单、按钮")
    private Integer type;

    @ApiModelProperty(value = "权限标识")
    private String permission;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @TableField("`cache`")
    @ApiModelProperty(value = "缓存")
    private Boolean cache;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    @ApiModelProperty(value = "上级菜单")
    private Long pid;

    @ApiModelProperty(value = "子节点数目", hidden = true)
    private Integer subCount = 0;

    @ApiModelProperty(value = "外链菜单")
    private Boolean iFrame;

    @TableField(exist = false)
    @ApiModelProperty(value = "数据权限")
    private Boolean isDataPermission;
}
