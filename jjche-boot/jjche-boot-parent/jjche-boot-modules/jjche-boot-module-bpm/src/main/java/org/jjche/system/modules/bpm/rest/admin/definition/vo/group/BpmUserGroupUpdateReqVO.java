package org.jjche.system.modules.bpm.rest.admin.definition.vo.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel(value = "管理后台 - 用户组更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmUserGroupUpdateReqVO extends BpmUserGroupBaseVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;

}
