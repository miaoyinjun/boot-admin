package org.jjche.system.modules.bpm.rest.admin.oa.vo;

import cn.hutool.core.date.DatePattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel(value = "管理后台 - 请假申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmOALeavePageReqVO extends PageParam {

    @ApiModelProperty(value = "状态-参见 bpm_process_instance_result 枚举", example = "1")
    private Integer result;

    @ApiModelProperty(value = "请假类型-参见 bpm_oa_type", example = "1")
    private Integer type;

    @ApiModelProperty(value = "原因-模糊匹配", example = "阅读芋道源码")
    private String reason;

    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime[] createTime;

}
