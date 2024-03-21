package org.jjche.bpm.modules.oa.api.vo;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.AssertTrue;

@ApiModel(value = "管理后台 - 请假申请创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmOALeaveCreateReqDTO extends BpmOALeaveBaseVO {

    @AssertTrue(message = "结束时间，需要在开始时间之后")
    public boolean isEndTimeValid() {
        return DateUtil.compare(getEndTime(), getStartTime()) > 0;
    }

}
