package org.jjche.bpm.config.flowable.core.behavior.script.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.jjche.bpm.config.flowable.core.behavior.script.BpmTaskAssignScript;
import org.jjche.bpm.enums.BpmErrorCodeEnum;
import org.jjche.bpm.modules.task.service.BpmProcessInstanceService;
import org.jjche.common.dto.DeptSmallDTO;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.vo.UserVO;
import org.jjche.sys.api.SysBaseApi;
import org.springframework.context.annotation.Lazy;

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
    private SysBaseApi sysBaseApi;
    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService bpmProcessInstanceService;

    protected Set<Long> calculateTaskCandidateUsers(DelegateExecution execution, int level) {
        AssertUtil.isTrue(level > 0, BpmErrorCodeEnum.TASK_ASSIGNEE_LEVEL_ZERO);
        // 获得发起人
        ProcessInstance processInstance = bpmProcessInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtil.parseLong(processInstance.getStartUserId());
        // 获得对应 leve 的部门
        DeptSmallDTO dept = null;
        for (int i = 0; i < level; i++) {
            // 获得 level 对应的部门
            if (dept == null) {
                dept = getStartUserDept(startUserId);
                if (dept == null) { // 找不到发起人的部门，所以无法使用该规则
                    return emptySet();
                }
            } else {
                DeptSmallDTO parentDept = sysBaseApi.getDeptSmallById(dept.getPid());
                if (parentDept == null) { // 找不到父级部门，所以只好结束寻找。原因是：例如说，级别比较高的人，所在部门层级比较少
                    break;
                }
                dept = parentDept;
            }
        }
        return dept.getLeaderUserId() != null ? CollUtil.newHashSet(dept.getLeaderUserId()) : emptySet();
    }

    private DeptSmallDTO getStartUserDept(Long startUserId) {
        UserVO startUser = sysBaseApi.getUserById(startUserId);
        if (startUser.getDeptId() == null) { // 找不到部门，所以无法使用该规则
            return null;
        }
        return sysBaseApi.getDeptSmallById(startUser.getDeptId());
    }

}
