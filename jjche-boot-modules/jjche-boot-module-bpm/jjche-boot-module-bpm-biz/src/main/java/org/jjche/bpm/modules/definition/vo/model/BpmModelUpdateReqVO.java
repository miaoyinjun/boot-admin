package org.jjche.bpm.modules.definition.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "工作流 - 流程模型的更新 Request VO")
@Data
public class BpmModelUpdateReqVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    @NotBlank(message = "编号不能为空")
    private String id;

    @ApiModelProperty(value = "流程名称", example = "芋道")
    private String name;

    @ApiModelProperty(value = "流程描述", example = "我是描述")
    private String description;

    @ApiModelProperty(value = "流程分类-参见 bpm_model_category 数据字典", example = "1")
    private String category;

    @ApiModelProperty(value = "BPMN XML", required = true)
    private String bpmnXml;

    @ApiModelProperty(value = "表单类型-参见 bpm_model_form_type 数据字典", example = "1")
    private Integer formType;
    @ApiModelProperty(value = "表单编号-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", example = "1024")
    private Long formId;
    @ApiModelProperty(value = "自定义表单的提交路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
            example = "/oa/leave/create")
    private String formCustomCreatePath;
    @ApiModelProperty(value = "自定义表单的查看路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
            example = "/oa/leave/view")
    private String formCustomViewPath;

}
