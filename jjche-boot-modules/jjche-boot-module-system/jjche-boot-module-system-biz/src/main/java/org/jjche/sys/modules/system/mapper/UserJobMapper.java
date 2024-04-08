package org.jjche.sys.modules.system.mapper;

import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.domain.UserJobDO;

import java.util.Set;

/**
 * <p>UserMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
public interface UserJobMapper extends MyBaseMapper<UserJobDO> {
    /**
     * <p>
     * 根据岗位id查询用户id
     * </p>
     *
     * @param jobIds 岗位id
     * @return /
     */
    Set<Long> selectUserIdByJobIds(Set<Long> jobIds);
}
