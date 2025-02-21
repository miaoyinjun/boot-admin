package org.jjche.sys.modules.logging.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.sys.modules.logging.domain.LogDO;
import org.jjche.sys.modules.logging.dto.LogErrorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>LogErrorMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-5-22
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogErrorMapStruct extends BaseVoMapStruct<LogDO, LogErrorDTO> {

}
