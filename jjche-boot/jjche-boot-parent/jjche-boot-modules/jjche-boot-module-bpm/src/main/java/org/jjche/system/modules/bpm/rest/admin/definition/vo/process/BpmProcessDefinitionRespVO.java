package org.jjche.system.modules.bpm.rest.admin.definition.vo.process;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel(value = "管理后台 - 流程定义 Response VO")
@Data
public class BpmProcessDefinitionRespVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "版本", required = true, example = "1")
    private Integer version;

    @ApiModelProperty(value = "流程名称", required = true, example = "芋道")
    @NotEmpty(message = "流程名称不能为空")
    private String name;

    @ApiModelProperty(value = "流程描述", example = "我是描述")
    private String description;

    @ApiModelProperty(value = "流程分类-参见 bpm_model_category 数据字典", example = "1")
    @NotEmpty(message = "流程分类不能为空")
    private String category;

    @ApiModelProperty(value = "表单类型-参见 bpm_model_form_type 数据字典", example = "1")
    private Integer formType;
    @ApiModelProperty(value = "表单编号-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", example = "1024")
    private Long formId;
    @ApiModelProperty(value = "表单的配置-JSON 字符串。在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", required = true)
    private String formConf;
    @ApiModelProperty(value = "表单项的数组-JSON 字符串的数组。在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", required = true)
    private List<String> formFields;
    @ApiModelProperty(value = "自定义表单的提交路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
            example = "/bpm/oa/leave/create")
    private String formCustomCreatePath;
    @ApiModelProperty(value = "自定义表单的查看路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
            example = "/bpm/oa/leave/view")
    private String formCustomViewPath;

    @ApiModelProperty(value = "中断状态-参见 SuspensionState 枚举", required = true, example = "1")
    private Integer suspensionState;

}
