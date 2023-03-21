package org.jjche.system.modules.bpm.rest.admin.definition.vo.process;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;

@ApiModel(value = "管理后台 - 流程定义分页 Request VO")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BpmProcessDefinitionPageReqVO extends PageParam {

    @ApiModelProperty(value = "标识-精准匹配", example = "process1641042089407")
    private String key;

}
