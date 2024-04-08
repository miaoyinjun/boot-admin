package org.jjche.bpm.config.flowable.core.behavior;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.jjche.bpm.modules.definition.service.BpmTaskAssignRuleService;

/**
 * 自定义的 ActivityBehaviorFactory 实现类，目的如下：
 * 1. 自定义 {@link #createUserTaskActivityBehavior(UserTask)}：实现自定义的流程任务的 assignee 负责人的分配
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

    @Setter
    private BpmTaskAssignRuleService bpmTaskRuleService;

    @Override
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        BpmUserTaskActivityBehavior behavior = new BpmUserTaskActivityBehavior(userTask);
        behavior.setBpmTaskRuleService(bpmTaskRuleService);
        return behavior;
    }

    @Override
    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        BpmParallelMultiInstanceBehavior behavior = new BpmParallelMultiInstanceBehavior(activity, innerActivityBehavior);
        behavior.setBpmTaskRuleService(bpmTaskRuleService);
        return behavior;
    }
}
