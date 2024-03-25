package org.jjche.bpm.modules.definition.api;

import org.jjche.bpm.constant.BpmApiConstants;
import org.jjche.bpm.modules.definition.api.dto.BpmProcessInstanceCreateReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 用户 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
@FeignClient(contextId = BpmApiConstants.NAME + "-process-instance-api",
        name = BpmApiConstants.NAME,
        path = "/bpm/internal/process-instance",
        url = "${FEIGN_URL_BPM:}"
)
public interface IBpmProcessInstanceApi {

    /**
     * 创建流程实例（提供给内部）
     *
     * @param userId 用户编号
     * @param reqDTO 创建信息
     * @return 实例的编号
     */
    @PostMapping("/create")
    String createProcessInstance(@RequestParam Long userId, @RequestBody BpmProcessInstanceCreateReqDTO reqDTO);
}
