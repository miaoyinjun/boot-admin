package org.jjche.system.modules.bpm.rest.admin.oa.vo;

import cn.hutool.core.date.DatePattern;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
* 请假申请 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BpmOALeaveBaseVO {

    @ApiModelProperty(value = "请假的开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startTime;
    @ApiModelProperty(value = "请假的结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "请假类型-参见 bpm_oa_type 枚举", required = true, example = "1")
    private Integer type;

    @ApiModelProperty(value = "原因", required = true, example = "阅读芋道源码")
    private String reason;

}
