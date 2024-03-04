package org.jjche.bpm.config.flowable.core.behavior.script.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.jjche.bpm.config.flowable.core.behavior.script.BpmTaskAssignScript;
import org.jjche.bpm.modules.task.service.BpmProcessInstanceService;
import org.jjche.common.dto.DeptSmallDto;
import org.jjche.common.dto.UserVO;
import org.jjche.system.modules.dept.api.DeptApi;
import org.jjche.system.modules.user.api.UserApi;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * 分配给发起人的 Leader 审批的 Script 实现类
 * 目前 Leader 的定义是，
 *
 * @author 芋道源码
 */
public abstract class BpmTaskAssignLeaderAbstractScript implements BpmTaskAssignScript {

    @Resource
    private UserApi userApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService bpmProcessInstanceService;

    protected Set<Long> calculateTaskCandidateUsers(DelegateExecution execution, int level) {
        Assert.isTrue(level > 0, "level 必须大于 0");
        // 获得发起人
        ProcessInstance processInstance = bpmProcessInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtil.parseLong(processInstance.getStartUserId());
        // 获得对应 leve 的部门
        DeptSmallDto dept = null;
        for (int i = 0; i < level; i++) {
            // 获得 level 对应的部门
            if (dept == null) {
                dept = getStartUserDept(startUserId);
                if (dept == null) { // 找不到发起人的部门，所以无法使用该规则
                    return emptySet();
                }
            } else {
                DeptSmallDto parentDept = deptApi.getSmallById(dept.getPid());
                if (parentDept == null) { // 找不到父级部门，所以只好结束寻找。原因是：例如说，级别比较高的人，所在部门层级比较少
                    break;
                }
                dept = parentDept;
            }
        }
        return dept.getLeaderUserId() != null ? CollUtil.newHashSet(dept.getLeaderUserId()) : emptySet();
    }

    private DeptSmallDto getStartUserDept(Long startUserId) {
        UserVO startUser = userApi.findById(startUserId);
        if (startUser.getDeptId() == null) { // 找不到部门，所以无法使用该规则
            return null;
        }
        return deptApi.getSmallById(startUser.getDeptId());
    }

}
