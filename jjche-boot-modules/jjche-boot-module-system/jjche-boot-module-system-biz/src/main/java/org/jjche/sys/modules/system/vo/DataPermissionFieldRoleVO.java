package org.jjche.sys.modules.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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
public class DataPermissionFieldRoleVO implements Serializable {
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "数据字段权限ID")
    private Long dataPermissionFieldId;
    @ApiModelProperty(value = "字段名称")
    private String name;
    @ApiModelProperty(value = "选中")
    private Boolean isSelected;
    @ApiModelProperty(value = "可见")
    private Boolean isAccessible;
    @ApiModelProperty(value = "编辑")
    private Boolean isEditable;
    @ApiModelProperty(value = "是否有效")
    private Boolean isActivated;
}
