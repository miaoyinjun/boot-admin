package org.jjche.sys.modules.system.mapper;

import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.domain.JobDO;

import java.util.List;

/**
 * <p>JobRepository interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-29
 */
public interface JobMapper extends MyBaseMapper<JobDO> {
    /**
     * <p>
     * 根据用户id查询
     * </p>
     *
     * @param userId 用户id
     * @return 岗位
     */
    List<JobDO> selectByUserId(Long userId);
}
