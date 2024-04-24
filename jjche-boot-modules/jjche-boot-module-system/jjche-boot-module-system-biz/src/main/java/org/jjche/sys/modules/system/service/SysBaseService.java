package org.jjche.sys.modules.system.service;

import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.*;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.common.vo.UserVO;
import org.jjche.sys.api.SysBaseApi;
import org.jjche.sys.modules.app.service.SecurityAppKeyService;
import org.jjche.sys.modules.logging.service.LogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统基础服务
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-08
 */
@Service
@RequiredArgsConstructor
public class SysBaseService implements SysBaseApi {
    private final DeptService deptService;
    private final UserService userService;
    private final DictDetailService dictDetailService;
    private final LogService logService;
    private final AuthService authService;
    private final RoleService roleService;
    private final JobService jobService;
    private final SecurityAppKeyService securityAppKeyService;
    private final DataPermissionFieldService dataPermissionFieldService;

    @Override
    public void validDeptList(Set<Long> ids) {
        deptService.validDeptList(ids);
    }

    @Override
    public List<DeptSmallDTO> listDeptSmallByIds(Set<Long> ids) {
        return deptService.listDeptSmallByIds(ids);
    }

    @Override
    public DeptSmallDTO getDeptSmallById(Long id) {
        return deptService.getDeptSmallById(id);
    }

    @Override
    public List<DeptSmallDTO> listDeptSmall() {
        return deptService.listDeptSmall();
    }

    @Override
    public List<UserVO> listUserByDeptIds(Set<Long> deptIds) {
        return userService.listUserByDeptIds(deptIds);
    }

    @Override
    public void validDictList(String dictType, Set<String> values) {
        dictDetailService.validDictList(dictType, values);
    }

    @Override
    public void recordLogs(List<LogRecordDTO> list) {
        logService.recordLogs(list);
    }

    @Override
    public void logoutOnlineUser(String token) {
        authService.logoutOnlineUser(token);
    }

    @Override
    public void validRoleList(Set<Long> ids) {
        roleService.validRoleList(ids);
    }

    @Override
    public Set<Long> getUserRoleIdByRoleIds(Set<Long> roleIds) {
        return roleService.getUserRoleIdByRoleIds(roleIds);
    }

    @Override
    public void validUserList(Set<Long> ids) {
        userService.validUserList(ids);
    }

    @Override
    public UserVO getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    public List<UserVO> listUserByIds(Set<Long> ids) {
        return userService.listUserByIds(ids);
    }

    @Override
    public Set<Long> listUserIdsByJobIds(Set<Long> jobIds) {
        return userService.listUserIdsByJobIds(jobIds);
    }

    @Override
    public void validJobIds(Set<Long> ids) {
        jobService.validJobIds(ids);
    }

    @Override
    public SecurityAppKeyBasicVO getAppKeyByAppId(String appId) {
        return securityAppKeyService.getKeyByAppId(appId);
    }

    @Override
    public JwtUserDTO getUserDetails() {
        return authService.getUserDetails();
    }

    @Override
    public JwtUserDTO getUserDetails(String token) {
        return authService.getUserDetails(token);
    }

    @Override
    public List<DataPermissionFieldResultVO> listPermissionDataResource(PermissionDataResourceDTO dto) {
        return dataPermissionFieldService.listPermissionDataResource(dto);
    }

    @Override
    public List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(Long userId) {
        return dataPermissionFieldService.listPermissionDataRuleByUserId(userId);
    }

    @Override
    public DictParam getDictByNameValue(String name, String value) {
        return dictDetailService.getDictByNameValue(name, value);
    }

    @Override
    public void recordLog(LogRecordDTO logRecord) {
        logService.recordLog(logRecord);
    }
}
