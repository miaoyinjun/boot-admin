package org.jjche.bpm.modules.task.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "工作流 - 流程实例的取消 Request VO")
@Data
public class BpmProcessInstanceCancelReqVO {

    @ApiModelProperty(value = "流程实例的编号", required = true, example = "1024")
    @NotBlank(message = "流程实例的编号不能为空")
    private String id;

    @ApiModelProperty(value = "取消原因", required = true, example = "不请假了！")
    @NotBlank(message = "取消原因不能为空")
    private String reason;

}
