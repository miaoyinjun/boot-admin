package org.jjche.system.modules.system.mapstruct;

import org.jjche.common.dto.UserVO;
import org.jjche.core.base.BaseMapStruct;
import org.jjche.system.modules.system.api.dto.UserDTO;
import org.jjche.system.modules.system.domain.UserDO;
import org.mapstruct.Mapper;

/**
 * <p>UserMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Mapper(componentModel = "spring")
public interface UserMapStruct extends BaseMapStruct<UserDO, UserDTO, UserVO> {
}
