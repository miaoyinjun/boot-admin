package org.jjche.bpm.modules.task.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.jjche.bpm.modules.task.vo.activity.BpmActivityRespVO;
import org.jjche.bpm.modules.task.mapstruct.BpmActivityConvert;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;


/**
 * BPM 活动实例 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@RequiredArgsConstructor
public class BpmActivityService {

    private final HistoryService historyService;
    private final BpmActivityConvert bpmActivityConvert;

    /**
     * 获得指定流程实例的活动实例列表
     *
     * @param processInstanceId 流程实例的编号
     * @return 活动实例列表
     */
    public List<BpmActivityRespVO> getActivityListByProcessInstanceId(String processInstanceId) {
        List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();
        return bpmActivityConvert.toVO(activityList);
    }

    /**
     * 获得执行编号对应的活动实例
     *
     * @param executionId 执行编号
     * @return 活动实例
     */
    public List<HistoricActivityInstance> getHistoricActivityListByExecutionId(String executionId) {
        return historyService.createHistoricActivityInstanceQuery().executionId(executionId).list();
    }

}
