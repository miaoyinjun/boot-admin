package org.jjche.system.modules.system.mapstruct;

import org.jjche.system.modules.system.api.dto.JobDTO;
import org.jjche.system.modules.system.domain.JobDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

/**
 * <p>JobMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-29
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobMapStruct {
    List<JobDTO> toVO(Collection<JobDO> dooList);
    JobDTO toVO(JobDO doo);
    JobDO toDO(JobDTO dto);
}
