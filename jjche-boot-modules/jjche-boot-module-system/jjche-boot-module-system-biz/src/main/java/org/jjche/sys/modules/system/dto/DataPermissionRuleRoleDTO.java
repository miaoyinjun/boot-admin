package org.jjche.sys.modules.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.base.BaseDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 数据规则权限
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-01
 */
@Data
public class DataPermissionRuleRoleDTO implements BaseDTO {
    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;
    @ApiModelProperty(value = "数据规则权限ID")
    private List<Long> dataPermissionRuleIdList;

}
