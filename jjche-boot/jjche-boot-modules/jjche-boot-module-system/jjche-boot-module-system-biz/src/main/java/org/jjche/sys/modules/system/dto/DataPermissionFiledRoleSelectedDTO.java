package org.jjche.sys.modules.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.base.BaseDTO;

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
public class DataPermissionFiledRoleSelectedDTO implements BaseDTO {
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "可见")
    private Boolean isAccessible;
    @ApiModelProperty(value = "编辑")
    private Boolean isEditable;
}
