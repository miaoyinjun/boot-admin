package org.jjche.common.service;

import org.jjche.common.dto.UserVO;

import java.util.List;
import java.util.Set;

/**
 * <p>系统用户</p>
 *
 * @author miaoyj
 */
public interface IUserService {
    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    UserVO findById(long id);

    /**
     * 根据ID查询
     *
     * @param ids ID
     * @return /
     */
    List<UserVO> listByIds(Set<Long> ids);

    /**
     * <p>
     * 根据部门Ids查询
     * </p>
     *
     * @param deptIds 部门
     * @return /
     */
    List<UserVO> listByDeptIds(Set<Long> deptIds);
}
