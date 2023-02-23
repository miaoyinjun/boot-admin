package org.jjche.bpm.modules.bpm.controller.admin.definition.vo.group;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmUserGroupPageReqVO extends PageParam {

    @Schema(description = "组名", example = "芋道")
    private String name;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

}
