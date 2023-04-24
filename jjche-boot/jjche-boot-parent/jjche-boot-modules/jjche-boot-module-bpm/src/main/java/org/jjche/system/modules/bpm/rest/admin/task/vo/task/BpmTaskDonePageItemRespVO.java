package org.jjche.system.modules.bpm.rest.admin.task.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;

@ApiModel(value = "管理后台 - 流程任务的 Done 已完成的分页项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskDonePageItemRespVO extends BpmTaskTodoPageItemRespVO {

    @ApiModelProperty(value = "结束时间", required = true)
    private Timestamp endTime;
    @ApiModelProperty(value = "持续时间", required = true, example = "1000")
    private Long durationInMillis;

    @ApiModelProperty(value = "任务结果-参见 bpm_process_instance_result", required = true, example = "2")
    private Integer result;
    @ApiModelProperty(value = "审批建议", required = true, example = "不请假了！")
    private String reason;

}
