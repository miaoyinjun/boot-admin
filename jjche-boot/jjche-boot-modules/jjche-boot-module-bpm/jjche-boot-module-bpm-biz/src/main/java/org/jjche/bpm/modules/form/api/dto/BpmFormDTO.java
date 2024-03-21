package org.jjche.bpm.modules.form.api.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.jjche.bpm.modules.form.api.vo.BpmFormBaseVO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true)
public class BpmFormDTO extends BpmFormBaseVO {
    private Long id;
    @ApiModelProperty(value = "表单的配置-JSON 字符串", required = true)
    @NotBlank(message = "表单的配置不能为空")
    private String content;
}
