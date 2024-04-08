package org.jjche.bpm.modules.definition.api;

import org.jjche.bpm.modules.definition.api.dto.BpmProcessInstanceCreateReqDTO;

/**
 * <p>
 * 工作流 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
public interface BpmProcessInstanceApi {

    /**
     * 创建流程实例（提供给内部）
     *
     * @param userId 用户编号
     * @param reqDTO 创建信息
     * @return 实例的编号
     */
    String createProcessInstance(Long userId, BpmProcessInstanceCreateReqDTO reqDTO);
}
