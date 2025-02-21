package org.jjche.bpm.modules.definition.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.jjche.bpm.config.flowable.core.behavior.script.BpmTaskAssignScript;
import org.jjche.bpm.enums.BpmErrorCodeEnum;
import org.jjche.bpm.modules.definition.domain.BpmTaskAssignRuleDO;
import org.jjche.bpm.modules.definition.enums.BpmTaskAssignRuleTypeEnum;
import org.jjche.bpm.modules.definition.mapper.BpmTaskAssignRuleMapper;
import org.jjche.bpm.modules.definition.mapstruct.BpmTaskAssignRuleConvert;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleRespVO;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import org.jjche.bpm.modules.group.domain.BpmUserGroupDO;
import org.jjche.bpm.modules.group.service.BpmUserGroupService;
import org.jjche.bpm.onstants.DictTypeConstants;
import org.jjche.common.dto.DeptSmallDTO;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.exception.util.BusinessExceptionUtil;
import org.jjche.common.vo.UserVO;
import org.jjche.flowable.util.FlowableUtils;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.sys.api.SysBaseApi;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BPM 任务分配规则 Service 实现类
 */
@Service
@Validated
@RequiredArgsConstructor
public class BpmTaskAssignRuleService extends MyServiceImpl<BpmTaskAssignRuleMapper, BpmTaskAssignRuleDO> {
    @Resource
    @Lazy // 解决循环依赖
    private BpmModelService modelService;
    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessDefinitionService processDefinitionService;
    private final BpmUserGroupService userGroupService;
    private final SysBaseApi sysBaseApi;
    private final BpmTaskAssignRuleConvert bpmTaskAssignRuleConvert;
    /**
     * 任务分配脚本
     */
    private Map<Long, BpmTaskAssignScript> scriptMap = Collections.emptyMap();

    @Resource
    public void setScripts(List<BpmTaskAssignScript> scripts) {
        Map<Long, BpmTaskAssignScript> map = MapUtil.newHashMap();
        this.scriptMap = CollUtil.toMap(scripts, map, script -> script.getEnum().getId());
    }

    /**
     * 获得流程定义的任务分配规则数组
     *
     * @param processDefinitionId 流程定义的编号
     * @param taskDefinitionKey   流程任务定义的 Key。允许空
     * @return 任务规则数组
     */
    public List<BpmTaskAssignRuleDO> getTaskAssignRuleListByProcessDefinitionId(String processDefinitionId, String taskDefinitionKey) {
        return this.baseMapper.selectListByProcessDefinitionId(processDefinitionId, taskDefinitionKey);
    }

    /**
     * 获得流程模型的任务规则数组
     *
     * @param modelId 流程模型的编号
     * @return 任务规则数组
     */
    public List<BpmTaskAssignRuleDO> getTaskAssignRuleListByModelId(String modelId) {
        return this.baseMapper.selectListByModelId(modelId);
    }

