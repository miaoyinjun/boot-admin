package org.jjche.bpm.modules.definition.mapstruct;

import cn.hutool.core.collection.CollUtil;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.jjche.bpm.modules.definition.dto.BpmProcessDefinitionCreateReqDTO;
import org.jjche.bpm.modules.form.domain.BpmFormDO;
import org.jjche.bpm.modules.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import org.jjche.bpm.modules.definition.domain.BpmProcessDefinitionExtDO;
import org.jjche.bpm.modules.definition.vo.process.BpmProcessDefinitionRespVO;
import org.mapstruct.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Bpm 流程定义的 Convert
 *
 * @author yunlong.li
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmProcessDefinitionConvert {
    BpmProcessDefinitionPageItemRespVO convert(ProcessDefinition bean);

    BpmProcessDefinitionExtDO convert2(BpmProcessDefinitionCreateReqDTO bean);

    default List<BpmProcessDefinitionPageItemRespVO> convertList(List<ProcessDefinition> list, Map<String, Deployment> deploymentMap,
                                                                 Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap, Map<Long, BpmFormDO> formMap) {
        List<BpmProcessDefinitionPageItemRespVO> result = CollUtil.newArrayList();
        for (ProcessDefinition definition : list) {
            Deployment deployment = definition.getDeploymentId() != null ? deploymentMap.get(definition.getDeploymentId()) : null;
            BpmProcessDefinitionExtDO definitionDO = processDefinitionDOMap.get(definition.getId());
            BpmFormDO form = definitionDO != null ? formMap.get(definitionDO.getFormId()) : null;
            result.add(convert(definition, deployment, definitionDO, form));
        }
        return result;
    }

    default List<BpmProcessDefinitionRespVO> convertList3(List<ProcessDefinition> list,
                                                          Map<String, BpmProcessDefinitionExtDO> processDefinitionDOMap) {
        List<BpmProcessDefinitionRespVO> result = CollUtil.newArrayList();
        for (ProcessDefinition processDefinition : list) {
            BpmProcessDefinitionRespVO respVO = convert3(processDefinition);
            BpmProcessDefinitionExtDO processDefinitionExtDO = processDefinitionDOMap.get(processDefinition.getId());
            // 复制通用属性
            copyTo(processDefinitionExtDO, respVO);
            result.add(respVO);
        }
        return result;
    }

    @Mapping(source = "suspended", target = "suspensionState", qualifiedByName = "convertSuspendedToSuspensionState")
    BpmProcessDefinitionRespVO convert3(ProcessDefinition bean);

    @Named("convertSuspendedToSuspensionState")
    default Integer convertSuspendedToSuspensionState(boolean suspended) {
        return suspended ? SuspensionState.SUSPENDED.getStateCode() :
                SuspensionState.ACTIVE.getStateCode();
    }

    default BpmProcessDefinitionPageItemRespVO convert(ProcessDefinition bean, Deployment deployment,
                                                       BpmProcessDefinitionExtDO processDefinitionExtDO, BpmFormDO form) {
        BpmProcessDefinitionPageItemRespVO respVO = convert(bean);
        respVO.setSuspensionState(bean.isSuspended() ? SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
        if (deployment != null) {
            respVO.setDeploymentTime(new Timestamp(deployment.getDeploymentTime().getTime()));
        }
        if (form != null) {
            respVO.setFormName(form.getName());
        }
        // 复制通用属性
        copyTo(processDefinitionExtDO, respVO);
        return respVO;
    }

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionExtDO from, @MappingTarget BpmProcessDefinitionRespVO to);
}
