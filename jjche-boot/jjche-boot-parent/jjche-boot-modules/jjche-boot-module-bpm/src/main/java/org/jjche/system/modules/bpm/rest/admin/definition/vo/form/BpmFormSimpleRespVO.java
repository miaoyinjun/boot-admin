package org.jjche.system.modules.bpm.rest.admin.definition.vo.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BpmFormSimpleRespVO {

    @ApiModelProperty(value = "表单编号", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "表单名称", required = true, example = "芋道")
    private String name;

}