    /**
     * 获得流程定义的任务分配规则数组
     *
     * @param modelId             流程模型的编号
     * @param processDefinitionId 流程定义的编号
     * @return 任务规则数组
     */
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
        return bpmTaskAssignRuleConvert.convertList(userTasks, rules);
    }

    /**
     * 创建任务分配规则
     *
     * @param reqVO 创建信息
     * @return 规则编号
     */
    public Long createTaskAssignRule(@Valid BpmTaskAssignRuleCreateReqVO reqVO) {
        // 校验参数
        validTaskAssignRuleOptions(reqVO.getType(), reqVO.getOptions());
        // 校验是否已经配置
        BpmTaskAssignRuleDO existRule = this.baseMapper.selectListByModelIdAndTaskDefinitionKey(reqVO.getModelId(), reqVO.getTaskDefinitionKey());
        if (existRule != null) {
            throw BusinessExceptionUtil.exception(BpmErrorCodeEnum.RULE_ALREADY_FOUND, reqVO.getModelId(), reqVO.getTaskDefinitionKey());
        }

        // 存储
        BpmTaskAssignRuleDO rule = bpmTaskAssignRuleConvert.convert(reqVO);
        rule.setProcessDefinitionId(BpmTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL); // 只有流程模型，才允许新建
        this.baseMapper.insert(rule);
        return rule.getId();
    }

    /**
     * 更新任务分配规则
     *
     * @param reqVO 创建信息
     */
    public void updateTaskAssignRule(@Valid BpmTaskAssignRuleUpdateReqVO reqVO) {
        // 校验参数
        validTaskAssignRuleOptions(reqVO.getType(), reqVO.getOptions());
        // 校验是否存在
        BpmTaskAssignRuleDO existRule = this.baseMapper.selectById(reqVO.getId());
        AssertUtil.notNull(existRule, BpmErrorCodeEnum.RULE_NOT_FOUND);
        // 只允许修改流程模型的规则
        if (!Objects.equals(BpmTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL, existRule.getProcessDefinitionId())) {
            throw BusinessExceptionUtil.exception(BpmErrorCodeEnum.RULE_NOT_ALLOWED_UPDATE);
        }

        // 执行更新
        this.baseMapper.updateById(bpmTaskAssignRuleConvert.convert(reqVO));
    }

    /**
     * 判断指定流程模型和流程定义的分配规则是否相等
     *
     * @param modelId             流程模型编号
     * @param processDefinitionId 流程定义编号
     * @return 是否相等
     */
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

    /**
     * 将流程流程模型的任务分配规则，复制一份给流程定义
     * 目的：每次流程模型部署时，都会生成一个新的流程定义，此时考虑到每次部署的流程不可变性，所以需要复制一份给该流程定义
     *
     * @param fromModelId           流程模型编号
     * @param toProcessDefinitionId 流程定义编号
     */
    public void copyTaskAssignRules(String fromModelId, String toProcessDefinitionId) {
        List<BpmTaskAssignRuleRespVO> rules = getTaskAssignRuleList(fromModelId, null);
        if (CollUtil.isEmpty(rules)) {
            return;
        }
        // 开始复制
        List<BpmTaskAssignRuleDO> newRules = bpmTaskAssignRuleConvert.convertList2(rules);
        newRules.forEach(rule -> {
            rule.setProcessDefinitionId(toProcessDefinitionId);
            rule.setId(null);
            rule.setGmtCreate(null);
            rule.setGmtModified(null);
        });
        this.saveBatch(newRules);
    }

    /**
     * 校验流程模型的任务分配规则全部都配置了
     * 目的：如果有规则未配置，会导致流程任务找不到负责人，进而流程无法进行下去！
     *
     * @param id 流程模型编号
     */
    public void checkTaskAssignRuleAllConfig(String id) {
        // 一个用户任务都没配置，所以无需配置规则
        List<BpmTaskAssignRuleRespVO> taskAssignRules = getTaskAssignRuleList(id, null);
        if (CollUtil.isEmpty(taskAssignRules)) {
            return;
        }
        // 校验未配置规则的任务
        taskAssignRules.forEach(rule -> {
            if (CollUtil.isEmpty(rule.getOptions())) {
                throw BusinessExceptionUtil.exception(BpmErrorCodeEnum.RULE_NOT_ALLOWED_DEPLOY, rule.getTaskDefinitionName());
            }
        });
    }

    private void validTaskAssignRuleOptions(String typeStr, Set<Long> options) {
        Integer type = NumberUtil.parseInt(typeStr);
        if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.ROLE.getType())) {
            sysBaseApi.validRoleList(options);
        } else if (Arrays.asList(BpmTaskAssignRuleTypeEnum.DEPT_MEMBER.getType(), BpmTaskAssignRuleTypeEnum.DEPT_LEADER.getType()).contains(type)) {
            sysBaseApi.validDeptList(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.POST.getType())) {
            sysBaseApi.validJobIds(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.USER.getType())) {
            sysBaseApi.validUserList(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.USER_GROUP.getType())) {
            userGroupService.validUserGroups(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.SCRIPT.getType())) {
            sysBaseApi.validDictList(DictTypeConstants.TASK_ASSIGN_SCRIPT, Convert.toSet(String.class, options));
        } else {
            throw BusinessExceptionUtil.exception(BpmErrorCodeEnum.RULE_UNKNOWN_FOUND, type);
        }
    }

    /**
     * 计算当前执行任务的处理人
     *
     * @param execution 执行任务
     * @return 处理人的编号数组
     */
