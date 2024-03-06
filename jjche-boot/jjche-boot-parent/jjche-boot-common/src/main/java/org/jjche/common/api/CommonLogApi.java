package org.jjche.common.api;

import org.jjche.common.dto.LogRecordDTO;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-05
 */
public interface CommonLogApi {
    /**
     * <p>
     * 记录日志
     * </p>
     *
     * @param logRecord /
     */
    void recordLog(LogRecordDTO logRecord);
}
