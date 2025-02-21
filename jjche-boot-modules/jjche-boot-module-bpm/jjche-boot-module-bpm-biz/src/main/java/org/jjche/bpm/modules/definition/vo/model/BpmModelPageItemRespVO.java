package org.jjche.bpm.modules.definition.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;

@ApiModel(value = "工作流 - 流程模型的分页的每一项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmModelPageItemRespVO extends BpmModelBaseVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "表单名字", example = "请假表单")
    private String formName;

    @ApiModelProperty(value = "创建时间", required = true)
    private Timestamp createTime;

    /**
     * 最新部署的流程定义
     */
    private ProcessDefinition processDefinition;

    @ApiModel(value = "流程定义")
    @Data
    public static class ProcessDefinition {

        @ApiModelProperty(value = "编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "版本", required = true, example = "1")
        private Integer version;

        @ApiModelProperty(value = "部署时间", required = true)
        private Timestamp deploymentTime;

        @ApiModelProperty(value = "中断状态-参见 SuspensionState 枚举", required = true, example = "1")
        private Integer suspensionState;

    }

}
