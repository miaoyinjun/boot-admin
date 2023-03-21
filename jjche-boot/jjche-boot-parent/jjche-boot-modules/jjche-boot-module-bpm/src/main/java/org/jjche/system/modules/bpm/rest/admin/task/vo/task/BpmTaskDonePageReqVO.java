package org.jjche.system.modules.bpm.rest.admin.task.vo.task;

import cn.hutool.core.date.DatePattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel(value = "管理后台 - 流程任务的 Done 已办的分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskDonePageReqVO extends PageParam {

    @ApiModelProperty(value = "流程任务名", example = "芋道")
    private String name;

    @ApiModelProperty(value = "开始的创建收间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime beginCreateTime;

    @ApiModelProperty(value = "结束的创建时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endCreateTime;

}
