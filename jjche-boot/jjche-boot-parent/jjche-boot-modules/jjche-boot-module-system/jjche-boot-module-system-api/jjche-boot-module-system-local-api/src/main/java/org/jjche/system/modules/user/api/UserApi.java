package org.jjche.system.modules.user.api;

import org.jjche.common.dto.UserVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
public interface UserApi {

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    void validateUserList(Collection<Long> ids);

    /**
     * 获得指定岗位的用户数组
     *
     * @param jobIds 岗位数组
     * @return 用户id数组
     */
    Set<Long> listUserIdsByJobIds(Set<Long> jobIds);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    UserVO findById(Long id);

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
