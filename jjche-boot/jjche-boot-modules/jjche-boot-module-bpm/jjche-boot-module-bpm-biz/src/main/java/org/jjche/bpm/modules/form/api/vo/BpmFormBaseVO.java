package org.jjche.bpm.modules.form.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
* 动态表单 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BpmFormBaseVO {

    @ApiModelProperty(value = "表单名称", required = true)
    @NotBlank(message = "表单名称不能为空")
    private String name;

    @ApiModelProperty(value = "备注", example = "我是备注")
    private String remark;

}
