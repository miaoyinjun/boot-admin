package org.jjche.bpm.modules.task.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@ApiModel(value = "工作流 - 流程任务的 Running 进行中的分页项 Response VO")
@Data
public class BpmTaskTodoPageItemRespVO {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "任务名字", required = true, example = "芋道")
    private String name;

    @ApiModelProperty(value = "接收时间", required = true)
    private Timestamp claimTime;

    @ApiModelProperty(value = "创建时间", required = true)
    private Timestamp createTime;

    @ApiModelProperty(value = "激活状态-参见 SuspensionState 枚举", required = true, example = "1")
    private Integer suspensionState;

    /**
     * 所属流程实例
     */
    private ProcessInstance processInstance;

    @Data
    @ApiModel(value = "流程实例")
    public static class ProcessInstance {

        @ApiModelProperty(value = "流程实例编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "流程实例名称", required = true, example = "芋道")
        private String name;

        @ApiModelProperty(value = "发起人的用户编号", required = true, example = "1024")
        private Long startUserId;

        @ApiModelProperty(value = "发起人的用户昵称", required = true, example = "芋艿")
        private String startUserNickname;

        @ApiModelProperty(value = "流程定义的编号", required = true, example = "2048")
        private String processDefinitionId;

    }

}
