package org.jjche.system.modules.system.rest;

import cn.hutool.core.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.annotation.PermissionData;
import org.jjche.common.dto.RoleSmallDto;
import org.jjche.common.dto.UserVO;
import org.jjche.common.enums.CodeEnum;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.pojo.DataScope;
import org.jjche.common.util.RsaUtils;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.util.SecurityUtil;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.property.AdminProperties;
import org.jjche.property.PasswordProperties;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.property.SecurityRsaProperties;
import org.jjche.system.modules.system.api.dto.UserCenterDTO;
import org.jjche.system.modules.system.api.dto.UserDTO;
import org.jjche.system.modules.system.api.dto.UserQueryCriteriaDTO;
import org.jjche.system.modules.system.api.dto.UserResetPassDTO;
import org.jjche.system.modules.system.api.vo.UserPassVO;
import org.jjche.system.modules.system.domain.UserDO;
import org.jjche.system.modules.system.service.RoleService;
import org.jjche.system.modules.system.service.UserService;
import org.jjche.system.modules.system.service.VerifyService;
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
@SysRestController("users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final VerifyService verificationCodeService;
    private final SecurityProperties properties;
    private final AdminProperties adminConfig;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link UserQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
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
     * @param criteria a {@link UserQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    @PermissionData(deptIdInFieldName = DataScope.F_SQL_SCOPE_NAME)
    public ResultWrapper<MyPage<UserVO>> query(UserQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(userService.queryAll(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link UserDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(
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
     * @param resources a {@link UserDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(
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
     * @param resources a {@link UserDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(
            value = "修改用户：个人中心", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户"
    )
    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ResultWrapper center(@Validated @RequestBody UserCenterDTO resources) {
        userService.updateCenter(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "用户"
    )
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(roleService.findByUsersId(SecurityUtil.getUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Assert.isFalse(currentLevel > optLevel, "角色权限不足，不能删除：" + userService.findById(id).getUsername());
        }
        userService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * <p>updatePass.</p>
     *
     * @param passVo a {@link UserPassVO} object.
     * @return a {@link ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecord(
            value = "修改密码", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户", saveParams = false
    )
    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResultWrapper updatePass(@RequestBody UserPassVO passVo) throws Exception {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        String oldPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passVo.getNewPass());
        //密码格式验证
        userService.checkPwd(newPass);
        UserVO user = userService.findByName(SecurityUtil.getUsername());
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
     * @param passDTO a {@link UserResetPassDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(
            value = "重置密码", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户", saveParams = false
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
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(
            value = "修改头像", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户", saveParams = false
    )
    @PostMapping(value = "/updateAvatar")
    public ResultWrapper<String> updateAvatar(@RequestParam MultipartFile avatar) {
        return ResultWrapper.ok(userService.updateAvatar(avatar));
    }

    /**
     * <p>updateEmail.</p>
     *
     * @param code a {@link java.lang.String} object.
     * @param user a {@link UserDO} object.
     * @return a {@link ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecord(
            value = "修改邮箱", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "用户", saveParams = false
    )
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResultWrapper updateEmail(@PathVariable String code, @RequestBody UserDO user) throws Exception {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        String password = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), user.getPassword());
        UserVO userDto = userService.findByName(SecurityUtil.getUsername());
        Boolean isPwdError = !passwordEncoder.matches(password, userDto.getPassword());
        Assert.isFalse(isPwdError, "密码错误");
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userService.updateEmail(userDto.getUsername(), user.getEmail());
        return ResultWrapper.ok();
    }
}