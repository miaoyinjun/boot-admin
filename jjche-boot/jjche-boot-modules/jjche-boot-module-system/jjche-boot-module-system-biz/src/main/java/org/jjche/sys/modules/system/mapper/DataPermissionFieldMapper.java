package org.jjche.sys.modules.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.api.vo.DataPermissionFieldVO;
import org.jjche.sys.modules.system.domain.DataPermissionFieldDO;

import java.util.List;

/**
 * <p>JobRepository interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-29
 */
public interface DataPermissionFieldMapper extends MyBaseMapper<DataPermissionFieldDO> {
    /**
     * <p>
     * 根据用户id查询
     * </p>
     *
     * @param userId 用户id
     * @return /
     */
    List<DataPermissionFieldVO> queryByUserId(@Param("userId") Long userId);
}
