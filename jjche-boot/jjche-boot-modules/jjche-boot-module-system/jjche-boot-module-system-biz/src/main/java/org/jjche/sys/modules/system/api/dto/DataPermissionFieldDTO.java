package org.jjche.sys.modules.system.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class DataPermissionFieldDTO implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "菜单id")
    @NotNull(message = "菜单ID不能为空")
    private Long menuId;
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;
    @ApiModelProperty(value = "字段")
    @NotBlank(message = "字段不能为空")
    private String code;
    @ApiModelProperty(value = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;
    @NotNull(message = "是否有效不能为空")
    @ApiModelProperty(value = "是否有效")
    private Boolean isActivated;

    /* 分组校验 */
    public @interface Create {
    }

    /* 分组校验 */
    public @interface Update {
    }
}
