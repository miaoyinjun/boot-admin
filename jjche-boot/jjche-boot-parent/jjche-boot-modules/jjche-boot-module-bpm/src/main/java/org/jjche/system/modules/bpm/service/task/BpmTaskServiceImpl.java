package org.jjche.system.modules.bpm.service.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.jjche.common.dto.DeptSmallDto;
import org.jjche.common.dto.UserVO;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.jjche.common.service.IDeptService;
import org.jjche.common.service.IUserService;
import org.jjche.system.modules.bpm.convert.task.BpmTaskConvert;
import org.jjche.system.modules.bpm.dal.dataobject.task.BpmTaskExtDO;
import org.jjche.system.modules.bpm.enums.task.BpmProcessInstanceDeleteReasonEnum;
import org.jjche.system.modules.bpm.enums.task.BpmProcessInstanceResultEnum;
import org.jjche.system.modules.bpm.mapper.BpmTaskExtMapper;
import org.jjche.system.modules.bpm.rest.admin.task.vo.task.*;
import org.jjche.system.modules.bpm.service.message.BpmMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程任务实例 Service 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Slf4j
@Service
public class BpmTaskServiceImpl implements BpmTaskService {

    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private IUserService userService;
    @Resource
    private IDeptService deptService;
    @Resource
    private BpmTaskExtMapper taskExtMapper;
    @Resource
    private BpmMessageService messageService;

