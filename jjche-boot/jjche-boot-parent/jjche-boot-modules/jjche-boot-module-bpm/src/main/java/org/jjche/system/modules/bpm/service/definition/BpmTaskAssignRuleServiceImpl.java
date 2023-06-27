package org.jjche.system.modules.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.delegate.DelegateExecution;
import org.jjche.common.dto.DeptSmallDto;
import org.jjche.common.dto.UserVO;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.service.IDeptService;
import org.jjche.common.service.IUserService;
import org.jjche.flowable.util.FlowableUtils;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.system.modules.bpm.convert.definition.BpmTaskAssignRuleConvert;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmTaskAssignRuleDO;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmUserGroupDO;
import org.jjche.system.modules.bpm.enums.definition.BpmTaskAssignRuleTypeEnum;
import org.jjche.system.modules.bpm.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import org.jjche.system.modules.bpm.mapper.BpmTaskAssignRuleMapper;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.rule.BpmTaskAssignRuleRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * BPM 任务分配规则 Service 实现类
 */
@Service
@Validated
@Slf4j
public class BpmTaskAssignRuleServiceImpl extends MyServiceImpl<BpmTaskAssignRuleMapper, BpmTaskAssignRuleDO> implements BpmTaskAssignRuleService {

    @Resource
    private BpmTaskAssignRuleMapper taskRuleMapper;
    @Resource
    @Lazy // 解决循环依赖
    private BpmModelService modelService;
    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmUserGroupService userGroupService;
    @Resource
    private IDeptService deptService;
    @Resource
    private IUserService userService;
    /**
     * 任务分配脚本
     */
    private Map<Long, BpmTaskAssignScript> scriptMap = Collections.emptyMap();

    @Resource
    public void setScripts(List<BpmTaskAssignScript> scripts) {
        Map<Long, BpmTaskAssignScript> map = MapUtil.newHashMap();
        this.scriptMap = CollUtil.toMap(scripts, map, script -> script.getEnum().getId());
    }

    @Override
    public List<BpmTaskAssignRuleDO> getTaskAssignRuleListByProcessDefinitionId(String processDefinitionId, String taskDefinitionKey) {
        return taskRuleMapper.selectListByProcessDefinitionId(processDefinitionId, taskDefinitionKey);
    }

    @Override
    public List<BpmTaskAssignRuleDO> getTaskAssignRuleListByModelId(String modelId) {
        return taskRuleMapper.selectListByModelId(modelId);
    }

    @Override
    public List<BpmTaskAssignRuleRespVO> getTaskAssignRuleList(String modelId, String processDefinitionId) {
        // 获得规则
        List<BpmTaskAssignRuleDO> rules = Collections.emptyList();
        BpmnModel model = null;
        if (StrUtil.isNotEmpty(modelId)) {
            rules = getTaskAssignRuleListByModelId(modelId);
            model = modelService.getBpmnModel(modelId);
        } else if (StrUtil.isNotEmpty(processDefinitionId)) {
            rules = getTaskAssignRuleListByProcessDefinitionId(processDefinitionId, null);
            model = processDefinitionService.getBpmnModel(processDefinitionId);
        }
        if (model == null) {
            return Collections.emptyList();
        }
        // 获得用户任务，只有用户任务才可以设置分配规则
        List<UserTask> userTasks = FlowableUtils.getBpmnModelElements(model, UserTask.class);
        if (CollUtil.isEmpty(userTasks)) {
            return Collections.emptyList();
        }
        // 转换数据
        return BpmTaskAssignRuleConvert.INSTANCE.convertList(userTasks, rules);
    }

    @Override
    public Long createTaskAssignRule(@Valid BpmTaskAssignRuleCreateReqVO reqVO) {
        // 校验参数
        validTaskAssignRuleOptions(reqVO.getType(), reqVO.getOptions());
        // 校验是否已经配置
        BpmTaskAssignRuleDO existRule = taskRuleMapper.selectListByModelIdAndTaskDefinitionKey(reqVO.getModelId(), reqVO.getTaskDefinitionKey());
        if (existRule != null) {
            String msg = StrUtil.format("流程({}) 的任务({}) 已经存在分配规则", reqVO.getModelId(), reqVO.getTaskDefinitionKey());
            throw new BusinessException(msg);
        }

        // 存储
        BpmTaskAssignRuleDO rule = BpmTaskAssignRuleConvert.INSTANCE.convert(reqVO);
        rule.setProcessDefinitionId(BpmTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL); // 只有流程模型，才允许新建
        taskRuleMapper.insert(rule);
        return rule.getId();
    }

