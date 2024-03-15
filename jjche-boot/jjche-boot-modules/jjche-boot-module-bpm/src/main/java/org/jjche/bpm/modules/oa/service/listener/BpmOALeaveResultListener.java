package org.jjche.bpm.modules.oa.service.listener;
import org.jjche.bpm.config.bpm.core.event.BpmProcessInstanceResultEvent;
import org.jjche.bpm.config.bpm.core.event.BpmProcessInstanceResultEventListener;
import org.jjche.bpm.modules.oa.service.BpmOALeaveService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmOALeaveResultListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private BpmOALeaveService leaveService;

    @Override
    protected String getProcessDefinitionKey() {
        return BpmOALeaveService.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        leaveService.updateLeaveResult(Long.parseLong(event.getBusinessKey()), event.getResult());
    }

}
