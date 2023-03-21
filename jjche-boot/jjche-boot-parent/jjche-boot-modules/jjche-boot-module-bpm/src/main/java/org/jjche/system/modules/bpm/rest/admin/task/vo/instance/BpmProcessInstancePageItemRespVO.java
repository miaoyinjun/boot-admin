package org.jjche.system.modules.bpm.rest.admin.task.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "管理后台 - 流程实例的分页 Item Response VO")
@Data
public class BpmProcessInstancePageItemRespVO {

    @ApiModelProperty(value = "流程实例的编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "流程名称", required = true, example = "芋道")
    private String name;

    @ApiModelProperty(value = "流程定义的编号", required = true, example = "2048")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程分类-参见 bpm_model_category 数据字典", required = true, example = "1")
    private String category;

    @ApiModelProperty(value = "流程实例的状态-参见 bpm_process_instance_status", required = true, example = "1")
    private Integer status;

    @ApiModelProperty(value = "流程实例的结果-参见 bpm_process_instance_result", required = true, example = "2")
    private Integer result;

    @ApiModelProperty(value = "提交时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "结束时间", required = true)
    private LocalDateTime endTime;

    /**
     * 当前任务
     */
    private List<Task> tasks;

    @ApiModel(value = "流程任务")
    @Data
    public static class Task {

        @ApiModelProperty(value = "流程任务的编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "任务名称", required = true, example = "芋道")
        private String name;

    }

}
