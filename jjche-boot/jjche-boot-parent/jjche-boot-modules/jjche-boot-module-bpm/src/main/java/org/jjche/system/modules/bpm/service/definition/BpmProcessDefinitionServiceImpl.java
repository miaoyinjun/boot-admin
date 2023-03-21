package org.jjche.system.modules.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.common.engine.impl.util.io.BytesStreamSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.process.BpmProcessDefinitionListReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.process.BpmProcessDefinitionPageReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import org.jjche.system.modules.bpm.convert.definition.BpmProcessDefinitionConvert;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmFormDO;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import org.jjche.system.modules.bpm.mapper.BpmProcessDefinitionExtMapper;
import org.jjche.system.modules.bpm.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.jjche.flowable.util.FlowableUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * 流程定义实现
 * 主要进行 Flowable {@link ProcessDefinition} 和 {@link Deployment} 的维护
 *
 * @author yunlongn
 * @author ZJQ
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BpmProcessDefinitionServiceImpl implements BpmProcessDefinitionService {

    private static final String BPMN_FILE_SUFFIX = ".bpmn";

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private BpmProcessDefinitionExtMapper processDefinitionMapper;

    @Resource
    private BpmFormService formService;

    @Override
    public ProcessDefinition getProcessDefinition(String id) {
        return repositoryService.getProcessDefinition(id);
    }

    @Override
    public ProcessDefinition getProcessDefinition2(String id) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
    }

    @Override
    public ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return null;
        }
        return repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitionListByDeploymentIds(Set<String> deploymentIds) {
        if (CollUtil.isEmpty(deploymentIds)) {
            return emptyList();
        }
        return repositoryService.createProcessDefinitionQuery().deploymentIds(deploymentIds).list();
    }

    @Override
    public ProcessDefinition getActiveProcessDefinition(String key) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).active().singleResult();
    }

    @Override
    public List<Deployment> getDeployments(Set<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return emptyList();
        }
        List<Deployment> list = new ArrayList<>(ids.size());
        for (String id : ids) {
            Deployment deployment = getDeployment(id);
            if (deployment != null) {
                list.add(deployment);
            }
        }
        return list;
    }

    @Override
    public Deployment getDeployment(String id) {
        if (StrUtil.isEmpty(id)) {
            return null;
        }
        return repositoryService.createDeploymentQuery().deploymentId(id).singleResult();
    }

    @Override
    public BpmnModel getBpmnModel(String processDefinitionId) {
        return repositoryService.getBpmnModel(processDefinitionId);
    }

    @Override
    public String createProcessDefinition(@Valid BpmProcessDefinitionCreateReqDTO createReqDTO) {
        // 创建 Deployment 部署
        Deployment deploy = repositoryService.createDeployment().key(createReqDTO.getKey()).name(createReqDTO.getName()).category(createReqDTO.getCategory()).addBytes(createReqDTO.getKey() + BPMN_FILE_SUFFIX, createReqDTO.getBpmnBytes()).deploy();

        // 设置 ProcessDefinition 的 category 分类
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        repositoryService.setProcessDefinitionCategory(definition.getId(), createReqDTO.getCategory());
        // 注意 1，ProcessDefinition 的 key 和 name 是通过 BPMN 中的 <bpmn2:process /> 的 id 和 name 决定
        // 注意 2，目前该项目的设计上，需要保证 Model、Deployment、ProcessDefinition 使用相同的 key，保证关联性。
        //          否则，会导致 ProcessDefinition 的分页无法查询到。
        if (!Objects.equals(definition.getKey(), createReqDTO.getKey())) {
            String msg = StrUtil.format("流程定义的标识期望是({})，当前是({})，请修改 BPMN 流程图", createReqDTO.getKey(), definition.getKey());
            throw new BusinessException(msg);
        }
        if (!Objects.equals(definition.getName(), createReqDTO.getName())) {
            String msg = StrUtil.format("流程定义的名字期望是({})，当前是({})，请修改 BPMN 流程图", createReqDTO.getName(), definition.getName());
            throw new BusinessException(msg);
        }

        // 插入拓展表
        BpmProcessDefinitionExtDO definitionDO = BpmProcessDefinitionConvert.INSTANCE.convert2(createReqDTO);
        definitionDO.setProcessDefinitionId(definition.getId());
        processDefinitionMapper.insert(definitionDO);
        return definition.getId();
    }

    @Override
    public void updateProcessDefinitionState(String id, Integer state) {
        // 激活
        if (Objects.equals(SuspensionState.ACTIVE.getStateCode(), state)) {
            repositoryService.activateProcessDefinitionById(id, false, null);
            return;
        }
        // 挂起
        if (Objects.equals(SuspensionState.SUSPENDED.getStateCode(), state)) {
            // suspendProcessInstances = false，进行中的任务，不进行挂起。
            // 原因：只要新的流程不允许发起即可，老流程继续可以执行。
            repositoryService.suspendProcessDefinitionById(id, false, null);
            return;
        }
        log.error("[updateProcessDefinitionState][流程定义({}) 修改未知状态({})]", id, state);
    }

    @Override
    public String getProcessDefinitionBpmnXML(String id) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(id);
        if (bpmnModel == null) {
            return null;
        }
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return StrUtil.utf8Str(converter.convertToXML(bpmnModel));
    }

    @Override
    public boolean isProcessDefinitionEquals(@Valid BpmProcessDefinitionCreateReqDTO createReqDTO) {
        // 校验 name、description 是否更新
        ProcessDefinition oldProcessDefinition = getActiveProcessDefinition(createReqDTO.getKey());
        if (oldProcessDefinition == null) {
            return false;
        }
        BpmProcessDefinitionExtDO oldProcessDefinitionExt = getProcessDefinitionExt(oldProcessDefinition.getId());
        if (!StrUtil.equals(createReqDTO.getName(), oldProcessDefinition.getName()) || !StrUtil.equals(createReqDTO.getDescription(), oldProcessDefinitionExt.getDescription()) || !StrUtil.equals(createReqDTO.getCategory(), oldProcessDefinition.getCategory())) {
            return false;
        }
        // 校验 form 信息是否更新
        if (!ObjectUtil.equal(createReqDTO.getFormType(), oldProcessDefinitionExt.getFormType()) || !ObjectUtil.equal(createReqDTO.getFormId(), oldProcessDefinitionExt.getFormId()) || !ObjectUtil.equal(createReqDTO.getFormConf(), oldProcessDefinitionExt.getFormConf()) || !ObjectUtil.equal(createReqDTO.getFormFields(), oldProcessDefinitionExt.getFormFields()) || !ObjectUtil.equal(createReqDTO.getFormCustomCreatePath(), oldProcessDefinitionExt.getFormCustomCreatePath()) || !ObjectUtil.equal(createReqDTO.getFormCustomViewPath(), oldProcessDefinitionExt.getFormCustomViewPath())) {
            return false;
        }
        // 校验 BPMN XML 信息
        BpmnModel newModel = buildBpmnModel(createReqDTO.getBpmnBytes());
        BpmnModel oldModel = getBpmnModel(oldProcessDefinition.getId());
        // 对比字节变化
        if (!FlowableUtils.equals(oldModel, newModel)) {
            return false;
        }
        // 最终发现都一致，则返回 true
        return true;
    }

    /**
     * 构建对应的 BPMN Model
     *
     * @param bpmnBytes 原始的 BPMN XML 字节数组
     * @return BPMN Model
     */
    private BpmnModel buildBpmnModel(byte[] bpmnBytes) {
        // 转换成 BpmnModel 对象
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToBpmnModel(new BytesStreamSource(bpmnBytes), true, true);
    }

    @Override
    public BpmProcessDefinitionExtDO getProcessDefinitionExt(String id) {
        return processDefinitionMapper.selectByProcessDefinitionId(id);
    }

    @Override
    public List<BpmProcessDefinitionRespVO> getProcessDefinitionList(BpmProcessDefinitionListReqVO listReqVO) {
        // 拼接查询条件
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        if (Objects.equals(SuspensionState.SUSPENDED.getStateCode(), listReqVO.getSuspensionState())) {
            definitionQuery.suspended();
        } else if (Objects.equals(SuspensionState.ACTIVE.getStateCode(), listReqVO.getSuspensionState())) {
            definitionQuery.active();
        }
        // 执行查询
        List<ProcessDefinition> processDefinitions = definitionQuery.list();
        if (CollUtil.isEmpty(processDefinitions)) {
            return Collections.emptyList();
        }

        // 获得 BpmProcessDefinitionDO Map
        List<String> processDefinitionIds = processDefinitions.stream().map(ProcessDefinition::getId).collect(Collectors.toList());
        List<BpmProcessDefinitionExtDO> processDefinitionDOs = processDefinitionMapper.selectListByProcessDefinitionIds(processDefinitionIds);
        Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap = MapUtil.newHashMap();
        processDefinitionDOMap = CollUtil.toMap(processDefinitionDOs, processDefinitionDOMap, BpmProcessDefinitionExtDO::getProcessDefinitionId);
        // 执行查询，并返回
        return BpmProcessDefinitionConvert.INSTANCE.convertList3(processDefinitions, processDefinitionDOMap);
    }

    @Override
    public MyPage<BpmProcessDefinitionPageItemRespVO> getProcessDefinitionPage(BpmProcessDefinitionPageReqVO pageVO) {
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        if (StrUtil.isNotBlank(pageVO.getKey())) {
            definitionQuery.processDefinitionKey(pageVO.getKey());
        }

        // 执行查询
        List<ProcessDefinition> processDefinitions = definitionQuery.orderByProcessDefinitionVersion().desc().listPage((int) pageVO.getPageIndex(), (int) pageVO.getPageSize());

        if (CollUtil.isEmpty(processDefinitions)) {
            MyPage myPage = new MyPage();
            myPage.setNewRecords(emptyList());
            myPage.setTotal(definitionQuery.count());
            return myPage;
        }
        // 获得 Deployment Map
        Set<String> deploymentIds = new HashSet<>();
        processDefinitions.forEach(definition -> {
            String deploymentId = definition.getDeploymentId();
            if (StrUtil.isNotBlank(deploymentId)) {
                deploymentIds.add(deploymentId);
            }
        });
        Map<String, Deployment> deploymentMap = getDeploymentMap(deploymentIds);

        // 获得 BpmProcessDefinitionDO Map
        List<String> processDefinitionIds = processDefinitions.stream().map(ProcessDefinition::getId).collect(Collectors.toList());
        List<BpmProcessDefinitionExtDO> processDefinitionDOs = processDefinitionMapper
                .selectListByProcessDefinitionIds(processDefinitionIds);
        Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap = MapUtil.newHashMap();
        processDefinitionDOMap = CollUtil.toMap(processDefinitionDOs,
                processDefinitionDOMap, BpmProcessDefinitionExtDO::getProcessDefinitionId);

        // 获得 Form Map
        Set<Long> formIds = processDefinitionDOs.stream().map(BpmProcessDefinitionExtDO::getFormId).collect(Collectors.toSet());
        Map<Long, BpmFormDO> formMap = formService.getFormMap(formIds);

        // 拼接结果
        long definitionCount = definitionQuery.count();
        MyPage myPage = new MyPage();
        myPage.setNewRecords(BpmProcessDefinitionConvert.INSTANCE.convertList(processDefinitions, deploymentMap, processDefinitionDOMap, formMap));
        myPage.setTotal(definitionCount);
        return myPage;
    }

}
