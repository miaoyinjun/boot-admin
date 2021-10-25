package com.boot.admin.system.modules.system.rest;

import cn.hutool.core.lang.Assert;
import com.boot.admin.common.annotation.PermissionData;
import com.boot.admin.common.enums.CodeEnum;
import com.boot.admin.common.pojo.DataScope;
import com.boot.admin.property.AdminProperties;
import com.boot.admin.property.PasswordProperties;
import com.boot.admin.system.modules.system.domain.UserDO;
import com.boot.admin.system.modules.system.dto.UserDTO;
import com.boot.admin.system.modules.system.dto.UserQueryCriteriaDTO;
import com.boot.admin.system.modules.system.dto.UserResetPassDTO;
import com.boot.admin.system.modules.system.service.*;
import com.boot.admin.system.modules.system.vo.UserPassVO;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.common.util.RsaUtils;
import com.boot.admin.core.annotation.controller.AdminRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.util.SecurityUtils;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.security.dto.RoleSmallDto;
import com.boot.admin.security.dto.UserVO;
import com.boot.admin.security.property.SecurityProperties;
import com.boot.admin.security.property.SecurityRsaProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>UserController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Api(tags = "系统：用户管理")
@AdminRestController("users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final DataService dataService;
    private final DeptService deptService;
    private final RoleService roleService;
    private final VerifyService verificationCodeService;
    private final SecurityProperties properties;
    private final AdminProperties adminConfig;
    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.system.modules.system.dto.UserQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出用户数据", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "用户"
    )
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    @PermissionData(deptIdInFieldName = DataScope.F_SQL_SCOPE_NAME)
    public void download(HttpServletResponse response, UserQueryCriteriaDTO criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.system.dto.UserQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "用户"
    )
    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    @PermissionData(deptIdInFieldName = DataScope.F_SQL_SCOPE_NAME)
    public ResultWrapper<Object> query(UserQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(userService.queryAll(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.system.domain.UserDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "用户"
    )
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResultWrapper create(@Validated @RequestBody UserDTO resources) {
        PasswordProperties pwdConf = adminConfig.getUser().getPassword();
        String defaultPwd = pwdConf.getDefaultVal();
        userService.create(resources, passwordEncoder.encode(defaultPwd));
        return ResultWrapper.ok(defaultPwd);
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.system.domain.UserDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户"
    )
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResultWrapper update(@Validated @RequestBody UserDTO resources) {
        userService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>center.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.system.domain.UserDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改用户：个人中心", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户"
    )
    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ResultWrapper center(@Validated @RequestBody UserDO resources) {
        Boolean isUpdateOther = !resources.getId().equals(SecurityUtils.getCurrentUserId());
        Assert.isFalse(isUpdateOther, "不能修改他人资料");
        String userName = SecurityUtils.getCurrentUsername();
        userService.updateCenter(resources, userName);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "用户"
    )
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(roleService.findByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Assert.isFalse(currentLevel > optLevel, "角色权限不足，不能删除：" + userService.findById(id).getUsername());
        }
        userService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * <p>updatePass.</p>
     *
     * @param passVo a {@link com.boot.admin.system.modules.system.vo.UserPassVO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "修改密码", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户", isSaveParams = false
    )
    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResultWrapper updatePass(@RequestBody UserPassVO passVo) throws Exception {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        String oldPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passVo.getNewPass());
        //密码格式验证
        userService.checkPwd(newPass);
        UserVO user = userService.findByName(SecurityUtils.getCurrentUsername());
        Boolean isOldPwdError = !passwordEncoder.matches(oldPass, user.getPassword());
        Assert.isFalse(isOldPwdError, "修改失败，旧密码错误");
        Boolean isNewPwdMatchOldPwd = passwordEncoder.matches(newPass, user.getPassword());
        Assert.isFalse(isNewPwdMatchOldPwd, "新密码不能与旧密码相同");
        userService.updatePass(user.getUsername(), passwordEncoder.encode(newPass));
        return ResultWrapper.ok();
    }

    /**
     * <p>resetPass.</p>
     *
     * @param passDTO a {@link com.boot.admin.system.modules.system.dto.UserResetPassDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "重置密码", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户", isSaveParams = false
    )
    @ApiOperation("重置密码")
    @PreAuthorize("@el.check('admin')")
    @PutMapping(value = "/resetPass")
    public ResultWrapper resetPass(@RequestBody UserResetPassDTO passDTO) {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        String newPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passDTO.getNewPass());
        //密码格式验证
        userService.checkPwd(newPass);
        String username = passDTO.getUsername();
        userService.updatePass(username, passwordEncoder.encode(newPass));
        //管理员重置密码后，用户登陆后必须修改密码
        userService.updateUserMustResetPwd(username);
        return ResultWrapper.ok();
    }

    /**
     * <p>updateAvatar.</p>
     *
     * @param avatar a {@link org.springframework.web.multipart.MultipartFile} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改头像", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户", isSaveParams = false
    )
    @PostMapping(value = "/updateAvatar")
    public ResultWrapper<String> updateAvatar(@RequestParam MultipartFile avatar) {
        String userName = SecurityUtils.getCurrentUsername();
        return ResultWrapper.ok(userService.updateAvatar(avatar, userName));
    }

    /**
     * <p>updateEmail.</p>
     *
     * @param code a {@link java.lang.String} object.
     * @param user a {@link com.boot.admin.system.modules.system.domain.UserDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "修改邮箱", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户", isSaveParams = false
    )
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResultWrapper updateEmail(@PathVariable String code, @RequestBody UserDO user) throws Exception {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        String password = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), user.getPassword());
        UserVO userDto = userService.findByName(SecurityUtils.getCurrentUsername());
        Boolean isPwdError = !passwordEncoder.matches(password, userDto.getPassword());
        Assert.isFalse(isPwdError, "密码错误");
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userService.updateEmail(userDto.getUsername(), user.getEmail());
        return ResultWrapper.ok();
    }

}