    @Override
    public void updateTaskAssignRule(@Valid BpmTaskAssignRuleUpdateReqVO reqVO) {
        // 校验参数
        validTaskAssignRuleOptions(reqVO.getType(), reqVO.getOptions());
        // 校验是否存在
        BpmTaskAssignRuleDO existRule = taskRuleMapper.selectById(reqVO.getId());
        if (existRule == null) {
            throw new BusinessException("流程任务分配规则不存在");
        }
        // 只允许修改流程模型的规则
        if (!Objects.equals(BpmTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL, existRule.getProcessDefinitionId())) {
            throw new BusinessException("只有流程模型的任务分配规则，才允许被修改");
        }

        // 执行更新
        taskRuleMapper.updateById(BpmTaskAssignRuleConvert.INSTANCE.convert(reqVO));
    }

    @Override
    public boolean isTaskAssignRulesEquals(String modelId, String processDefinitionId) {
        // 调用 VO 接口的原因是，过滤掉流程模型不需要的规则，保持和 copyTaskAssignRules 方法的一致性
        List<BpmTaskAssignRuleRespVO> modelRules = getTaskAssignRuleList(modelId, null);
        List<BpmTaskAssignRuleRespVO> processInstanceRules = getTaskAssignRuleList(null, processDefinitionId);
        if (modelRules.size() != processInstanceRules.size()) {
            return false;
        }

        // 遍历，匹配对应的规则
        Map<String, BpmTaskAssignRuleRespVO> processInstanceRuleMap = MapUtil.newHashMap();
        processInstanceRuleMap = CollUtil.toMap(processInstanceRules, processInstanceRuleMap, BpmTaskAssignRuleRespVO::getTaskDefinitionKey);
        for (BpmTaskAssignRuleRespVO modelRule : modelRules) {
            BpmTaskAssignRuleRespVO processInstanceRule = processInstanceRuleMap.get(modelRule.getTaskDefinitionKey());
            if (processInstanceRule == null) {
                return false;
            }
            if (!ObjectUtil.equals(modelRule.getType(), processInstanceRule.getType()) || !ObjectUtil.equal(modelRule.getOptions(), processInstanceRule.getOptions())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void copyTaskAssignRules(String fromModelId, String toProcessDefinitionId) {
        List<BpmTaskAssignRuleRespVO> rules = getTaskAssignRuleList(fromModelId, null);
        if (CollUtil.isEmpty(rules)) {
            return;
        }
        // 开始复制
        List<BpmTaskAssignRuleDO> newRules = BpmTaskAssignRuleConvert.INSTANCE.convertList2(rules);
        newRules.forEach(rule -> {
            rule.setProcessDefinitionId(toProcessDefinitionId);
            rule.setId(null);
            rule.setGmtCreate(null);
            rule.setGmtModified(null);
        });
        this.saveBatch(newRules);
    }

    @Override
    public void checkTaskAssignRuleAllConfig(String id) {
        // 一个用户任务都没配置，所以无需配置规则
        List<BpmTaskAssignRuleRespVO> taskAssignRules = getTaskAssignRuleList(id, null);
        if (CollUtil.isEmpty(taskAssignRules)) {
            return;
        }
        // 校验未配置规则的任务
        taskAssignRules.forEach(rule -> {
            if (CollUtil.isEmpty(rule.getOptions())) {
                String msg = StrUtil.format("部署流程失败，原因：用户任务({})未配置分配规则，请点击【修改流程】按钮进行配置", rule.getTaskDefinitionName());
                throw new BusinessException(msg);
            }
        });
    }

    private void validTaskAssignRuleOptions(String typeStr, Set<Long> options) {
        Integer type = NumberUtil.parseInt(typeStr);
        if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.ROLE.getType())) {
//            roleApi.validRoleList(options);
        } else if (Arrays.asList(BpmTaskAssignRuleTypeEnum.DEPT_MEMBER.getType(), BpmTaskAssignRuleTypeEnum.DEPT_LEADER.getType()).contains(type)) {
//            deptApi.validateDeptList(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.POST.getType())) {
//            postApi.validPostList(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.USER.getType())) {
//            adminUserApi.validateUserList(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.USER_GROUP.getType())) {
//            userGroupService.validUserGroups(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.SCRIPT.getType())) {
//            dictDataApi.validateDictDataList(DictTypeConstants.TASK_ASSIGN_SCRIPT, CollectionUtils.convertSet(options, String::valueOf));
        } else {
            throw new IllegalArgumentException(format("未知的规则类型({})", type));
        }
    }

    @Override
//    @DataPermission(enable = false) // 忽略数据权限，不然分配会存在问题
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        BpmTaskAssignRuleDO rule = getTaskRule(execution);
        return calculateTaskCandidateUsers(execution, rule);
    }

