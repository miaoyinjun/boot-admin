package org.jjche.sys.modules.logging.mapstruct;

import org.jjche.common.dto.LogRecordDTO;
import org.jjche.sys.modules.logging.domain.LogDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * <p>
 * LogRecordDTO, log映射
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogRecordMapStruct {

    @Mappings({
            @Mapping(source = "operator", target = "username"),
            @Mapping(source = "value", target = "description"),
            @Mapping(source = "type", target = "logType"),
            @Mapping(source = "success", target = "isSuccess"),
    })
    /**
     * <p>
     * LogRecord转换Log
     * </p>
     *
     * @param logRecord 日志记录
     * @return LogDO
     */
    LogDO toLog(LogRecordDTO logRecord);

    @Mappings({
            @Mapping(source = "operator", target = "username"),
            @Mapping(source = "value", target = "description"),
            @Mapping(source = "type", target = "logType"),
            @Mapping(source = "success", target = "isSuccess"),
    })
    /**
     * <p>
     * LogRecord转换Log
     * </p>
     *
     * @param logRecord 日志记录
     * @return LogDO
     */
    List<LogDO> toLog(List<LogRecordDTO> logRecord);
}
