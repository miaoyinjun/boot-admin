package org.jjche.sys.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "管理后台 - 岗位精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSimpleVO {

    @ApiModelProperty(value = "岗位编号", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "岗位名称", required = true)
    private String name;

}
