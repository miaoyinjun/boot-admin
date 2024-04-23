package org.jjche.bpm.modules.task.mapstruct;

import org.flowable.engine.history.HistoricActivityInstance;
import org.jjche.bpm.modules.task.vo.activity.BpmActivityRespVO;
import org.mapstruct.*;

import java.util.List;

/**
 * BPM 活动 Convert
 *
 * @author 芋道源码
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmActivityConvert {

    List<BpmActivityRespVO> convertList(List<HistoricActivityInstance> list);

    @Mappings({
            @Mapping(source = "activityId", target = "key"),
            @Mapping(source = "activityType", target = "type")
    })
    BpmActivityRespVO convert(HistoricActivityInstance bean);
}
