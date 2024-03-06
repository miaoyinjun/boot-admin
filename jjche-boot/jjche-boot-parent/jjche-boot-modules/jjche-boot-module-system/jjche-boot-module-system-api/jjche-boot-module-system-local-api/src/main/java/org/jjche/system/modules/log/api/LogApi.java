package org.jjche.system.modules.log.api;

import org.jjche.common.api.CommonLogApi;
import org.jjche.common.dto.LogRecordDTO;

import java.util.List;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-05
 */
public interface LogApi extends CommonLogApi {

    /**
     * <p>
     * 批量记录日志
     * </p>
     *
     * @param list /
     */
    void recordLogs(List<LogRecordDTO> list);
}
