package org.jjche.bpm.modules.task.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.jjche.bpm.modules.message.service.BpmMessageService;
import org.jjche.bpm.modules.task.api.vo.task.*;
import org.jjche.bpm.modules.task.domain.BpmTaskExtDO;
import org.jjche.bpm.modules.task.enums.BpmProcessInstanceDeleteReasonEnum;
import org.jjche.bpm.modules.task.enums.BpmProcessInstanceResultEnum;
import org.jjche.bpm.modules.task.mapper.BpmTaskExtMapper;
import org.jjche.bpm.modules.task.mapstruct.BpmTaskConvert;
import org.jjche.common.dto.DeptSmallDTO;
import org.jjche.common.dto.UserVO;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.system.modules.dept.api.DeptApi;
import org.jjche.system.modules.user.api.UserApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程任务实例 Service 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Service
@RequiredArgsConstructor
public class BpmTaskService extends MyServiceImpl<BpmTaskExtMapper, BpmTaskExtDO> {

    private final TaskService taskService;
    private final HistoryService historyService;
    private final BpmProcessInstanceService processInstanceService;
    private final UserApi userApi;
    private final DeptApi deptApi;
    private final BpmMessageService messageService;
    private final BpmTaskConvert bpmTaskConvert;
    /**
     * 获得待办的流程任务分页
     *
     * @param userId    用户编号
     * @param pageVO 分页请求
     *
     * @return 流程任务分页
     */
    public MyPage<BpmTaskTodoPageItemRespVO> getTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageVO) {
        // 查询待办任务
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(userId)) // 分配给自己
                .orderByTaskCreateTime().desc(); // 创建时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }

        List<Timestamp> createTime = pageVO.getCreateTime();
        if (CollUtil.isNotEmpty(createTime)) {
            taskQuery.taskCreatedAfter(CollUtil.getFirst(createTime));
            taskQuery.taskCreatedBefore(CollUtil.getLast(createTime));
        }
        // 执行查询
        List<Task> tasks = taskQuery.listPage((int) pageVO.getPageIndex() - 1, (int) pageVO.getPageSize());
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
        List<UserVO> users = userApi.listByIds(startUserIds);

        Map<Long, UserVO> userMap = MapUtil.newHashMap();
        userMap = CollUtil.toMap(users, userMap, UserVO::getId);
        // 拼接结果
        MyPage myPage = new MyPage();
        myPage.setNewRecords(bpmTaskConvert.convertList1(tasks, processInstanceMap, userMap));
        myPage.setTotal(taskQuery.count());
        return myPage;
    }

    /**
     * 获得已办的流程任务分页
     *
     * @param userId    用户编号
     * @param pageVO 分页请求
     *
     * @return 流程任务分页
     */
    public MyPage<BpmTaskDonePageItemRespVO> getDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageVO) {
        // 查询已办任务
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery().finished() // 已完成
                .taskAssignee(String.valueOf(userId)) // 分配给自己
                .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        List<Timestamp> createTime = pageVO.getCreateTime();
        if (CollUtil.isNotEmpty(createTime)) {
            taskQuery.taskCreatedAfter(CollUtil.getFirst(createTime));
            taskQuery.taskCreatedBefore(CollUtil.getLast(createTime));
        }
        // 执行查询
        List<HistoricTaskInstance> tasks = taskQuery.listPage((int) pageVO.getPageIndex() - 1, (int) pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            MyPage myPage = new MyPage();
            myPage.setTotal(taskQuery.count());
            return myPage;
        }

        // 获得 TaskExtDO Map
        Set<String> taskInstanceIds = tasks.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet());

        List<BpmTaskExtDO> bpmTaskExtDOs = this.baseMapper.selectListByTaskIds(taskInstanceIds);

        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = MapUtil.newHashMap();
        bpmTaskExtDOMap = CollUtil.toMap(bpmTaskExtDOs, bpmTaskExtDOMap, BpmTaskExtDO::getTaskId);

        // 获得 ProcessInstance Map
        Set<String> processInstanceIds = tasks.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet());

        Map<String, HistoricProcessInstance> historicProcessInstanceMap = processInstanceService.getHistoricProcessInstanceMap(processInstanceIds);
        // 获得 User Map
        Set<Long> startUserIds = historicProcessInstanceMap.values().stream().map(instance -> Long.valueOf(instance.getStartUserId())).collect(Collectors.toSet());

        List<UserVO> users = userApi.listByIds(startUserIds);

        Map<Long, UserVO> userMap = MapUtil.newHashMap();
        userMap = CollUtil.toMap(users, userMap, UserVO::getId);
        // 拼接结果
        MyPage myPage = new MyPage();
        myPage.setNewRecords(bpmTaskConvert.convertList2(tasks, bpmTaskExtDOMap, historicProcessInstanceMap, userMap));
        myPage.setTotal(taskQuery.count());
        return myPage;
    }

    /**
     * 获得流程任务 Map
     *
     * @param processInstanceIds 流程实例的编号数组
     *
     * @return 流程任务 Map
     */
    public Map<String, List<Task>> getTaskMapByProcessInstanceIds(List<String> processInstanceIds) {
        return getTasksByProcessInstanceIds(processInstanceIds).stream().collect(Collectors.groupingBy(Task::getProcessInstanceId));
    }

    /**
     * 获得流程任务列表
     *
     * @param processInstanceIds 流程实例的编号数组
     *
     * @return 流程任务列表
     */
    public List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
    }

    /**
     * 获得指令流程实例的流程任务列表，包括所有状态的
     *
     * @param processInstanceId 流程实例的编号
     *
     * @return 流程任务列表
     */
    public List<BpmTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId) {
        // 获得任务列表
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
                .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        // 获得 TaskExtDO Maps
        Set<String> taskIds = tasks.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet());
        List<BpmTaskExtDO> bpmTaskExtDOs = this.baseMapper.selectListByTaskIds(taskIds);
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = MapUtil.newHashMap();
        bpmTaskExtDOMap = CollUtil.toMap(bpmTaskExtDOs, bpmTaskExtDOMap, BpmTaskExtDO::getTaskId);

        // 获得 ProcessInstance Map
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User Map
        Set<Long> userIds = tasks.stream().map(task -> NumberUtil.parseLong(task.getAssignee())).collect(Collectors.toSet());
        userIds.add(NumberUtil.parseLong(processInstance.getStartUserId()));

        List<UserVO> users = userApi.listByIds(userIds);

        Map<Long, UserVO> userMap = MapUtil.newHashMap();
        userMap = CollUtil.toMap(users, userMap, UserVO::getId);

        // 获得 Dept Map
        Set<Long> deptIds = userMap.values().stream().map(UserVO::getDeptId).collect(Collectors.toSet());
        List<DeptSmallDTO> deptSmalls = deptApi.listByIds(deptIds);
        // 拼接数据
        return bpmTaskConvert.convertList3(tasks, bpmTaskExtDOMap, processInstance, userMap);
    }

    /**
     * 通过任务
     *
     * @param userId 用户编号
     * @param reqVO  通过请求
     */
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
        this.baseMapper.updateByTaskId(bpmTaskExt);
    }

    /**
     * 不通过任务
     *
     * @param userId 用户编号
     * @param reqVO  不通过请求
     */
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
        bpmTaskExt.setEndTime(new Timestamp(DateUtil.date().getTime()));
        bpmTaskExt.setReason(reqVO.getReason());
        this.baseMapper.updateByTaskId(bpmTaskExt);
    }

    /**
     * 将流程任务分配给指定用户
     *
     * @param userId 用户编号
     * @param reqVO  分配请求
     */
    public void updateTaskAssignee(Long userId, BpmTaskUpdateAssigneeReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 更新负责人
        updateTaskAssignee(task.getId(), reqVO.getAssigneeUserId());
    }

    /**
     * 将流程任务分配给指定用户
     *
     * @param id     流程任务编号
     * @param userId 用户编号
     */
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

    /**
     * 创建 Task 拓展记录
     *
     * @param task 任务实体
     */
    public void createTaskExt(Task task) {
        BpmTaskExtDO taskExtDO = bpmTaskConvert.convert2TaskExt(task);
        taskExtDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        this.baseMapper.insert(taskExtDO);
    }

    /**
     * 更新 Task 拓展记录为完成
     *
     * @param task 任务实体
     */
    public void updateTaskExtComplete(Task task) {
        BpmTaskExtDO taskExtDO = bpmTaskConvert.convert2TaskExt(task);
        taskExtDO.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
        // 不设置也问题不大，因为 Complete 一般是审核通过，已经设置
        taskExtDO.setEndTime(new Timestamp(DateUtil.date().getTime()));
        this.baseMapper.updateByTaskId(taskExtDO);
    }

    /**
     * 更新 Task 拓展记录为已取消
     *
     * @param taskId 任务的编号
     */
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
                BpmTaskExtDO taskExt = baseMapper.selectByTaskId(taskId);
                if (taskExt == null) {
                    StaticLog.error("[updateTaskExtCancel][taskId({}) 查找不到对应的记录，可能存在问题]", taskId);
                    return;
                }
                // 如果已经是最终的结果，则跳过
                if (BpmProcessInstanceResultEnum.isEndResult(taskExt.getResult())) {
                    StaticLog.error("[updateTaskExtCancel][taskId({}) 处于结果({})，无需进行更新]", taskId, taskExt.getResult());
                    return;
                }

                // 更新任务
                BpmTaskExtDO bpmTaskExtDO = new BpmTaskExtDO();
                bpmTaskExtDO.setId(taskExt.getId());
                bpmTaskExtDO.setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());
                bpmTaskExtDO.setEndTime(new Timestamp(DateUtil.date().getTime()));
                bpmTaskExtDO.setReason(BpmProcessInstanceDeleteReasonEnum.translateReason(task.getDeleteReason()));
                baseMapper.updateById(bpmTaskExtDO);
            }

        });
    }

    /**
     * 更新 Task 拓展记录，并发送通知
     *
     * @param task 任务实体
     */
    public void updateTaskExtAssign(Task task) {
        BpmTaskExtDO taskExtDO = new BpmTaskExtDO();
        taskExtDO.setAssigneeUserId(NumberUtil.parseLong(task.getAssignee()));
        taskExtDO.setTaskId(task.getId());
        this.baseMapper.updateByTaskId(taskExtDO);
        // 发送通知。在事务提交时，批量执行操作，所以直接查询会无法查询到 ProcessInstance，所以这里是通过监听事务的提交来实现。
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ProcessInstance processInstance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
                UserVO startUser = userApi.findById(Long.valueOf(processInstance.getStartUserId()));
                messageService.sendMessageWhenTaskAssigned(bpmTaskConvert.convert(processInstance, startUser, task));
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
