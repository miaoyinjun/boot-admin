package org.jjche.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>DataPermissionFieldResultVO class.</p>
 *
 * @author nikohuang
 * @version 1.0.10-SNAPSHOT
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DataPermissionFieldResultVO {
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 标志符号
     */
    @ApiModelProperty("标志符号")
    private String code;

    /**
     * 是否允许访问
     */
    @ApiModelProperty("可访问")
    private Boolean isAccessible;

    @ApiModelProperty(value = "可编辑")
    private Boolean isEditable;
}
