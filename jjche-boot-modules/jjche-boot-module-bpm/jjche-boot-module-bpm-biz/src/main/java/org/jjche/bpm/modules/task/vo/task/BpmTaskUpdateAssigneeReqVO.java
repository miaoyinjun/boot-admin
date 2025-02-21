package org.jjche.bpm.modules.task.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "工作流 - 流程任务的更新负责人的 Request VO")
@Data
public class BpmTaskUpdateAssigneeReqVO {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    @NotBlank(message = "任务编号不能为空")
    private String id;

    @ApiModelProperty(value = "新审批人的用户编号", required = true, example = "2048")
    @NotNull(message = "新审批人的用户编号不能为空")
    private Long assigneeUserId;

}
