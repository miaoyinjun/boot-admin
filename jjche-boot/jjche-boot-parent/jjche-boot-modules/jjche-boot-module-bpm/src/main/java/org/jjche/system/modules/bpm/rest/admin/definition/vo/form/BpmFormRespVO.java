package org.jjche.system.modules.bpm.rest.admin.definition.vo.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmFormRespVO extends BpmFormBaseVO {

    @ApiModelProperty(value = "表单编号")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Timestamp gmtCreate;
}
