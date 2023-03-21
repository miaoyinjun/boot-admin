package org.jjche.system.modules.bpm.rest.admin.definition.vo.form;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmFormRespVO extends BpmFormBaseVO {

    @ApiModelProperty(value = "表单编号")
    private Long id;

    @ApiModelProperty(value = "表单的配置-JSON 字符串")
    @NotNull(message = "表单的配置不能为空")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private Timestamp gmtCreate;
}
