package org.jjche.system.modules.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.common.engine.impl.util.io.BytesStreamSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.system.modules.bpm.convert.definition.BpmModelConvert;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmFormDO;
import org.jjche.system.modules.bpm.enums.definition.BpmModelFormTypeEnum;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.model.*;
import org.jjche.system.modules.bpm.service.definition.dto.BpmModelMetaInfoRespDTO;
import org.jjche.system.modules.bpm.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Flowable流程模型实现
 * 主要进行 Flowable {@link Model} 的维护
 *
 * @author yunlongn
 * @author 芋道源码
 * @author jason
 */
@Service
@Validated
@Slf4j
public class BpmModelService {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmFormService bpmFormService;
    @Resource
    private BpmTaskAssignRuleService taskAssignRuleService;

    private static final Pattern PATTERN_XML_NCNAME = Pattern.compile("[a-zA-Z_][\\-_.0-9_a-zA-Z$]*");

    /**
     * 获得流程模型分页
     *
     * @param pageVO 分页查询
     * @return 流程模型分页
     */
    public MyPage<BpmModelPageItemRespVO> getModelPage(PageParam page,
                                                       BpmModelPageReqVO pageVO) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        String key = pageVO.getKey();
        String name = pageVO.getName();
        String category = pageVO.getCategory();
        if (StrUtil.isNotBlank(key)) {
            modelQuery.modelKey(key);
        }
        if (StrUtil.isNotBlank(name)) {
            // 模糊匹配
            modelQuery.modelNameLike("%" + name + "%");
        }
        if (StrUtil.isNotBlank(category)) {
            modelQuery.modelCategory(category);
        }
        // 执行查询
        List<Model> models = modelQuery.orderByCreateTime().desc().listPage((int) page.getPageIndex() - 1, (int) page.getPageSize());

        // 获得 Form Map
        List<String> metaInfos = models.stream().map(Model::getMetaInfo).collect(Collectors.toList());

        List<BpmModelMetaInfoRespDTO> bpmModelMetaInfoRespDTOS = CollUtil.newArrayList();
        for (String metaInfo : metaInfos) {
            BpmModelMetaInfoRespDTO dto = JSONUtil.toBean(metaInfo, BpmModelMetaInfoRespDTO.class);
            bpmModelMetaInfoRespDTOS.add(dto);
        }
        bpmModelMetaInfoRespDTOS = CollUtil.removeNull(bpmModelMetaInfoRespDTOS);
        Set<Long> formIds = bpmModelMetaInfoRespDTOS.stream().map(BpmModelMetaInfoRespDTO::getFormId).collect(Collectors.toSet());
        Map<Long, BpmFormDO> formMap = bpmFormService.getFormMap(formIds);

        // 获得 Deployment Map
        Set<String> deploymentIds = new HashSet<>();
        models.forEach(model -> {
            String deploymentId = model.getDeploymentId();
            if (StrUtil.isNotBlank(deploymentId)) {
                deploymentIds.add(deploymentId);
            }
        });
        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(deploymentIds);
        // 获得 ProcessDefinition Map
        List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessDefinitionListByDeploymentIds(deploymentIds);
        Map<String, ProcessDefinition> processDefinitionMap = MapUtil.newHashMap();
        processDefinitionMap = CollUtil.toMap(processDefinitions, processDefinitionMap, ProcessDefinition::getDeploymentId);

