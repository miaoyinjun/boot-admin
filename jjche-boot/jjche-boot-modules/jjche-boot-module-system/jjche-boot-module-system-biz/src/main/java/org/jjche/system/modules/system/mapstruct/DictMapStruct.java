package org.jjche.system.modules.system.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.system.modules.system.api.dto.DictDTO;
import org.jjche.system.modules.system.domain.DictDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DictMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictMapStruct extends BaseVoMapStruct<DictDO, DictDTO> {

}
