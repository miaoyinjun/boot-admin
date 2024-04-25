package org.jjche.sys.modules.system.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DeptSmallMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-25
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptSmallMapStruct {

}
