package org.jjche.sys.modules.version.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 版本
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
@Data
public class VersionDTO implements Serializable {
    @NotNull(message = "id不能为空", groups = VersionDtoUpdateValid.class)
    private Long id;

    @NotBlank(message = "版本号名称不能为空")
    @ApiModelProperty(value = "版本号名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * <p>
     * 版本修改分组验证
     * </p>
     *
     * @author miaoyj
     * @since 2021-04-23
     */
    public interface VersionDtoUpdateValid {
    }
}
