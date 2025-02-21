package org.jjche.bpm.modules.task.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@ApiModel(value = "工作流 - 流程实例的创建 Request VO")
@Data
public class BpmProcessInstanceCreateReqVO {

    @ApiModelProperty(value = "流程定义的编号", required = true, example = "1024")
    @NotEmpty(message = "流程定义编号不能为空")
    private String processDefinitionId;

    @ApiModelProperty(value = "变量实例")
    private Map<String, Object> variables;

}
