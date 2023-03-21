package org.jjche.system.modules.bpm.rest.admin.definition.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;


@ApiModel(value = "管理后台 - 流程模型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmModelPageReqVO extends PageParam {

    @ApiModelProperty(value = "标识-精准匹配", example = "process1641042089407")
    private String key;

    @ApiModelProperty(value = "名字-模糊匹配", example = "芋道")
    private String name;

    @ApiModelProperty(value = "流程分类-参见 bpm_model_category 数据字典", example = "1")
    private String category;

}
