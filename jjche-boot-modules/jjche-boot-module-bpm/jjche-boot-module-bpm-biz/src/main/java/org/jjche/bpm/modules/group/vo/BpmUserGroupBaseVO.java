package org.jjche.bpm.modules.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.enums.CommonStatusEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
* 用户组 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BpmUserGroupBaseVO {

    @ApiModelProperty(value = "组名", required = true, example = "芋道")
    @NotBlank(message = "组名不能为空")
    private String name;

    @ApiModelProperty(value = "描述", required = true, example = "芋道源码")
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "成员编号数组", required = true, example = "1,2,3")
    @NotNull(message = "成员编号数组不能为空")
    private Set<Long> memberUserIds;

    @ApiModelProperty(value = "状态", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private CommonStatusEnum status;

}
