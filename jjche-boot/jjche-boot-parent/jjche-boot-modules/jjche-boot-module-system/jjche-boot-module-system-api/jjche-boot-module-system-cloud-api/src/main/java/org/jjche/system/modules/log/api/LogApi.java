package org.jjche.system.modules.log.api;

import org.jjche.common.api.CommonLogApi;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.system.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-05
 */
@Component
@FeignClient(contextId = ApiConstants.NAME + "-log-api",
        name = ApiConstants.NAME,
        path = "/sys/logs/",
        url = "${FEIGN_URL_SYSTEM:}"
)
public interface LogApi extends CommonLogApi {

    /**
     * <p>
     * 记录日志
     * </p>
     *
     * @param logRecord /
     */
    @PostMapping("record")
    void recordLog(@RequestBody LogRecordDTO logRecord);

    /**
     * <p>
     * 批量记录日志
     * </p>
     *
     * @param list /
     */
    @PostMapping("records")
    void recordLogs(@RequestBody List<LogRecordDTO> list);
}