        // 拼接结果
        long modelCount = modelQuery.count();
        MyPage myPage = new MyPage();
        myPage.setRecords(BpmModelConvert.INSTANCE.convertList(models, formMap, deploymentMap, processDefinitionMap));
        myPage.setTotal(modelCount);
        return myPage;
    }

    /**
     * 创建流程模型
     *
     * @param createReqVO 创建信息
     * @param bpmnXml BPMN XML
     * @return 创建的流程模型的编号
     */
    @Transactional(rollbackFor = Exception.class)
    public String createModel(@Valid BpmModelCreateReqVO createReqVO, String bpmnXml) {
        checkKeyNCName(createReqVO.getKey());
        // 校验流程标识已经存在
        Model keyModel = getModelByKey(createReqVO.getKey());
        String msg = StrUtil.format("已经存在流程标识为【{}】的流程", createReqVO.getKey());
        Assert.isNull(keyModel, msg);

        // 创建流程定义
        Model model = repositoryService.newModel();
        BpmModelConvert.INSTANCE.copy(model, createReqVO);
        // 保存流程定义
        repositoryService.saveModel(model);
        // 保存 BPMN XML
        saveModelBpmnXml(model, bpmnXml);
        return model.getId();
    }

    private Model getModelByKey(String key) {
        return repositoryService.createModelQuery().modelKey(key).singleResult();
    }

    /**
     * 获得流程模块
     *
     * @param id 编号
     * @return 流程模型
     */
    public BpmModelRespVO getModel(String id) {
        Model model = repositoryService.getModel(id);
        if (model == null) {
            return null;
        }
        BpmModelRespVO modelRespVO = BpmModelConvert.INSTANCE.convert(model);
        // 拼接 bpmn XML
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        modelRespVO.setBpmnXml(StrUtil.utf8Str(bpmnBytes));
        return modelRespVO;
    }

    /**
     * 修改流程模型
     *
     * @param updateReqVO 更新信息
     */
    @Transactional(rollbackFor = Exception.class) // 因为进行多个操作，所以开启事务
    public void updateModel(@Valid BpmModelUpdateReqVO updateReqVO) {
        // 校验流程模型存在
        Model model = repositoryService.getModel(updateReqVO.getId());
        if (model == null) {
            throw new BusinessException("流程模型不存在");
        }

        // 修改流程定义
        BpmModelConvert.INSTANCE.copy(model, updateReqVO);
        // 更新模型
        repositoryService.saveModel(model);
        // 更新 BPMN XML
        saveModelBpmnXml(model, updateReqVO.getBpmnXml());
    }

    /**
     * 将流程模型，部署成一个流程定义
     *
     * @param id 编号
     */
    @Transactional(rollbackFor = Exception.class) // 因为进行多个操作，所以开启事务
    public void deployModel(String id) {
        // 1.1 校验流程模型存在
        Model model = repositoryService.getModel(id);
        if (ObjectUtils.isEmpty(model)) {
            throw new BusinessException("流程模型不存在");
        }
        // 1.2 校验流程图
        // TODO 芋艿：校验流程图的有效性；例如说，是否有开始的元素，是否有结束的元素；
        byte[] bpmnBytes = repositoryService.getModelEditorSource(model.getId());
        if (bpmnBytes == null) {
            throw new BusinessException("流程模型不存在");
        }
        // 1.3 校验表单已配
        BpmFormDO form = checkFormConfig(model.getMetaInfo());
        // 1.4 校验任务分配规则已配置
        taskAssignRuleService.checkTaskAssignRuleAllConfig(id);

        // 1.5 校验模型是否发生修改。如果未修改，则不允许创建
        BpmProcessDefinitionCreateReqDTO definitionCreateReqDTO = BpmModelConvert.INSTANCE.convert2(model, form);
        definitionCreateReqDTO.setBpmnBytes(bpmnBytes);
        if (processDefinitionService.isProcessDefinitionEquals(definitionCreateReqDTO)) { // 流程定义的信息相等
            ProcessDefinition oldProcessDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(model.getDeploymentId());
            if (oldProcessDefinition != null && taskAssignRuleService.isTaskAssignRulesEquals(model.getId(), oldProcessDefinition.getId())) {
                throw new BusinessException("流程定义部署失败，原因：信息未发生变化");
            }
        }

        // 2.1 创建流程定义
        String definitionId = processDefinitionService.createProcessDefinition(definitionCreateReqDTO);

        // 2.2 将老的流程定义进行挂起。也就是说，只有最新部署的流程定义，才可以发起任务。
        updateProcessDefinitionSuspended(model.getDeploymentId());

        // 2.3 更新 model 的 deploymentId，进行关联
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(definitionId);
        model.setDeploymentId(definition.getDeploymentId());
        repositoryService.saveModel(model);

        // 2.4 复制任务分配规则
        taskAssignRuleService.copyTaskAssignRules(id, definition.getId());
    }


    /**
     * 删除模型
     *
     * @param ids 编号
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(List<String> ids) {
        // 校验流程模型存在
        String id = CollUtil.getFirst(ids);
        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw new BusinessException("流程模型不存在");
        }
        // 执行删除
        repositoryService.deleteModel(id);
        // 禁用流程实例
        updateProcessDefinitionSuspended(model.getDeploymentId());
    }

    /**
     * 修改模型的状态，实际更新的部署的流程定义的状态
     *
     * @param id    编号
     * @param state 状态
     */
    public void updateModelState(String id, Integer state) {
        // 校验流程模型存在
        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw new BusinessException("流程模型不存在");
        }
        // 校验流程定义存在
        ProcessDefinition definition = processDefinitionService.getProcessDefinitionByDeploymentId(model.getDeploymentId());
        if (definition == null) {
            throw new BusinessException("流程定义不存在");
        }

        // 更新状态
        processDefinitionService.updateProcessDefinitionState(definition.getId(), state);
    }

    /**
     * 获得流程模型编号对应的 BPMN Model
     *
     * @param id 流程模型编号
     * @return BPMN Model
     */
    public BpmnModel getBpmnModel(String id) {
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        if (ArrayUtil.isEmpty(bpmnBytes)) {
            return null;
        }
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToBpmnModel(new BytesStreamSource(bpmnBytes), true, true);
    }

    private void checkKeyNCName(String key) {
        if (!(StringUtils.hasText(key)
                && PATTERN_XML_NCNAME.matcher(key).matches())) {
            throw new BusinessException("流程标识格式不正确，需要以字母或下划线开头，后接任意字母、数字、中划线、下划线、句点！");
        }
    }

    /**
     * 校验流程表单已配置
     *
     * @param metaInfoStr 流程模型 metaInfo 字段
     * @return 流程表单
     */
    private BpmFormDO checkFormConfig(String metaInfoStr) {
        BpmModelMetaInfoRespDTO metaInfo = JSONUtil.toBean(metaInfoStr, BpmModelMetaInfoRespDTO.class);
        if (metaInfo == null || metaInfo.getFormType() == null) {
            throw new BusinessException("部署流程失败，原因：流程表单未配置，请点击【修改流程】按钮进行配置");
        }
        // 校验表单存在
        if (Objects.equals(metaInfo.getFormType(), BpmModelFormTypeEnum.NORMAL.getType())) {
            BpmFormDO form = bpmFormService.getForm(metaInfo.getFormId());
            if (form == null) {
                throw new BusinessException("动态表单不存在");
            }
            return form;
        }
        return null;
    }

    private void saveModelBpmnXml(Model model, String bpmnXml) {
        if (StrUtil.isEmpty(bpmnXml)) {
            return;
        }
        repositoryService.addModelEditorSource(model.getId(), StrUtil.utf8Bytes(bpmnXml));
    }

    /**
     * 挂起 deploymentId 对应的流程定义。 这里一个deploymentId 只关联一个流程定义
     *
     * @param deploymentId 流程发布Id.
     */
    private void updateProcessDefinitionSuspended(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return;
        }
        ProcessDefinition oldDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(deploymentId);
        if (oldDefinition == null) {
            return;
        }
        processDefinitionService.updateProcessDefinitionState(oldDefinition.getId(), SuspensionState.SUSPENDED.getStateCode());
    }


}
