package org.jjche.system.modules.bpm.rest.admin.oa.vo;

import cn.hutool.core.date.DatePattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(value = "管理后台 - 请假申请 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmOALeaveRespVO extends BpmOALeaveBaseVO {

    @ApiModelProperty(value = "请假表单主键", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "状态-参见 bpm_process_instance_result 枚举", required = true, example = "1")
    private Integer result;

    @ApiModelProperty(value = "申请时间", required = true)
    @NotNull(message = "申请时间不能为空")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "流程id")
    private String processInstanceId;

}
