package org.jjche.sys.modules.system.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.sys.modules.system.api.dto.MenuDTO;
import org.jjche.sys.modules.system.domain.MenuDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

/**
 * <p>MenuMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-17
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapStruct extends BaseVoMapStruct<MenuDO, MenuDTO> {
    /**
     * <p>
     * DTO集合转DO集合
     * </p>
     *
     * @param dtoList dtoList
     * @return DOList
     */
    List<MenuDO> toDO(Collection<MenuDTO> dtoList);
}
