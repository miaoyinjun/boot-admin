package org.jjche.system.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.*;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.system.modules.system.service.SysBaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 基础服务
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-08
 */
@Api(tags = "系统：基础")
@SysRestController("base")
@RequiredArgsConstructor
public class SysBaseController {

    private final SysBaseService sysBaseService;

    /**
     * 校验部门们是否有效。如下情况，视为无效：
     * 1. 部门编号不存在
     * 2. 部门被禁用
     *
     * @param ids 角色编号数组
     */
    @GetMapping("dept/valid-ids")
    @ApiOperation("校验部门们是否有效")
    public void validDeptList(@RequestParam Set<Long> ids){
        sysBaseService.validDeptList(ids);
    }

    /**
     * 根据IDS查询部门
     *
     * @param ids ID
     * @return /
     */
    @ApiOperation("根据IDS查询部门")
    @GetMapping("dept/small-ids")
    public List<DeptSmallDTO> listDeptSmallByIds(@RequestParam Set<Long> ids){
        return sysBaseService.listDeptSmallByIds(ids);
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    @ApiOperation("根据ID查询部门")
    @GetMapping("dept/small-id")
    public DeptSmallDTO getDeptSmallById(@RequestParam Long id){
        return sysBaseService.getDeptSmallById(id);
    }

    /**
     * <p>
     * 获取所有精简部门
     * </p>
     *
     * @return /
     */
    @ApiOperation("获取所有精简部门")
    @GetMapping("dept/list-small")
    public List<DeptSmallDTO> listDeptSmall(){
        return sysBaseService.listDeptSmall();
    }

    /**
     * <p>
     * 根据部门Ids查询用户
     * </p>
     *
     * @param deptIds 部门
     * @return /
     */
    @ApiOperation("根据部门Ids查询用户")
    @GetMapping("user/list-by-dept-ids")
    public List<UserVO> listUserByDeptIds(@RequestParam Set<Long> deptIds){
        return sysBaseService.listUserByDeptIds(deptIds);
    }

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values 字典数据值的数组
     */
    @ApiOperation("校验字典数据们是否有效")
    @GetMapping("dict/valid")
    public void validDictList(@RequestParam String dictType, @RequestParam Set<String> values){
        sysBaseService.validDictList(dictType, values);
    }

    /**
     * <p>
     * 获取根据字典名称和值
     * </p>
     *
     * @param name  字典名称
     * @param value 字典值
     * @return /
     */
    @ApiOperation("获取根据字典名称和值")
    @GetMapping("dict/value")
    public DictParam getDictByNameValue(@RequestParam("name") String name, @RequestParam("value") String value){
        return sysBaseService.getDictByNameValue(name, value);
    }

    /**
     * <p>
     * 记录单条日志
     * </p>
     *
     * @param logRecord /
     */
    @ApiOperation("记录单条日志")
    @PostMapping("log/record")
    public void recordLog(@RequestBody LogRecordDTO logRecord){
        sysBaseService.recordLog(logRecord);
    }

    /**
     * <p>
     * 批量记录日志
     * </p>
     *
     * @param list /
     */
    @ApiOperation("批量记录日志")
    @PostMapping("log/records")
    public void recordLogs(@RequestBody List<LogRecordDTO> list){
        sysBaseService.recordLogs(list);
    }


    /**
     * <p>
     * 清除在线用户token
     * </p>
     *
     * @param token /
     */
    @ApiOperation("清除在线用户token")
    @GetMapping("auth/logout-token")
    public void logoutOnlineUser(@RequestParam("token") String token){
        sysBaseService.logoutOnlineUser(token);
    }

    /**
     * <p>
     * 获取当前登录认证信息
     * </p>
     *
     * @return /
     */
    @ApiOperation("获取当前登录认证信息")
    @GetMapping("auth/user-details")
    public JwtUserDTO getUserDetails(){
        return sysBaseService.getUserDetails();
    }

    /**
     * <p>
     * 根据参数token获取认证信息
     * </p>
     *
     * @param token /
     * @return /
     */
    @ApiOperation("根据参数token获取认证信息")
    @GetMapping("auth/user-details-token")
    public JwtUserDTO getUserDetails(@RequestHeader(SecurityConstant.HEADER_AUTH) String token){
        return sysBaseService.getUserDetails(token);
    }

    /**
     * 查询数据权限字段
     *
     * @param dto 查询条件
     * @return 查询结果
     */
    @ApiOperation("查询数据权限字段")
    @PostMapping("permission/permission-data-resource")
    public List<DataPermissionFieldResultVO> listPermissionDataResource(@RequestBody PermissionDataResourceDTO dto){
        return sysBaseService.listPermissionDataResource(dto);
    }

    @ApiOperation("根据用户id查询权限")
    @GetMapping("permission/permission-data-rule-by-userid")
    public List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(@RequestParam Long userId){
        return sysBaseService.listPermissionDataRuleByUserId(userId);
    }

    @ApiOperation("校验角色们是否有效")
    @GetMapping("role/valid-ids")
    public void validRoleList(@RequestParam Set<Long> ids){
        sysBaseService.validRoleList(ids);
    }

    @ApiOperation("获得拥有多个角色的用户编号集合")
    @GetMapping("role/user-role-ids")
    public Set<Long> getUserRoleIdByRoleIds(@RequestParam Set<Long> roleIds){
        return sysBaseService.getUserRoleIdByRoleIds(roleIds);
    }

    @ApiOperation("根据应用id获取密钥")
    @GetMapping("app-key/key-by-app-id")
    public SecurityAppKeyBasicVO getAppKeyByAppId(@RequestParam("appId") String appId){
        return sysBaseService.getAppKeyByAppId(appId);
    }

    @ApiOperation("校验用户们是否有效")
    @GetMapping("user/valid")
    public void validUserList(@RequestParam Set<Long> ids){
        sysBaseService.validUserList(ids);
    }

    /**
     * 根据ID查询用户
     *
     * @param id ID
     * @return /
     */
    @ApiOperation("根据用户ID查询用户")
    @GetMapping("user/get-by-id")
    public UserVO getUserById(@RequestParam Long id){
        return sysBaseService.getUserById(id);
    }

    @ApiOperation("根据用户IDS查询用户")
    @GetMapping("user/list-by-ids")
    public List<UserVO> listUserByIds(@RequestParam Set<Long> ids){
        return sysBaseService.listUserByIds(ids);
    }

    @ApiOperation("获得指定岗位的用户数组")
    @GetMapping("job/list-user-ids")
    public Set<Long> listUserIdsByJobIds(@RequestParam Set<Long> jobIds){
        return sysBaseService.listUserIdsByJobIds(jobIds);
    }

    @ApiOperation("校验岗位们是否有效")
    @GetMapping("job/valid")
    public void validJobIds(@RequestParam Set<Long> ids){
        sysBaseService.validJobIds(ids);
    }
}
