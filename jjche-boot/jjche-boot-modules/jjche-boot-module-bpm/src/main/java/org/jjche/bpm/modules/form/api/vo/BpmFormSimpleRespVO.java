package org.jjche.bpm.modules.form.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BpmFormSimpleRespVO {

    @ApiModelProperty(value = "表单编号", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "表单名称", required = true, example = "芋道")
    private String name;

}