//    @DataPermission(enable = false) // 忽略数据权限，不然分配会存在问题
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        BpmTaskAssignRuleDO rule = getTaskRule(execution);
        return calculateTaskCandidateUsers(execution, rule);
    }

    @VisibleForTesting
    BpmTaskAssignRuleDO getTaskRule(DelegateExecution execution) {
        List<BpmTaskAssignRuleDO> taskRules = getTaskAssignRuleListByProcessDefinitionId(execution.getProcessDefinitionId(), execution.getCurrentActivityId());
        if (CollUtil.isEmpty(taskRules)) {
            throw BusinessExceptionUtil.exception(BpmErrorCodeEnum.RULE_ACCORD_NOT_FOUND, execution.getProcessDefinitionId(), execution.getCurrentActivityId());
        }
        if (taskRules.size() > 1) {
            throw BusinessExceptionUtil.exception(BpmErrorCodeEnum.RULE_FOUND_MORE, execution.getProcessDefinitionId(), execution.getCurrentActivityId());
        }
        return taskRules.get(0);
    }

    @VisibleForTesting
    Set<Long> calculateTaskCandidateUsers(DelegateExecution execution, BpmTaskAssignRuleDO rule) {
        Set<Long> assigneeUserIds = null;
        if (Objects.equals(BpmTaskAssignRuleTypeEnum.ROLE.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByRole(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.DEPT_MEMBER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByDeptMember(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.DEPT_LEADER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByDeptLeader(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.POST.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByPost(rule);
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
            StaticLog.error("[calculateTaskCandidateUsers][流程任务({}/{}/{}) 任务规则({}) 找不到候选人]", execution.getId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId(), JSONUtil.toJsonStr(rule));
            throw BusinessExceptionUtil.exception(BpmErrorCodeEnum.TASK_NOT_FOUND_ASSIGNEE_ERROR);
        }
        return assigneeUserIds;
    }

    private Set<Long> calculateTaskCandidateUsersByRole(BpmTaskAssignRuleDO rule) {
        return sysBaseApi.getUserRoleIdByRoleIds(rule.getOptions());
    }

    private Set<Long> calculateTaskCandidateUsersByDeptMember(BpmTaskAssignRuleDO rule) {
        List<UserVO> users = sysBaseApi.listUserByDeptIds(rule.getOptions());
        return users.stream().map(UserVO::getId).collect(Collectors.toSet());
    }

    private Set<Long> calculateTaskCandidateUsersByDeptLeader(BpmTaskAssignRuleDO rule) {
        List<DeptSmallDTO> depts = sysBaseApi.listDeptSmallByIds(rule.getOptions());
        return depts.stream().map(DeptSmallDTO::getLeaderUserId).collect(Collectors.toSet());
    }

    private Set<Long> calculateTaskCandidateUsersByPost(BpmTaskAssignRuleDO rule) {
        return sysBaseApi.listUserIdsByJobIds(rule.getOptions());
    }

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
            AssertUtil.notNull(script, BpmErrorCodeEnum.RULE_SCRIPT_NOT_FOUND, id);
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
        List<UserVO> users = sysBaseApi.listUserByIds(assigneeUserIds);
        Map<Long, UserVO> userMap = MapUtil.newHashMap();
        userMap = CollUtil.toMap(users, userMap, UserVO::getId);

        Map<Long, UserVO> finalUserMap = userMap;
        assigneeUserIds.removeIf(id -> {
            UserVO user = finalUserMap.get(id);
            return user == null || BooleanUtil.isFalse(user.getEnabled());
        });
    }

}
