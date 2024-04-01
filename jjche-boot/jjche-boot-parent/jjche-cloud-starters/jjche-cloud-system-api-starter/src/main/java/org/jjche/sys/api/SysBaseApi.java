package org.jjche.sys.api;

import org.jjche.common.api.CommonApi;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.*;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.common.constant.SysBaseApiConstants;
import org.jjche.common.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统基础cloud接口
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@FeignClient(contextId = SysBaseApiConstants.NAME + "-base-api",
        name = SysBaseApiConstants.NAME,
        path = "/sys/base/",
        url = SysBaseApiConstants.FEIGN_URL)
public interface SysBaseApi extends CommonApi {

    /**
     * 校验部门们是否有效。如下情况，视为无效：
     * 1. 部门编号不存在
     * 2. 部门被禁用
     *
     * @param ids 角色编号数组
     */
    @PostMapping("dept/valid-ids")
    void validDeptList(@RequestBody Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param ids ID
     * @return /
     */
    @PostMapping("dept/small-ids")
    List<DeptSmallDTO> listDeptSmallByIds(@RequestBody Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    @GetMapping("dept/small-id")
    DeptSmallDTO getDeptSmallById(@RequestParam("id") Long id);

    /**
     * <p>
     * 获取所有精简部门
     * </p>
     *
     * @return /
     */
    @GetMapping("dept/list-small")
    List<DeptSmallDTO> listDeptSmall();

    /**
     * <p>
     * 根据部门Ids查询
     * </p>
     *
     * @param deptIds 部门
     * @return /
     */
    @PostMapping("user/list-by-dept-ids")
    List<UserVO> listUserByDeptIds(@RequestBody Set<Long> deptIds);

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values   字典数据值的数组
     */
    @PostMapping("dict/valid")
    void validDictList(@RequestParam("dictType") String dictType, @RequestBody Set<String> values);

    /**
     * <p>
     * 获取根据字典名称和值
     * </p>
     *
     * @param name  字典名称
     * @param value 字典值
     * @return /
     */
    @Override
    @GetMapping("dict/value")
    DictParam getDictByNameValue(@RequestParam("name") String name, @RequestParam("value") String value);

    /**
     * <p>
     * 记录日志
     * </p>
     *
     * @param logRecord /
     */
    @Override
    @PostMapping("log/record")
    void recordLog(@RequestBody LogRecordDTO logRecord);

    /**
     * <p>
     * 批量记录日志
     * </p>
     *
     * @param list /
     */
    @PostMapping("log/records")
    void recordLogs(@RequestBody List<LogRecordDTO> list);


    /**
     * <p>
     * 清除在线用户token
     * </p>
     *
     * @param token /
     */
    @GetMapping("auth/logout-token")
    void logoutOnlineUser(@RequestParam("token") String token);

    /**
     * <p>
     * 获取认证
     * </p>
     *
     * @return /
     */
    @Override
    @GetMapping("auth/user-details")
    JwtUserDTO getUserDetails();

    /**
     * <p>
     * 根据参数token获取认证信息
     * </p>
     *
     * @param token /
     * @return /
     */
    @Override
    @GetMapping("auth/user-details")
    JwtUserDTO getUserDetails(@RequestHeader(SecurityConstant.HEADER_AUTH) String token);

    /**
     * 查询数据权限字段
     *
     * @param dto 查询条件
     * @return 查询结果
     */
    @Override
    @PostMapping("permission/permission-data-resource")
    List<DataPermissionFieldResultVO> listPermissionDataResource(@RequestBody PermissionDataResourceDTO dto);

    @Override
    @GetMapping("permission/permission-data-rule-by-userid")
    List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(@RequestParam("userId") Long userId);

    /**
     * 校验角色们是否有效。如下情况，视为无效：
     * 1. 角色编号不存在
     * 2. 角色被禁用
     *
     * @param ids 角色编号数组
     */
    @PostMapping("role/valid-ids")
    void validRoleList(@RequestBody Set<Long> ids);

    /**
     * 获得拥有多个角色的用户编号集合
     *
     * @param roleIds 角色编号集合
     * @return 用户编号集合
     */
    @PostMapping("role/user-role-ids")
    Set<Long> getUserRoleIdByRoleIds(@RequestBody Set<Long> roleIds);

    /**
     * <p>
     * 根据应用id获取密钥
     * </p>
     *
     * @param appId 应用id
     * @return /
     */
    @Override
    @GetMapping("app-key/key-by-app-id")
    SecurityAppKeyBasicVO getAppKeyByAppId(@RequestParam("appId") String appId);

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    @PostMapping("user/valid")
    void validUserList(@RequestBody Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    @GetMapping("user/get-by-id")
    UserVO getUserById(@RequestParam("id") Long id);

    /**
     * 根据ID查询
     *
     * @param ids ID
     * @return /
     */
    @PostMapping("user/list-by-ids")
    List<UserVO> listUserByIds(@RequestBody Set<Long> ids);

    /**
     * 获得指定岗位的用户数组
     *
     * @param jobIds 岗位数组
     * @return 用户id数组
     */
    @PostMapping("job/list-user-ids")
    Set<Long> listUserIdsByJobIds(@RequestBody Set<Long> jobIds);

    /**
     * 校验岗位们是否有效。如下情况，视为无效：
     * 1. 岗位编号不存在
     * 2. 岗位被禁用
     *
     * @param ids 岗位编号数组
     */
    @PostMapping("job/valid")
    void validJobIds(@RequestBody Set<Long> ids);
}
