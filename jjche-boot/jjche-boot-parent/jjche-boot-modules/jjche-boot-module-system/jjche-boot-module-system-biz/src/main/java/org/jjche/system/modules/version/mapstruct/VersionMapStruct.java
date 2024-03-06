package org.jjche.system.modules.version.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.system.modules.version.domain.VersionDO;
import org.jjche.system.modules.version.api.dto.VersionDTO;
import org.jjche.system.modules.version.api.vo.VersionVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 版本 转换类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VersionMapStruct extends BaseVoMapStruct<VersionDO, VersionVO> {
    /**
     * <p>
     * DTO集合转DO集合
     * </p>
     *
     * @param dto dto
     * @return DO
     */
    VersionDO toDO(VersionDTO dto);
}
