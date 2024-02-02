package org.jjche.bpm.modules.task.api.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.common.param.PageParam;

import java.sql.Timestamp;
import java.util.List;

@ApiModel(value = "管理后台 - 流程任务的 Done 已办的分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskDonePageReqVO extends PageParam {

    @ApiModelProperty(value = "流程任务名", example = "芋道")
    private String name;

    private List<Timestamp> createTime;
}
