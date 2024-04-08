package org.jjche.bpm.modules.task.mapstruct;

import org.flowable.engine.history.HistoricActivityInstance;
import org.jjche.bpm.modules.task.vo.activity.BpmActivityRespVO;
import org.jjche.core.base.BaseVoMapStruct;
import org.mapstruct.*;

/**
 * BPM 活动 Convert
 *
 * @author 芋道源码
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmActivityConvert extends BaseVoMapStruct<HistoricActivityInstance, BpmActivityRespVO> {
     @Mappings({
            @Mapping(source = "activityId", target = "key"),
            @Mapping(source = "activityType", target = "type")
    })
     @Named("convert")
    BpmActivityRespVO convert(HistoricActivityInstance bean);
}
