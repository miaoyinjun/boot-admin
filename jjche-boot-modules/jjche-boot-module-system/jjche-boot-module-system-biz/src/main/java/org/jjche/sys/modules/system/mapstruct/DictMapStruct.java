package org.jjche.sys.modules.system.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.sys.modules.system.dto.DictDTO;
import org.jjche.sys.modules.system.domain.DictDO;
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
