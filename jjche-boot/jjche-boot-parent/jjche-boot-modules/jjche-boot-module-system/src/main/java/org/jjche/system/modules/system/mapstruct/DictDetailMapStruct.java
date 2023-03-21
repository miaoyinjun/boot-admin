package org.jjche.system.modules.system.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.system.modules.system.api.dto.DictDetailDTO;
import org.jjche.system.modules.system.domain.DictDetailDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DictDetailMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Mapper(componentModel = "spring", uses = {DictSmallMapStruct.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictDetailMapStruct extends BaseVoMapStruct<DictDetailDO, DictDetailDTO> {

}
