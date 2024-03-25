package org.jjche.bpm.modules.definition.api;

import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.definition.api.dto.BpmProcessInstanceCreateReqDTO;
import org.jjche.bpm.modules.task.service.BpmProcessInstanceService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Flowable 流程实例 Api 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Service
@Validated
@RequiredArgsConstructor
public class BpmProcessInstanceApiImpl implements IBpmProcessInstanceApi {

    private final BpmProcessInstanceService processInstanceService;

    @Override
    public String createProcessInstance(Long userId, BpmProcessInstanceCreateReqDTO reqDTO) {
        return processInstanceService.createProcessInstance(userId, reqDTO);
    }
}
