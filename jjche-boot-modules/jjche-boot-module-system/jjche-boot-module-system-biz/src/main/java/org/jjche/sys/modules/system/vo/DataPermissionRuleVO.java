package org.jjche.sys.modules.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;

import java.io.Serializable;

/**
 * <p>
 * 数据规则
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-10-27
 */
@Data
public class DataPermissionRuleVO implements Serializable {
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "条件")
    private QueryCriteria.Type condition;
    @ApiModelProperty(value = "列名")
    private String column;
    @ApiModelProperty(value = "规则值")
    private String value;
    @ApiModelProperty(value = "是否有效")
    private Boolean isActivated;
    @ApiModelProperty(value = "选中")
    private Boolean isSelected;
}
