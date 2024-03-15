package org.jjche.system.modules.system.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 数据字段输出
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-19
 */
@Data
public class DataPermissionFieldVO implements Serializable {
    @ApiModelProperty(value = "")
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "标识")
    private String code;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "菜单id")
    private String menuId;
    @ApiModelProperty(value = "是否有效")
    private Boolean isActivated;

    /**
     * 权限用
     */
    @ApiModelProperty(value = "可见")
    private Boolean isAccessible;
    @ApiModelProperty(value = "可编辑")
    private Boolean isEditable;
    @ApiModelProperty(value = "菜单标识")
    private String menuPermission;
}
