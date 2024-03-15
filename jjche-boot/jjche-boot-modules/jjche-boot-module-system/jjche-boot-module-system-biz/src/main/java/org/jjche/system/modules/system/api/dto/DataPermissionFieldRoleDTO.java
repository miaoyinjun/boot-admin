package org.jjche.system.modules.system.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.base.BaseDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

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
public class DataPermissionFieldRoleDTO implements BaseDTO {
    @ApiModelProperty(value = "数据字段权限")
    List<DataPermissionFiledRoleSelectedDTO> dataPermissionFieldSelectedList;
    @NotNull(message = "id不能为空", groups = Update.class)
    @ApiModelProperty(value = "ID")
    private Long id;
    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;
}
