package org.jjche.system.modules.bpm.rest.admin.definition.vo.group;

import cn.hutool.core.date.DatePattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel(value = "管理后台 - 用户组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmUserGroupPageReqVO extends PageParam {

    @ApiModelProperty(value = "组名", example = "芋道")
    private String name;

    @ApiModelProperty(value = "状态", example = "1")
    private Integer status;

    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime[] createTime;

}
