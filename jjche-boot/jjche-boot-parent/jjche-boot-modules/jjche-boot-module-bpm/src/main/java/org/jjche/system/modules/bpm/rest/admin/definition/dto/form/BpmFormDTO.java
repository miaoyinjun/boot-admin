package org.jjche.system.modules.bpm.rest.admin.definition.dto.form;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.form.BpmFormBaseVO;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString(callSuper = true)
public class BpmFormDTO extends BpmFormBaseVO {
    private Long id;
    @ApiModelProperty(value = "表单的配置-JSON 字符串", required = true)
    @NotNull(message = "表单的配置不能为空")
    private String content;
}
