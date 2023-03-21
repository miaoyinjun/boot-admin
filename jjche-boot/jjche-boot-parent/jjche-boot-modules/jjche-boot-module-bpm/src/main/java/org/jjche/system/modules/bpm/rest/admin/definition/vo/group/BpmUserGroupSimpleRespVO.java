package org.jjche.system.modules.bpm.rest.admin.definition.vo.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "管理后台 - 用户组精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BpmUserGroupSimpleRespVO {

    @ApiModelProperty(value = "用户组编号", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "用户组名字", required = true, example = "芋道")
    private String name;

}
