package org.jjche.bpm.modules.definition.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "工作流 - 流程模型更新状态 Request VO")
@Data
public class BpmModelUpdateStateReqVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    @NotBlank(message = "编号不能为空")
    private String id;

    @ApiModelProperty(value = "状态-见 SuspensionState 枚举", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer state;

}
