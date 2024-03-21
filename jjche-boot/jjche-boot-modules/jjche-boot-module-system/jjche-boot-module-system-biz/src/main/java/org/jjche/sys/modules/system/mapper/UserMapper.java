package org.jjche.sys.modules.system.mapper;

import org.jjche.common.dto.UserVO;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.domain.UserDO;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>UserMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
public interface UserMapper extends MyBaseMapper<UserDO> {

    /**
     * 修改密码
     *
     * @param username              用户名
     * @param pass                  密码
     * @param lastPasswordResetTime /
     */
    void updatePass(String username, String pass, Date lastPasswordResetTime);

    /**
     * <p>
     * 修改用户必须修改密码属性
     * </p>
     *
     * @param username 用户名
     */
    void updateUserMustResetPwd(String username);

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */
    void updateEmail(String username, String email);

    /**
     * 根据角色查询用户
     *
     * @param roleId /
     * @return /
     */
    List<UserDO> findByRoleId(Long roleId);

    /**
     * 根据角色中的部门查询
     *
     * @param id /
     * @return /
     */
    List<UserDO> findByDeptRoleId(Long id);

    /**
     * 根据菜单查询
     *
     * @param id 菜单ID
     * @return /
     */
    List<UserDO> findByMenuId(Long id);


    /**
     * 根据岗位查询
     *
     * @param ids /
     * @return /
     */
    int countByJobs(Set<Long> ids);

    /**
     * 根据部门查询
     *
     * @param deptIds /
     * @return /
     */
    int countByDepts(Set<Long> deptIds);

    /**
     * 根据角色查询
     *
     * @param ids /
     * @return /
     */
    int countByRoles(Set<Long> ids);

    /**
     * <p>
     * 账号过期批量设置
     * </p>
     *
     * @param days 天数
     * @return 修改记录条数
     */
    Integer updateAccountExpired(Integer days);

    /**
     * <p>
     * 密码过期批量设置
     * </p>
     *
     * @param days 天数
     * @return 修改记录条数
     */
    Integer updateCredentialsExpired(Integer days);

    /**
     * <p>
     * 必须修改密码批量设置
     * </p>
     *
     * @param days 天数
     * @return 修改记录条数
     */
    Integer updateAllUserMustResetPwd(Integer days);

    /**
     * <p>
     * 提前15天登录界面提醒用户修改密码
     * </p>
     *
     * @param days 天数
     * @return 修改记录条数
     */
    Integer updateUserTipResetPwd(Integer days);

    /**
     * <p>
     * 查询用户根据角色id
     * </p>
     *
     * @param page   分页
     * @param roleId 角色id
     * @param blurry 模糊
     * @return /
     */
    MyPage<UserVO> selectByRoleId(PageParam page, Long roleId, String blurry);

    /**
     * <p>
     * 查询非用户根据角色id
     * </p>
     *
     * @param page   分页
     * @param roleId 角色id
     * @param blurry 模糊
     * @return /
     */
    MyPage<UserVO> selectNotByRoleId(PageParam page, Long roleId, String blurry);
}
