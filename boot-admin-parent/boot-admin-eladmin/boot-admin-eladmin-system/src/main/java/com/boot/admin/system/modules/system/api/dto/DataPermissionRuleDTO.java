package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
* <p>
* 数据规则
* </p>
*
* @author miaoyj
* @since 2021-10-27
*/
@Data
public class DataPermissionRuleDTO implements BaseDTO {
   @NotNull(message = "id不能为空", groups = Update.class)
   @ApiModelProperty(value = "ID")
   private Long id;
   @NotNull(message = "菜单ID不能为空")
   @ApiModelProperty(value = "菜单ID")
   private Long menuId;
   @NotBlank(message = "名称不能为空")
   @ApiModelProperty(value = "名称")
   private String name;
   @NotNull(message = "条件不能为空")
   @ApiModelProperty(value = "条件")
   private QueryCriteria.Type condition;
   @ApiModelProperty(value = "列名")
   private String column;
   @ApiModelProperty(value = "规则值")
   private String value;
   @NotNull(message = "是否有效不能为空")
   @ApiModelProperty(value = "是否有效")
   private Boolean isActivated;
}