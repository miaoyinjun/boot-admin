package org.jjche.system.modules.bpm.rest.admin.task.vo.task;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;

import java.sql.Timestamp;

@ApiModel(value = "管理后台 - 流程任务的 TODO 待办的分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskTodoPageReqVO extends PageParam {

    @ApiModelProperty(value = "流程任务名", example = "芋道")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Timestamp[] createTime;

}
