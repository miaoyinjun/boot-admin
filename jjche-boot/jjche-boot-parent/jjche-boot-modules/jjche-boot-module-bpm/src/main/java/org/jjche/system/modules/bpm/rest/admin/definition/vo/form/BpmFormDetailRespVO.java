package org.jjche.system.modules.bpm.rest.admin.definition.vo.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BpmFormDetailRespVO extends BpmFormRespVO {
    @ApiModelProperty(value = "表单的配置-JSON 字符串")
    private String content;
}