    @Override
    public MyPage<BpmTaskTodoPageItemRespVO> getTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageVO) {
        // 查询待办任务
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(userId)) // 分配给自己
                .orderByTaskCreateTime().desc(); // 创建时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (ArrayUtil.get(pageVO.getCreateTime(), 0) != null) {
            taskQuery.taskCreatedAfter(DateUtil.date(pageVO.getCreateTime()[0]));
        }
        if (ArrayUtil.get(pageVO.getCreateTime(), 1) != null) {
            taskQuery.taskCreatedBefore(DateUtil.date(pageVO.getCreateTime()[1]));
        }
        // 执行查询
        List<Task> tasks = taskQuery.listPage((int) pageVO.getPageIndex(), (int) pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            MyPage myPage = new MyPage();
            myPage.setTotal(taskQuery.count());
            return myPage;
        }

        Set<String> instanceIds = tasks.stream().map(Task::getProcessInstanceId).collect(Collectors.toSet());

        // 获得 ProcessInstance Map
        Map<String, ProcessInstance> processInstanceMap = processInstanceService.getProcessInstanceMap(instanceIds);
        // 获得 User Map
        Set<Long> startUserIds = processInstanceMap.values().stream().map(o -> Long.valueOf(o.getStartUserId())).collect(Collectors.toSet());
        List<UserVO> users = userService.listByIds(startUserIds);

        Map<Long, UserVO> userMap = MapUtil.newHashMap();
        userMap = CollUtil.toMap(users, userMap, UserVO::getId);
        // 拼接结果
        MyPage myPage = new MyPage();
        myPage.setNewRecords(BpmTaskConvert.INSTANCE.convertList1(tasks, processInstanceMap, userMap));
        myPage.setTotal(taskQuery.count());
        return myPage;
    }

    @Override
    public MyPage<BpmTaskDonePageItemRespVO> getDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageVO) {
        // 查询已办任务
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery().finished() // 已完成
                .taskAssignee(String.valueOf(userId)) // 分配给自己
                .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (pageVO.getBeginCreateTime() != null) {
            taskQuery.taskCreatedAfter(DateUtil.date(pageVO.getBeginCreateTime()));
        }
        if (pageVO.getEndCreateTime() != null) {
            taskQuery.taskCreatedBefore(DateUtil.date(pageVO.getEndCreateTime()));
        }
        // 执行查询
        List<HistoricTaskInstance> tasks = taskQuery.listPage((int) pageVO.getPageIndex(), (int) pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            MyPage myPage = new MyPage();
            myPage.setTotal(taskQuery.count());
            return myPage;
        }

        // 获得 TaskExtDO Map
        Set<String> taskInstanceIds = tasks.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet());

        List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(taskInstanceIds);

        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = MapUtil.newHashMap();
        bpmTaskExtDOMap = CollUtil.toMap(bpmTaskExtDOs, bpmTaskExtDOMap, BpmTaskExtDO::getTaskId);

        // 获得 ProcessInstance Map
        Set<String> processInstanceIds = tasks.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet());

        Map<String, HistoricProcessInstance> historicProcessInstanceMap = processInstanceService.getHistoricProcessInstanceMap(processInstanceIds);
        // 获得 User Map
        Set<Long> startUserIds = historicProcessInstanceMap.values().stream().map(instance -> Long.valueOf(instance.getStartUserId())).collect(Collectors.toSet());


        List<UserVO> users = userService.listByIds(startUserIds);

        Map<Long, UserVO> userMap = MapUtil.empty();
        userMap = CollUtil.toMap(users, userMap, UserVO::getId);
        // 拼接结果
        MyPage myPage = new MyPage();
        myPage.setNewRecords(BpmTaskConvert.INSTANCE.convertList2(tasks, bpmTaskExtDOMap, historicProcessInstanceMap, userMap));
        myPage.setTotal(taskQuery.count());
        return myPage;
    }

    @Override
    public List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
    }

    @Override
    public List<BpmTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId) {
        // 获得任务列表
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
                .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        // 获得 TaskExtDO Maps
        Set<String> taskIds = tasks.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet());
        List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(taskIds);
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = MapUtil.empty();
        bpmTaskExtDOMap = CollUtil.toMap(bpmTaskExtDOs, bpmTaskExtDOMap, BpmTaskExtDO::getTaskId);

        // 获得 ProcessInstance Map
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User Map
        Set<Long> userIds = tasks.stream().map(task -> NumberUtil.parseLong(task.getAssignee())).collect(Collectors.toSet());
        userIds.add(NumberUtil.parseLong(processInstance.getStartUserId()));

        List<UserVO> users = userService.listByIds(userIds);

        Map<Long, UserVO> userMap = MapUtil.empty();
        userMap = CollUtil.toMap(users, userMap, UserVO::getId);

        // 获得 Dept Map
        Set<Long> deptIds = userMap.values().stream().map(UserVO::getDeptId).collect(Collectors.toSet());
        List<DeptSmallDto> deptSmalls = deptService.listByIds(deptIds);
        Map<Long, DeptSmallDto> deptMap = MapUtil.empty();
        deptMap = CollUtil.toMap(deptSmalls, deptMap, DeptSmallDto::getId);
        // 拼接数据
        return BpmTaskConvert.INSTANCE.convertList3(tasks, bpmTaskExtDOMap, processInstance, userMap, deptMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(Long userId, @Valid BpmTaskApproveReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw new BusinessException("流程实例不存在");
        }

        // 完成任务，审批通过
        taskService.complete(task.getId(), instance.getProcessVariables());

        // 更新任务拓展表为通过
        BpmTaskExtDO bpmTaskExt = new BpmTaskExtDO();
        bpmTaskExt.setTaskId(task.getId());
        bpmTaskExt.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
        bpmTaskExt.setReason(reqVO.getReason());
        taskExtMapper.updateByTaskId(bpmTaskExt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(Long userId, @Valid BpmTaskRejectReqVO reqVO) {
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw new BusinessException("流程实例不存在");
        }

        // 更新流程实例为不通过
        processInstanceService.updateProcessInstanceExtReject(instance.getProcessInstanceId(), reqVO.getReason());

        // 更新任务拓展表为不通过
        BpmTaskExtDO bpmTaskExt = new BpmTaskExtDO();

        bpmTaskExt.setTaskId(task.getId());
        bpmTaskExt.setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
        bpmTaskExt.setEndTime(LocalDateTime.now());
        bpmTaskExt.setReason(reqVO.getReason());
        taskExtMapper.updateByTaskId(bpmTaskExt);
    }

    @Override
    public void updateTaskAssignee(Long userId, BpmTaskUpdateAssigneeReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 更新负责人
        updateTaskAssignee(task.getId(), reqVO.getAssigneeUserId());
    }

    @Override
    public void updateTaskAssignee(String id, Long userId) {
        taskService.setAssignee(id, String.valueOf(userId));
    }

    /**
     * 校验任务是否存在， 并且是否是分配给自己的任务
     *
     * @param userId 用户 id
     * @param taskId task id
     */
    private Task checkTask(Long userId, String taskId) {
        Task task = getTask(taskId);
        if (task == null) {
            throw new BusinessException("审批任务失败，原因：该任务不处于未审批");
        }
        if (!Objects.equals(userId, NumberUtil.parseLong(task.getAssignee()))) {
            throw new BusinessException("审批任务失败，原因：该任务的审批人不是你");
        }
        return task;
    }

    @Override
    public void createTaskExt(Task task) {
        BpmTaskExtDO taskExtDO = BpmTaskConvert.INSTANCE.convert2TaskExt(task);
        taskExtDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        taskExtMapper.insert(taskExtDO);
    }

    @Override
    public void updateTaskExtComplete(Task task) {
        BpmTaskExtDO taskExtDO = BpmTaskConvert.INSTANCE.convert2TaskExt(task);
        taskExtDO.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
        // 不设置也问题不大，因为 Complete 一般是审核通过，已经设置
        taskExtDO.setEndTime(LocalDateTime.now());
        taskExtMapper.updateByTaskId(taskExtDO);
    }

    @Override
    public void updateTaskExtCancel(String taskId) {
        // 需要在事务提交后，才进行查询。不然查询不到历史的原因
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                // 可能只是活动，不是任务，所以查询不到
                HistoricTaskInstance task = getHistoricTask(taskId);
                if (task == null) {
                    return;
                }

                // 如果任务拓展表已经是完成的状态，则跳过
                BpmTaskExtDO taskExt = taskExtMapper.selectByTaskId(taskId);
                if (taskExt == null) {
                    log.error("[updateTaskExtCancel][taskId({}) 查找不到对应的记录，可能存在问题]", taskId);
                    return;
                }
                // 如果已经是最终的结果，则跳过
                if (BpmProcessInstanceResultEnum.isEndResult(taskExt.getResult())) {
                    log.error("[updateTaskExtCancel][taskId({}) 处于结果({})，无需进行更新]", taskId, taskExt.getResult());
                    return;
                }

                // 更新任务
                BpmTaskExtDO bpmTaskExtDO = new BpmTaskExtDO();
                bpmTaskExtDO.setId(taskExt.getId());
                bpmTaskExtDO.setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());
                bpmTaskExtDO.setEndTime(LocalDateTime.now());
                bpmTaskExtDO.setReason(BpmProcessInstanceDeleteReasonEnum.translateReason(task.getDeleteReason()));
                taskExtMapper.updateById(bpmTaskExtDO);
            }

        });
    }

    @Override
    public void updateTaskExtAssign(Task task) {
        BpmTaskExtDO taskExtDO = new BpmTaskExtDO();
        taskExtDO.setAssigneeUserId(NumberUtil.parseLong(task.getAssignee()));
        taskExtDO.setTaskId(task.getId());
        taskExtMapper.updateByTaskId(taskExtDO);
        // 发送通知。在事务提交时，批量执行操作，所以直接查询会无法查询到 ProcessInstance，所以这里是通过监听事务的提交来实现。
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ProcessInstance processInstance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
                UserVO startUser = userService.findById(Long.valueOf(processInstance.getStartUserId()));
                messageService.sendMessageWhenTaskAssigned(BpmTaskConvert.INSTANCE.convert(processInstance, startUser, task));
            }
        });
    }

    private Task getTask(String id) {
        return taskService.createTaskQuery().taskId(id).singleResult();
    }

    private HistoricTaskInstance getHistoricTask(String id) {
        return historyService.createHistoricTaskInstanceQuery().taskId(id).singleResult();
    }

}
