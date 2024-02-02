package org.jjche.bpm.modules.definition.mapstruct;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.jjche.bpm.modules.definition.api.dto.BpmModelMetaInfoRespDTO;
import org.jjche.bpm.modules.definition.api.dto.BpmProcessDefinitionCreateReqDTO;
import org.jjche.bpm.modules.definition.api.vo.model.*;
import org.jjche.bpm.modules.form.domain.BpmFormDO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 流程模型 Convert
 *
 * @author yunlongn
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmModelConvert {
    default List<BpmModelPageItemRespVO> convertList(List<Model> list, Map<Long, BpmFormDO> formMap,
                                                     Map<String, Deployment> deploymentMap,
                                                     Map<String, ProcessDefinition> processDefinitionMap) {
        List<BpmModelPageItemRespVO> result = CollUtil.newArrayList();
        for (Model model : list) {
            BpmModelMetaInfoRespDTO metaInfo = JSONUtil.toBean(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class);
            BpmFormDO form = metaInfo != null ? formMap.get(metaInfo.getFormId()) : null;
            Deployment deployment = model.getDeploymentId() != null ? deploymentMap.get(model.getDeploymentId()) : null;
            ProcessDefinition processDefinition = model.getDeploymentId() != null ? processDefinitionMap.get(model.getDeploymentId()) : null;
            result.add(convert(model, form, deployment, processDefinition));
        }
        return result;
    }

    default BpmModelPageItemRespVO convert(Model model, BpmFormDO form, Deployment deployment, ProcessDefinition processDefinition) {
        BpmModelPageItemRespVO modelRespVO = new BpmModelPageItemRespVO();
        modelRespVO.setId(model.getId());
        modelRespVO.setCreateTime(new Timestamp(model.getCreateTime().getTime()));
        // 通用 copy
        copyTo(model, modelRespVO);
        // Form
        if (form != null) {
            modelRespVO.setFormId(form.getId());
            modelRespVO.setFormName(form.getName());
        }
        // ProcessDefinition
        modelRespVO.setProcessDefinition(this.convert(processDefinition));
        if (modelRespVO.getProcessDefinition() != null) {
            modelRespVO.getProcessDefinition().setSuspensionState(processDefinition.isSuspended() ?
                    SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
            modelRespVO.getProcessDefinition().setDeploymentTime(new Timestamp(deployment.getDeploymentTime().getTime()));
        }
        return modelRespVO;
    }

    default BpmModelRespVO convert(Model model) {
        BpmModelRespVO modelRespVO = new BpmModelRespVO();
        modelRespVO.setId(model.getId());
        modelRespVO.setCreateTime(new Timestamp(model.getCreateTime().getTime()));
        // 通用 copy
        copyTo(model, modelRespVO);
        return modelRespVO;
    }

    default void copyTo(Model model, BpmModelBaseVO to) {
        to.setName(model.getName());
        to.setKey(model.getKey());
        to.setCategory(model.getCategory());
        // metaInfo
        BpmModelMetaInfoRespDTO metaInfo = JSONUtil.toBean(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class);
        copyTo(metaInfo, to);
    }

    BpmModelCreateReqVO convert(BpmModeImportReqVO bean);

    default BpmProcessDefinitionCreateReqDTO convert2(Model model, BpmFormDO form) {
        BpmProcessDefinitionCreateReqDTO createReqDTO = new BpmProcessDefinitionCreateReqDTO();
        createReqDTO.setModelId(model.getId());
        createReqDTO.setName(model.getName());
        createReqDTO.setKey(model.getKey());
        createReqDTO.setCategory(model.getCategory());
        BpmModelMetaInfoRespDTO metaInfo = JSONUtil.toBean(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class);
        // metaInfo
        copyTo(metaInfo, createReqDTO);
        // form
        if (form != null) {
            createReqDTO.setFormConf(form.getContent());
//            createReqDTO.setFormFields();
        }
        return createReqDTO;
    }

    void copyTo(BpmModelMetaInfoRespDTO from, @MappingTarget BpmProcessDefinitionCreateReqDTO to);

    void copyTo(BpmModelMetaInfoRespDTO from, @MappingTarget BpmModelBaseVO to);

    BpmModelPageItemRespVO.ProcessDefinition convert(ProcessDefinition bean);

    default void copy(Model model, BpmModelCreateReqVO bean) {
        model.setName(bean.getName());
        model.setKey(bean.getKey());
        model.setMetaInfo(buildMetaInfoStr(null, bean.getDescription(), null, null,
                null, null));
    }

    default void copy(Model model, BpmModelUpdateReqVO bean) {
        model.setName(bean.getName());
        model.setCategory(bean.getCategory());
        model.setMetaInfo(buildMetaInfoStr(JSONUtil.toBean(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class),
                bean.getDescription(), bean.getFormType(), bean.getFormId(),
                bean.getFormCustomCreatePath(), bean.getFormCustomViewPath()));
    }

    default String buildMetaInfoStr(BpmModelMetaInfoRespDTO metaInfo, String description, Integer formType,
                                    Long formId, String formCustomCreatePath, String formCustomViewPath) {
        if (metaInfo == null) {
            metaInfo = new BpmModelMetaInfoRespDTO();
        }
        // 只有非空，才进行设置，避免更新时的覆盖
        if (StrUtil.isNotEmpty(description)) {
            metaInfo.setDescription(description);
        }
        if (Objects.nonNull(formType)) {
            metaInfo.setFormType(formType);
            metaInfo.setFormId(formId);
            metaInfo.setFormCustomCreatePath(formCustomCreatePath);
            metaInfo.setFormCustomViewPath(formCustomViewPath);
        }
        JSONConfig jsonConfig = JSONConfig.create();
        jsonConfig.setIgnoreNullValue(false);
        return JSONUtil.toJsonStr(metaInfo, jsonConfig);
    }
}
