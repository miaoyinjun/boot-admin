package org.jjche.system.modules.bpm.rest.admin.task.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@ApiModel(value = "管理后台 - 流程活动的 Response VO")
@Data
public class BpmActivityRespVO {

    @ApiModelProperty(value = "流程活动的标识", required = true, example = "1024")
    private String key;
    @ApiModelProperty(value = "流程活动的类型", required = true, example = "StartEvent")
    private String type;

    @ApiModelProperty(value = "流程活动的开始时间", required = true)
    private Timestamp startTime;
    @ApiModelProperty(value = "流程活动的结束时间", required = true)
    private Timestamp endTime;

    @ApiModelProperty(value = "关联的流程任务的编号-关联的流程任务，只有 UserTask 等类型才有", example = "2048")
    private String taskId;

}
