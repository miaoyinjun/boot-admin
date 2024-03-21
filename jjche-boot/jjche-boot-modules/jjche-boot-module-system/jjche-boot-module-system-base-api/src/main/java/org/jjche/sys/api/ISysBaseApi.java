package org.jjche.sys.api;

import org.jjche.common.api.CommonApi;
import org.jjche.common.dto.DeptSmallDTO;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.common.dto.UserVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统基础实现
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
public interface ISysBaseApi extends CommonApi {

    /**
     * 校验部门们是否有效。如下情况，视为无效：
     * 1. 部门编号不存在
     * 2. 部门被禁用
     *
     * @param ids 角色编号数组
     */
    void validDeptList(Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param ids ID
     * @return /
     */
    List<DeptSmallDTO> listDeptSmallByIds(Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    DeptSmallDTO getDeptSmallById(Long id);

    /**
     * <p>
     * 获取所有精简部门
     * </p>
     *
     * @return /
     */
    List<DeptSmallDTO> listDeptSmall();

    /**
     * <p>
     * 根据部门Ids查询
     * </p>
     *
     * @param deptIds 部门
     * @return /
     */
    List<UserVO> listUserByDeptIds(Set<Long> deptIds);

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values 字典数据值的数组
     */
    void validDictList(String dictType, Set<String> values);

    /**
     * <p>
     * 批量记录日志
     * </p>
     *
     * @param list /
     */
    void recordLogs(List<LogRecordDTO> list);


    /**
     * <p>
     * 清除在线用户token
     * </p>
     *
     * @param token /
     */
    void logoutOnlineUser(String token);

    /**
     * 校验角色们是否有效。如下情况，视为无效：
     * 1. 角色编号不存在
     * 2. 角色被禁用
     *
     * @param ids 角色编号数组
     */
    void validRoleList(Set<Long> ids);

    /**
     * 获得拥有多个角色的用户编号集合
     *
     * @param roleIds 角色编号集合
     * @return 用户编号集合
     */
    Set<Long> getUserRoleIdByRoleIds(Set<Long> roleIds);

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    void validUserList(Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    UserVO getUserById(Long id);

    /**
     * 根据ID查询
     *
     * @param ids ID
     * @return /
     */
    List<UserVO> listUserByIds(Set<Long> ids);

    /**
     * 获得指定岗位的用户数组
     *
     * @param jobIds 岗位数组
     * @return 用户id数组
     */
    Set<Long> listUserIdsByJobIds(Set<Long> jobIds);

    /**
     * 校验岗位们是否有效。如下情况，视为无效：
     * 1. 岗位编号不存在
     * 2. 岗位被禁用
     *
     * @param ids 岗位编号数组
     */
    void validJobIds(Set<Long> ids);
}