    @VisibleForTesting
    BpmTaskAssignRuleDO getTaskRule(DelegateExecution execution) {
        List<BpmTaskAssignRuleDO> taskRules = getTaskAssignRuleListByProcessDefinitionId(execution.getProcessDefinitionId(), execution.getCurrentActivityId());
        if (CollUtil.isEmpty(taskRules)) {
            throw new FlowableException(format("流程任务({}/{}/{}) 找不到符合的任务规则", execution.getId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId()));
        }
        if (taskRules.size() > 1) {
            throw new FlowableException(format("流程任务({}/{}/{}) 找到过多任务规则({})", execution.getId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId()));
        }
        return taskRules.get(0);
    }

    @VisibleForTesting
    Set<Long> calculateTaskCandidateUsers(DelegateExecution execution, BpmTaskAssignRuleDO rule) {
        Set<Long> assigneeUserIds = null;
        if (Objects.equals(BpmTaskAssignRuleTypeEnum.ROLE.getType(), rule.getType())) {
//            assigneeUserIds = calculateTaskCandidateUsersByRole(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.DEPT_MEMBER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByDeptMember(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.DEPT_LEADER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByDeptLeader(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.POST.getType(), rule.getType())) {
//            assigneeUserIds = calculateTaskCandidateUsersByPost(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.USER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByUser(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.USER_GROUP.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByUserGroup(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.SCRIPT.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByScript(execution, rule);
        }

        // 移除被禁用的用户
        removeDisableUsers(assigneeUserIds);
        // 如果候选人为空，抛出异常
        if (CollUtil.isEmpty(assigneeUserIds)) {
            log.error("[calculateTaskCandidateUsers][流程任务({}/{}/{}) 任务规则({}) 找不到候选人]", execution.getId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId(), JSONUtil.toJsonStr(rule));
            throw new BusinessException("操作失败，原因：找不到任务的审批人！");
        }
        return assigneeUserIds;
    }

//    private Set<Long> calculateTaskCandidateUsersByRole(BpmTaskAssignRuleDO rule) {
//        return permissionApi.getUserRoleIdListByRoleIds(rule.getOptions());
//    }

    private Set<Long> calculateTaskCandidateUsersByDeptMember(BpmTaskAssignRuleDO rule) {
        List<UserVO> users = userService.listByDeptIds(rule.getOptions());
        return users.stream().map(UserVO::getId).collect(Collectors.toSet());
    }

    private Set<Long> calculateTaskCandidateUsersByDeptLeader(BpmTaskAssignRuleDO rule) {
        List<DeptSmallDto> depts = deptService.listByIds(rule.getOptions());
        return depts.stream().map(DeptSmallDto::getLeaderUserId).collect(Collectors.toSet());
    }

//    private Set<Long> calculateTaskCandidateUsersByPost(BpmTaskAssignRuleDO rule) {
//        List<AdminUserRespDTO> users = adminUserApi.getUsersByPostIds(rule.getOptions());
//        return users.stream().map(AdminUserRespDTO::getId).collect(Collectors.toSet());
//    }

    private Set<Long> calculateTaskCandidateUsersByUser(BpmTaskAssignRuleDO rule) {
        return rule.getOptions();
    }

    private Set<Long> calculateTaskCandidateUsersByUserGroup(BpmTaskAssignRuleDO rule) {
        List<BpmUserGroupDO> userGroups = userGroupService.getUserGroupList(rule.getOptions());
        Set<Long> userIds = new HashSet<>();
        userGroups.forEach(group -> userIds.addAll(group.getMemberUserIds()));
        return userIds;
    }

    private Set<Long> calculateTaskCandidateUsersByScript(DelegateExecution execution, BpmTaskAssignRuleDO rule) {
        // 获得对应的脚本
        List<BpmTaskAssignScript> scripts = new ArrayList<>(rule.getOptions().size());
        rule.getOptions().forEach(id -> {
            BpmTaskAssignScript script = scriptMap.get(id);
            if (script == null) {
                String msg = StrUtil.format("操作失败，原因：任务分配脚本({}) 不存在", id);
                throw new BusinessException(msg);
            }
            scripts.add(script);
        });
        // 逐个计算任务
        Set<Long> userIds = new HashSet<>();
        scripts.forEach(script -> CollUtil.addAll(userIds, script.calculateTaskCandidateUsers(execution)));
        return userIds;
    }

    @VisibleForTesting
    void removeDisableUsers(Set<Long> assigneeUserIds) {
        if (CollUtil.isEmpty(assigneeUserIds)) {
            return;
        }
        List<UserVO> users = userService.listByIds(assigneeUserIds);
        Map<Long, UserVO> userMap = MapUtil.newHashMap();
        userMap = CollUtil.toMap(users, userMap, UserVO::getId);

        Map<Long, UserVO> finalUserMap = userMap;
        assigneeUserIds.removeIf(id -> {
            UserVO user = finalUserMap.get(id);
            return user == null || BooleanUtil.isFalse(user.getEnabled());
        });
    }

}
