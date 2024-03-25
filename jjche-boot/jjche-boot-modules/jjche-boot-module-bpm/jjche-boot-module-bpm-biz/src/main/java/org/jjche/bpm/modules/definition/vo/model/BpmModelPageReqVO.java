package org.jjche.bpm.modules.definition.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "管理后台 - 流程模型分页 Request VO")
@Data
public class BpmModelPageReqVO {

    @ApiModelProperty(value = "标识-精准匹配")
    private String key;

    @ApiModelProperty(value = "名字-模糊匹配")
    private String name;

    @ApiModelProperty(value = "流程分类-参见")
    private String category;
}
