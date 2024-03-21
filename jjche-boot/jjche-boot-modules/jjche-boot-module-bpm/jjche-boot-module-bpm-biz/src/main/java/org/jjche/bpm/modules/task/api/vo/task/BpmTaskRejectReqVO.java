package org.jjche.bpm.modules.task.api.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "管理后台 - 不通过流程任务的 Request VO")
@Data
public class BpmTaskRejectReqVO {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    @NotBlank(message = "任务编号不能为空")
    private String id;

    @ApiModelProperty(value = "审批意见", required = true, example = "不错不错！")
    @NotBlank(message = "审批意见不能为空")
    private String reason;

}
