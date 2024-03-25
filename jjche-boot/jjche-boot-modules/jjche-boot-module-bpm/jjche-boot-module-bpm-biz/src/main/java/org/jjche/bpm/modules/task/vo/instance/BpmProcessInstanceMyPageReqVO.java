package org.jjche.bpm.modules.task.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;

import java.sql.Timestamp;
import java.util.List;

@ApiModel(value = "管理后台 - 流程实例的分页 Item Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmProcessInstanceMyPageReqVO extends PageParam {

    @ApiModelProperty(value = "流程名称", example = "芋道")
    private String name;

    @ApiModelProperty(value = "流程定义的编号", example = "2048")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程实例的状态-参见 bpm_process_instance_status", example = "1")
    private Integer status;

    @ApiModelProperty(value = "流程实例的结果-参见 bpm_process_instance_result", example = "2")
    private Integer result;

    @ApiModelProperty(value = "流程分类-参见 bpm_model_category 数据字典", example = "1")
    private String category;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> gmtCreate;
}
