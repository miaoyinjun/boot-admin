package org.jjche.bpm.modules.definition.api.vo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.bpm.onstants.DictTypeConstants;
import org.jjche.common.annotation.Dict;
import org.jjche.common.annotation.JacksonAllowNull;

import javax.validation.constraints.NotEmpty;

/**
* 流程模型 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BpmModelBaseVO {

    @ApiModelProperty(value = "流程标识", required = true, example = "process_yudao")
    @NotEmpty(message = "流程标识不能为空")
    private String key;

    @ApiModelProperty(value = "流程名称", required = true, example = "芋道")
    @NotEmpty(message = "流程名称不能为空")
    private String name;

    @ApiModelProperty(value = "流程描述", example = "我是描述")
    private String description;

    @ApiModelProperty(value = "流程分类-参见 bpm_model_category 数据字典", example = "1")
    @NotEmpty(message = "流程分类不能为空")
    @Dict(DictTypeConstants.BPM_MODEL_CATEGORY)
    private String category;

    @ApiModelProperty(value = "表单类型-参见 bpm_model_form_type 数据字典", example = "1")
    @JacksonAllowNull
    @Dict(DictTypeConstants.BPM_MODEL_FORM_TYPE)
    private Integer formType;
    @ApiModelProperty(value = "表单编号-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", example = "1024")
    @JacksonAllowNull
    private Long formId;
    @ApiModelProperty(value = "自定义表单的提交路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
            example = "/bpm/oa/leave/create")
    private String formCustomCreatePath;
    @ApiModelProperty(value = "自定义表单的查看路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
            example = "/bpm/oa/leave/view")
    private String formCustomViewPath;

}
