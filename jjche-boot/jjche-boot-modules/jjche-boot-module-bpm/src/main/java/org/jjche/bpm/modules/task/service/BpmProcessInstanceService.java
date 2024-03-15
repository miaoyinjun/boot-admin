package org.jjche.bpm.modules.task.service;import cn.hutool.core.collection.CollUtil;import cn.hutool.core.date.DateUtil;import cn.hutool.core.lang.Assert;import cn.hutool.core.map.MapUtil;import cn.hutool.core.util.NumberUtil;import cn.hutool.core.util.StrUtil;import lombok.RequiredArgsConstructor;import org.flowable.engine.HistoryService;import org.flowable.engine.RuntimeService;import org.flowable.engine.delegate.event.FlowableCancelledEvent;import org.flowable.engine.history.HistoricProcessInstance;import org.flowable.engine.repository.ProcessDefinition;import org.flowable.engine.runtime.ProcessInstance;import org.flowable.task.api.Task;import org.jjche.bpm.config.bpm.core.event.BpmProcessInstanceResultEventPublisher;import org.jjche.bpm.modules.definition.api.dto.BpmProcessInstanceCreateReqDTO;import org.jjche.bpm.modules.definition.domain.BpmProcessDefinitionExtDO;import org.jjche.bpm.modules.definition.mapper.BpmProcessInstanceExtMapper;import org.jjche.bpm.modules.definition.service.BpmProcessDefinitionService;import org.jjche.bpm.modules.message.service.BpmMessageService;import org.jjche.bpm.modules.task.api.vo.instance.*;import org.jjche.bpm.modules.task.domain.BpmProcessInstanceExtDO;import org.jjche.bpm.modules.task.enums.BpmProcessInstanceDeleteReasonEnum;import org.jjche.bpm.modules.task.enums.BpmProcessInstanceResultEnum;import org.jjche.bpm.modules.task.enums.BpmProcessInstanceStatusEnum;import org.jjche.bpm.modules.task.mapstruct.BpmProcessInstanceConvert;import org.jjche.common.dto.UserVO;import org.jjche.common.exception.BusinessException;import org.jjche.common.param.MyPage;import org.jjche.mybatis.base.service.MyServiceImpl;import org.jjche.system.api.ISysBaseApi;import org.springframework.context.annotation.Lazy;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import org.springframework.validation.annotation.Validated;import javax.annotation.Resource;import javax.validation.Valid;import java.sql.Timestamp;import java.util.List;import java.util.Map;import java.util.Objects;import java.util.Set;import java.util.stream.Collectors;/** * 流程实例 Service 实现类 * * ProcessDefinition & ProcessInstance & Execution & Task 的关系： *     1. <a href="https://blog.csdn.net/bobozai86/article/details/105210414" /> * * HistoricProcessInstance & ProcessInstance 的关系： *     1. <a href=" https://my.oschina.net/843294669/blog/71902" /> * * 简单来说，前者 = 历史 + 运行中的流程实例，后者仅是运行中的流程实例 * * @author 芋道源码 */@Service@Validated@RequiredArgsConstructorpublic class BpmProcessInstanceService extends MyServiceImpl<BpmProcessInstanceExtMapper, BpmProcessInstanceExtDO> {    private final BpmProcessInstanceConvert bpmProcessInstanceConvert;    @Resource    @Lazy // 解决循环依赖    private RuntimeService runtimeService;    @Resource    @Lazy // 解决循环依赖    private BpmTaskService taskService;    @Resource    @Lazy // 解决循环依赖    private BpmProcessDefinitionService processDefinitionService;    @Resource    @Lazy    private HistoryService historyService;    private final ISysBaseApi sysBaseApi;    private final BpmProcessInstanceResultEventPublisher processInstanceResultEventPublisher;    private final BpmMessageService messageService;    /**     * 获得流程实例     *     * @param id 流程实例的编号     * @return 流程实例     */    public ProcessInstance getProcessInstance(String id) {        return runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();    }    /**     * 获得流程实例列表     *     * @param ids 流程实例的编号集合     * @return 流程实例列表     */    public List<ProcessInstance> getProcessInstances(Set<String> ids) {        return runtimeService.createProcessInstanceQuery().processInstanceIds(ids).list();    }    /**     * 获得历史的流程实例 Map     *     * @param ids 流程实例的编号集合     * @return 历史的流程实例列表 Map     */    public Map<String, HistoricProcessInstance> getHistoricProcessInstanceMap(Set<String> ids) {        Map<String, HistoricProcessInstance> map = MapUtil.newHashMap();        map = CollUtil.toMap(getHistoricProcessInstances(ids), map, HistoricProcessInstance::getId);        return map;    }    /**     * 获得流程实例 Map     *     * @param ids 流程实例的编号集合     * @return 流程实例列表 Map     */    public Map<String, ProcessInstance> getProcessInstanceMap(Set<String> ids) {        Map<String, ProcessInstance> map = MapUtil.newHashMap();        map = CollUtil.toMap(getProcessInstances(ids), map, ProcessInstance::getProcessInstanceId);        return map;    }    /**     * 获得流程实例的分页     *     * @param userId 用户编号     * @param pageReqVO 分页请求     * @return 流程实例的分页     */    public MyPage<BpmProcessInstancePageItemRespVO> getMyProcessInstancePage(Long userId,                                                                             BpmProcessInstanceMyPageReqVO pageReqVO) {        // 通过 BpmProcessInstanceExtDO 表，先查询到对应的分页        MyPage<BpmProcessInstanceExtDO> pageResult = this.baseMapper.selectPage(userId, pageReqVO);        if (CollUtil.isEmpty(pageResult.getRecords())) {            MyPage myPage = new MyPage();            myPage.setTotal(pageResult.getTotal());            return myPage;        }        // 获得流程 Task Map        List<String> processInstanceIds = pageResult.getRecords()                .stream().map(BpmProcessInstanceExtDO::getProcessInstanceId).collect(Collectors.toList());        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(processInstanceIds);        // 转换返回        return bpmProcessInstanceConvert.convertPage(pageResult, taskMap);    }    /**     * 创建流程实例（提供给前端）     *     * @param userId 用户编号     * @param createReqVO 创建信息     * @return 实例的编号     */    @Transactional(rollbackFor = Exception.class)    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqVO createReqVO) {        // 获得流程定义        ProcessDefinition definition = processDefinitionService.getProcessDefinition(createReqVO.getProcessDefinitionId());        // 发起流程        return createProcessInstance0(userId, definition, createReqVO.getVariables(), null);    }    /**     * 创建流程实例（提供给内部）     *     * @param userId 用户编号     * @param createReqDTO 创建信息     * @return 实例的编号     */    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO createReqDTO) {        // 获得流程定义        ProcessDefinition definition = processDefinitionService.getActiveProcessDefinition(createReqDTO.getProcessDefinitionKey());        // 发起流程        return createProcessInstance0(userId, definition, createReqDTO.getVariables(), createReqDTO.getBusinessKey());    }    /**     * 获得流程实例 VO 信息     *     * @param id 流程实例的编号     * @return 流程实例     */    public BpmProcessInstanceRespVO getProcessInstanceVO(String id) {        // 获得流程实例        HistoricProcessInstance processInstance = getHistoricProcessInstance(id);        if (processInstance == null) {            return null;        }        BpmProcessInstanceExtDO processInstanceExt = this.baseMapper.selectByProcessInstanceId(id);        Assert.notNull(processInstanceExt, "流程实例拓展({}) 不存在", id);        // 获得流程定义        ProcessDefinition processDefinition = processDefinitionService                                                    .getProcessDefinition(processInstance.getProcessDefinitionId());        Assert.notNull(processDefinition, "流程定义({}) 不存在", processInstance.getProcessDefinitionId());        BpmProcessDefinitionExtDO processDefinitionExt = processDefinitionService.getProcessDefinitionExt(                processInstance.getProcessDefinitionId());        Assert.notNull(processDefinitionExt, "流程定义拓展({}) 不存在", id);        String bpmnXml = processDefinitionService.getProcessDefinitionBpmnXML(processInstance.getProcessDefinitionId());        // 获得 User        UserVO startUser = sysBaseApi.getUserById(NumberUtil.parseLong(processInstance.getStartUserId()));        // 拼接结果        return bpmProcessInstanceConvert.convert2(processInstance, processInstanceExt,                processDefinition, processDefinitionExt, bpmnXml, startUser, startUser.getDept());    }    /**     * 取消流程实例     *     * @param userId 用户编号     * @param cancelReqVO 取消信息     */    public void cancelProcessInstance(Long userId, @Valid BpmProcessInstanceCancelReqVO cancelReqVO) {        // 校验流程实例存在        ProcessInstance instance = getProcessInstance(cancelReqVO.getId());        if (instance == null) {            throw new BusinessException("流程取消失败，流程不处于运行中");        }        // 只能取消自己的        if (!Objects.equals(instance.getStartUserId(), String.valueOf(userId))) {            throw new BusinessException("流程取消失败，该流程不是你发起的");        }        // 通过删除流程实例，实现流程实例的取消,        // 删除流程实例，正则执行任务 ACT_RU_TASK. 任务会被删除。通过历史表查询        deleteProcessInstance(cancelReqVO.getId(),                BpmProcessInstanceDeleteReasonEnum.CANCEL_TASK.format(cancelReqVO.getReason()));    }    /**     * 获得历史的流程实例     *     * @param id 流程实例的编号     * @return 历史的流程实例     */    public HistoricProcessInstance getHistoricProcessInstance(String id) {        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();    }    /**     * 获得历史的流程实例列表     *     * @param ids 流程实例的编号集合     * @return 历史的流程实例列表     */    public List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids) {        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(ids).list();    }    /**     * 创建 ProcessInstance 拓展记录     *     * @param instance 流程任务     */    public void createProcessInstanceExt(ProcessInstance instance) {        // 获得流程定义        ProcessDefinition definition = processDefinitionService.getProcessDefinition2(instance.getProcessDefinitionId());        // 插入 BpmProcessInstanceExtDO 对象        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO();        instanceExtDO.setProcessInstanceId(instance.getId());        instanceExtDO.setProcessDefinitionId(definition.getId());        instanceExtDO.setName(instance.getProcessDefinitionName());        instanceExtDO.setStartUserId(Long.valueOf(instance.getStartUserId()));        instanceExtDO.setCategory(definition.getCategory());        instanceExtDO.setStatus(BpmProcessInstanceStatusEnum.RUNNING.getStatus());        instanceExtDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());        this.baseMapper.insert(instanceExtDO);    }    /**     * 更新 ProcessInstance 拓展记录为取消     *     * @param event 流程取消事件     */    public void updateProcessInstanceExtCancel(FlowableCancelledEvent event) {        // 判断是否为 Reject 不通过。如果是，则不进行更新.        // 因为，updateProcessInstanceExtReject 方法，已经进行更新了        if (BpmProcessInstanceDeleteReasonEnum.isRejectReason((String)event.getCause())) {            return;        }        // 需要主动查询，因为 instance 只有 id 属性        // 另外，此时如果去查询 ProcessInstance 的话，字段是不全的，所以去查询了 HistoricProcessInstance        HistoricProcessInstance processInstance = getHistoricProcessInstance(event.getProcessInstanceId());        // 更新拓展表        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO();        instanceExtDO.setProcessInstanceId(event.getProcessInstanceId());        instanceExtDO.setEndTime(new Timestamp(DateUtil.date().getTime())); // 由于 ProcessInstance 里没有办法拿到 endTime，所以这里设置        instanceExtDO.setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus());        instanceExtDO.setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());        this.baseMapper.updateByProcessInstanceId(instanceExtDO);        // 发送流程实例的状态事件        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(                bpmProcessInstanceConvert.convert(this, processInstance, instanceExtDO.getResult()));    }    /**     * 更新 ProcessInstance 拓展记录为完成     *     * @param instance 流程任务     */    public void updateProcessInstanceExtComplete(ProcessInstance instance) {        // 需要主动查询，因为 instance 只有 id 属性        // 另外，此时如果去查询 ProcessInstance 的话，字段是不全的，所以去查询了 HistoricProcessInstance        HistoricProcessInstance processInstance = getHistoricProcessInstance(instance.getId());        // 更新拓展表        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO();        instanceExtDO.setProcessInstanceId(instance.getProcessInstanceId());        instanceExtDO.setEndTime(new Timestamp(DateUtil.date().getTime())); // 由于 ProcessInstance 里没有办法拿到 endTime，所以这里设置        instanceExtDO.setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus());        instanceExtDO.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()); // 如果正常完全，说明审批通过        this.baseMapper.updateByProcessInstanceId(instanceExtDO);        // 发送流程被通过的消息        messageService.sendMessageWhenProcessInstanceApprove(bpmProcessInstanceConvert.convert2ApprovedReq(instance));        // 发送流程实例的状态事件        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(                bpmProcessInstanceConvert.convert(this, processInstance, instanceExtDO.getResult()));    }    /**     * 更新 ProcessInstance 拓展记录为不通过     *     * @param id 流程编号     * @param reason 理由。例如说，审批不通过时，需要传递该值     */    @Transactional(rollbackFor = Exception.class)    public void updateProcessInstanceExtReject(String id, String reason) {        // 需要主动查询，因为 instance 只有 id 属性        ProcessInstance processInstance = getProcessInstance(id);        // 删除流程实例，以实现驳回任务时，取消整个审批流程        deleteProcessInstance(id, StrUtil.format(BpmProcessInstanceDeleteReasonEnum.REJECT_TASK.format(reason)));        // 更新 status + result        // 注意，不能和上面的逻辑更换位置。因为 deleteProcessInstance 会触发流程的取消，进而调用 updateProcessInstanceExtCancel 方法，        // 设置 result 为 BpmProcessInstanceStatusEnum.CANCEL，显然和 result 不一定是一致的        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO();        instanceExtDO.setProcessInstanceId(id);        instanceExtDO.setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus());        instanceExtDO.setResult(BpmProcessInstanceResultEnum.REJECT.getResult());        this.baseMapper.updateByProcessInstanceId(instanceExtDO);        // 发送流程被不通过的消息        messageService.sendMessageWhenProcessInstanceReject(bpmProcessInstanceConvert.convert2RejectReq(processInstance, reason));        // 发送流程实例的状态事件        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(                bpmProcessInstanceConvert.convert(this, processInstance, instanceExtDO.getResult()));    }    private void deleteProcessInstance(String id, String reason) {        runtimeService.deleteProcessInstance(id, reason);    }    private String createProcessInstance0(Long userId, ProcessDefinition definition,                                          Map<String, Object> variables, String businessKey) {        // 校验流程定义        if (definition == null) {            throw new BusinessException("流程定义不存在");        }        if (definition.isSuspended()) {            throw new BusinessException("流程定义处于挂起状态");        }        // 创建流程实例        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId(), businessKey, variables);        // 设置流程名字        runtimeService.setProcessInstanceName(instance.getId(), definition.getName());        // 补全流程实例的拓展表        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = new BpmProcessInstanceExtDO();        bpmProcessInstanceExtDO.setProcessInstanceId(instance.getId());        bpmProcessInstanceExtDO.setFormVariables(variables);        this.baseMapper.updateByProcessInstanceId(bpmProcessInstanceExtDO);        return instance.getId();    }}