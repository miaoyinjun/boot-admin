package org.jjche.sys.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.dto.JwtUserDTO;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.PwdCheckUtil;
import org.jjche.common.vo.UserSampleVO;
import org.jjche.common.vo.UserVO;
import org.jjche.core.util.FileUtil;
import org.jjche.core.util.RequestHolder;
import org.jjche.core.util.SecurityUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.security.auth.sms.SmsCodeAuthenticationToken;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityLoginProperties;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.security.TokenProvider;
import org.jjche.security.service.JwtUserService;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.jjche.sys.modules.security.dto.AuthUserSmsDTO;
import org.jjche.sys.modules.security.dto.SmsCodeDTO;
import org.jjche.sys.modules.security.vo.LoginVO;
import org.jjche.sys.modules.system.domain.*;
import org.jjche.sys.modules.system.dto.UserCenterDTO;
import org.jjche.sys.modules.system.dto.UserDTO;
import org.jjche.sys.modules.system.dto.UserQueryCriteriaDTO;
import org.jjche.sys.modules.system.mapper.UserJobMapper;
import org.jjche.sys.modules.system.mapper.UserMapper;
import org.jjche.sys.modules.system.mapper.UserRoleMapper;
import org.jjche.sys.modules.system.mapstruct.UserMapStruct;
import org.jjche.sys.modules.tool.service.AvatarService;
import org.jjche.sys.property.AdminProperties;
import org.jjche.sys.property.AliYunSmsCodeProperties;
import org.jjche.sys.property.PasswordProperties;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>系统用户</p>
 *
 * @author miaoyj
 */
@Service
@RequiredArgsConstructor
public class UserService extends MyServiceImpl<UserMapper, UserDO> {

    private final UserJobMapper userJobMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserMapStruct userMapStruct;
    private final RedisService redisService;
    private final AuthService authService;
    private final AvatarService avatarService;
    private final JwtUserService jwtUserService;
    private final AdminProperties adminConfig;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final SmsService smsService;
    private final DeptService deptService;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(UserQueryCriteriaDTO criteria) {
        Long deptId = criteria.getDeptId();
        if (!ObjectUtils.isEmpty(deptId)) {
            criteria.getDeptIds().add(deptId);
            criteria.getDeptIds().addAll(deptService.getDeptChildren(deptService.findByPid(deptId)));
        }

        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(email LIKE {0} OR username LIKE {0} OR nick_name LIKE {0})", "%" + blurry + "%");
        }
        return queryWrapper;
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    public MyPage<UserVO> queryAll(UserQueryCriteriaDTO criteria, PageParam page) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(page, queryWrapper);
        List<UserVO> list = userMapStruct.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    public List<UserSampleVO> querySample(){
        UserQueryCriteriaDTO criteria = new UserQueryCriteriaDTO();
        List<UserDO> list = this.queryAll(criteria);
        return userMapStruct.toSampleVO(list);
    }

    /**
     * 查询全部不分页
     *
     * @param criteria 条件
     * @return /
     */
    public List<UserDO> queryAll(UserQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.list(queryWrapper);
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    public UserVO getUserById(Long id) {
        UserDO user = this.getById(id);
        AssertUtil.notNull(user, SysErrorCodeEnum.RECORD_NOT_FOUND);
        return userMapStruct.toVO(user);
    }

    /**
     * <p>
     * 根据用户名查询
     * </p>
     *
     * @param username 用户名
     * @return 用户
     */
    public UserDO getByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * 根据邮箱查询
     * </p>
     *
     * @param email 邮箱
     * @return 用户
     */
    public UserDO getByEmail(String email) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getEmail, email);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * 根据手机号查询
     * </p>
     *
     * @param phone 手机号
     * @return 用户
     */
    public UserDO getByPhone(String phone) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getPhone, phone);
        return this.getOne(queryWrapper);
    }

    /**
     * 新增用户
     *
     * @param resources /
     * @param pwd       /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(UserDTO resources, String pwd) {
        String username = resources.getUsername();
        UserDO user = this.getByUsername(username);
        AssertUtil.isNull(user, SysErrorCodeEnum.USER_USERNAME_ALREADY_ERROR, username);

        String email = resources.getEmail();
        user = this.getByEmail(email);
        AssertUtil.isNull(user, SysErrorCodeEnum.USER_EMAIL_ALREADY_ERROR, email);

        String phone = resources.getPhone();
        user = this.getByPhone(phone);
        AssertUtil.isNull(user, SysErrorCodeEnum.USER_PHONE_ALREADY_ERROR, phone);

        UserDO userDO = userMapStruct.toDO(resources);
        userDO.setPwdResetTime(DateUtil.date().toTimestamp());
        //是否激活密码策略
        Boolean newUserMustReset = adminConfig.getUser().getPassword().getNewUserMustReset();
        //新用户必须修改密码
        userDO.setIsMustResetPwd(newUserMustReset);
        userDO.setPassword(pwd);
        userDO.setDeptId(resources.getDept().getId());
        this.saveUser(userDO);
        resources.setId(userDO.getId());
        updateUserAndJobAndRole(resources);
    }

    /**
     * 编辑用户
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDTO resources) {
        Long userId = resources.getId();
        String usernameNew = resources.getUsername();
        String emailNew = resources.getEmail();
        String phoneNew = resources.getPhone();

        UserDO user = this.getById(userId);

        String usernameOld = user.getUsername();
        String emailOld = user.getEmail();
        String phoneOld = user.getPhone();

        AssertUtil.notNull(user, SysErrorCodeEnum.USER_NOT_FOUND_ERROR);
        UserDO user1 = this.getByUsername(usernameNew);
        UserDO user2 = this.getByEmail(emailNew);
        UserDO user3 = this.getByPhone(phoneNew);

        Boolean isUserEqual = user1 != null && !user.getId().equals(user1.getId());
        AssertUtil.isFalse(isUserEqual, SysErrorCodeEnum.USER_USERNAME_ALREADY_ERROR, usernameNew);

        isUserEqual = user2 != null && !user.getId().equals(user2.getId());
        AssertUtil.isFalse(isUserEqual, SysErrorCodeEnum.USER_EMAIL_ALREADY_ERROR, emailNew);

        isUserEqual = user3 != null && !user.getId().equals(user3.getId());
        AssertUtil.isFalse(isUserEqual, SysErrorCodeEnum.USER_PHONE_ALREADY_ERROR, phoneNew);
        /**
         * 如果用户被禁用
         * 或用户名、手机号、邮箱被修改
         * 则清除用户登录信息
         */
        boolean userUpdate = !StrUtil.equals(usernameOld, usernameNew)
                || !StrUtil.equals(emailOld, emailNew) || !StrUtil.equals(phoneOld, phoneNew);
        if (!resources.getEnabled() || userUpdate) {
            authService.kickOutForUsername(usernameOld);
        }
        user.setUsername(usernameNew);
        user.setEmail(emailOld);
        user.setEnabled(resources.getEnabled());
        user.setPhone(phoneOld);
        user.setNickName(resources.getNickName());
        user.setGender(resources.getGender());
        user.setDeptId(resources.getDept().getId());
        this.updateById(user);

        updateUserAndJobAndRole(resources);
        // 清除缓存
        flushCache(usernameNew);
        flushCache(emailNew);
        flushCache(phoneNew);

        //修改用户名等
        flushCache(usernameOld);
        flushCache(phoneOld);
        flushCache(emailOld);
    }

    /**
     * <p>
     * 重置用户与角色关系
     * </p>
     *
     * @param resources 用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndJobAndRole(UserDTO resources) {
        Long userId = resources.getId();
        List<Long> jobIds = resources.getJobIds();
        this.updateUserAndJob(userId, jobIds);
        List<Long> roleIds = resources.getRoleIds();
        this.updateUserAndRole(userId, roleIds);
    }

    /**
     * 修改用户所有属性
     *
     * @param user /
     */
    public void saveUser(UserDO user) {
        this.saveOrUpdate(user);
        // 清除缓存
        flushCache(user.getUsername());
        flushCache(user.getPhone());
        flushCache(user.getEmail());
    }

    /**
     * 用户自助修改资料
     *
     * @param resources a {@link UserDO} object.
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(UserCenterDTO resources) {
        Boolean isUpdateOther = !resources.getId().equals(SecurityUtil.getUserId());
        AssertUtil.isFalse(isUpdateOther, SysErrorCodeEnum.USER_NOT_ALLOWED_UPDATE_OTHER_ERROR);
        UserDO user = this.getById(resources.getId());
        UserDO user1 = this.getByPhone(resources.getPhone());
        Boolean isUserEqual = user1 != null && !user.getId().equals(user1.getId());
        AssertUtil.isFalse(isUserEqual, SysErrorCodeEnum.USER_PHONE_ALREADY_ERROR, resources.getPhone());

        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        this.updateById(user);
    }

    /**
     * 删除用户
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            UserVO user = this.getUserById(id);
            flushCache(user.getUsername());
            flushCache(user.getPhone());
            flushCache(user.getEmail());
            this.removeById(id);
        }
    }

    /**
     * 根据用户名查询
     *
     * @param userName /
     * @return /
     */
    public UserVO findByName(String userName) {
        UserDO user = this.getByUsername(userName);
        return userMapStruct.toVO(user);
    }

    /**
     * 修改密码
     *
     * @param username 用户名
     * @param pass     密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        this.baseMapper.updatePass(username, pass, new Date());
        flushCache(username);
    }

    /**
     * <p>
     * 修改用户必须修改密码属性
     * </p>
     *
     * @param username 用户名
     */
    public void updateUserMustResetPwd(String username) {
        this.baseMapper.updateUserMustResetPwd(username);
    }

    /**
     * <p>
     * 密码格式验证
     * </p>
     *
     * @param pass 密码
     */
    public void checkPwd(String pass) {
        PasswordProperties pwdConf = adminConfig.getUser().getPassword();
        //密码复杂度验证
        //长度
        String minLength = pwdConf.getMinLength();
        String maxLength = pwdConf.getMaxLength();
        boolean isLength = PwdCheckUtil.checkPasswordLength(pass, minLength, maxLength);
        AssertUtil.isTrue(isLength, SysErrorCodeEnum.USER_PWD_LENGTH_ERROR, minLength, maxLength);
        //大写
        if (pwdConf.getUpperCase()) {
            boolean isUpperCase = PwdCheckUtil.checkContainUpperCase(pass);
            AssertUtil.isTrue(isUpperCase, SysErrorCodeEnum.USER_PWD_UC_ERROR);
        }

        //小写
        if (pwdConf.getLowerCase()) {
            boolean isLowerCase = PwdCheckUtil.checkContainLowerCase(pass);
            AssertUtil.isTrue(isLowerCase, SysErrorCodeEnum.USER_PWD_LC_ERROR);
        }
        //数字
        if (pwdConf.getDigit()) {
            boolean isDigit = PwdCheckUtil.checkContainDigit(pass);
            AssertUtil.isTrue(isDigit, SysErrorCodeEnum.USER_PWD_NUMBER_ERROR);
        }
        //特殊符号
        if (pwdConf.getSpecialChar()) {
            boolean isSpecialChar = PwdCheckUtil.checkContainSpecialChar(pass);
            AssertUtil.isTrue(isSpecialChar, SysErrorCodeEnum.USER_PWD_SC_ERROR);
        }
    }

    /**
     * <p>
     * 账号过期批量设置
     * </p>
     *
     * @param days 天数
     * @return 修改记录条数
     */
    public Integer updateAccountExpired(Integer days) {
        return this.baseMapper.updateAccountExpired(days);
    }

    /**
     * <p>
     * 密码过期批量设置
     * </p>
     *
     * @param days 天数
     * @return 修改记录条数
     */
    public Integer updateCredentialsExpired(Integer days) {
        return this.baseMapper.updateCredentialsExpired(days);
    }

    /**
     * <p>
     * 必须修改密码批量设置
     * </p>
     *
     * @param days 天数
     * @return 修改记录条数
     */
    public Integer updateAllUserMustResetPwd(Integer days) {
        return this.baseMapper.updateAllUserMustResetPwd(days);
    }

    /**
     * <p>
     * 提前15天登录界面提醒用户修改密码
     * </p>
     *
     * @param days 天数
     * @return 修改记录条数
     */
    public Integer updateUserTipResetPwd(Integer days) {
        return this.baseMapper.updateUserTipResetPwd(days);
    }

    /**
     * <p>
     * 查询根据用户手机号
     * </p>
     *
     * @param phone 手机号
     * @return 用户
     */
    public UserVO findDtoByPhone(String phone) {
        UserDO user = this.getByPhone(phone);
        return userMapStruct.toVO(user);
    }

    /**
     * <p>
     * 登录认证
     * </p>
     *
     * @param authenticationToken 认证信息
     * @param userTypeEnum        用户类型
     * @return 登录信息
     */
    public LoginVO loginByAuthenticationToken(AbstractAuthenticationToken authenticationToken, UserTypeEnum userTypeEnum) {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication.getName(), userTypeEnum);
        final JwtUserDTO jwtUserDto = (JwtUserDTO) authentication.getPrincipal();
        // 保存在线信息
        authService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        LoginVO loginVO = new LoginVO(jwtUserDto, securityJwtProperties.getTokenStartWith() + token);
        SecurityLoginProperties loginProperties = properties.getLogin();
        if (loginProperties.isSingle()) {
            //踢掉之前已经登录的token
            authService.checkLoginOnUser(authentication.getName(), token);
        }
        this.updateLastLoginTimeAndCleanPwdFailsCount(loginVO.getUser().getUser().getId());
        return loginVO;
    }

    /**
     * 修改头像
     *
     * @param multipartFile 文件
     * @return /
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateAvatar(MultipartFile multipartFile) {
        UserDO user = this.getByUsername(SecurityUtil.getUsername());
        String username = user.getUsername();
        String oldPath = user.getAvatarPath();
        File file = avatarService.uploadAvatar(multipartFile);
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        this.updateById(user);
        if (StrUtil.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        flushCache(username);
        return file.getName();
    }

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        this.baseMapper.updateEmail(username, email);
        flushCache(username);
        flushCache(email);
    }

    /**
     * <p>
     * 修改最后一次登录时间，重置密码连续错误次数
     * </p>
     *
     * @param userId 用户id
     * @author miaoyj
     * @since 2020-11-11
     */
    public void updateLastLoginTimeAndCleanPwdFailsCount(Long userId) {
        UserDO user = this.getById(userId);
        if (user != null) {
            user.setPwdFailsCount(0);
            user.setLastLoginTime(DateUtil.date().toTimestamp());
            this.updateById(user);
        }
    }

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<UserDO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDO userDTO : queryAll) {
            List<String> roles = userDTO.getRoles().stream().map(RoleDO::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("角色", roles);
            map.put("部门", userDTO.getDept().getName());
            map.put("岗位", userDTO.getJobs().stream().map(JobDO::getName).collect(Collectors.toList()));
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("修改密码的时间", userDTO.getPwdResetTime());
            map.put("创建日期", userDTO.getGmtCreate());
            map.put("最后一次登录时间", userDTO.getLastLoginTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        jwtUserService.removeByUserName(username);
    }

    /**
     * <p>
     * 获取手机号验证码
     * </p>
     *
     * @param dto 手机号信息
     * @return 手机号验证码
     */
    public String getSmsCode(SmsCodeDTO dto) {
        String phone = dto.getPhone();
        // 查询验证码
        String captchaCodeUuid = dto.getCaptchaCodeUuid();
        String captchaCode = redisService.stringGetString(captchaCodeUuid);
        // 清除验证码
        redisService.delete(captchaCodeUuid);
        AssertUtil.notBlank(captchaCode, SysErrorCodeEnum.AUTH_VALID_NOT_FOUND_ERROR);
        AssertUtil.isTrue(StrUtil.equalsIgnoreCase(dto.getCaptchaCode(), captchaCode), SysErrorCodeEnum.AUTH_VALID_ERROR);
        UserDO user = this.getByPhone(phone);
        AssertUtil.notNull(user, SysErrorCodeEnum.AUTH_USER_PHONE_NOT_FOUND_ERROR);
        if (!user.getEnabled()) {
            throw new DisabledException("");
        }
        //短信验证码
        String smsCode = RandomUtil.randomNumbers(6);
        StaticLog.info("phone:{}, smsCode:{}", phone, smsCode);
        //短信的配置
        AliYunSmsCodeProperties smsCodeConfig = adminConfig.getSms();
        AssertUtil.notNull(smsCodeConfig, SysErrorCodeEnum.AUTH_SMS_NOT_CONFIG_ERROR);
        //保留时长
        Long smsCodeTimeInterval = smsCodeConfig.getTimeInterval();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String uuid = IdUtil.simpleUUID();
        String redisUuid = securityJwtProperties.getCodeKey() + phone + ":" + uuid;
        // 保存
        redisService.stringSetString(redisUuid, smsCode, smsCodeTimeInterval);
        try {
            Boolean isSuccess = smsService.sendSmsValidCode(phone, smsService.getTemplateCodeLogin(), smsCode);
            StaticLog.info("phone:{}, isSuccess:{}", phone, isSuccess);
        } catch (Exception e) {
            StaticLog.error("phone:{}, isSuccess:{}", phone);
        }
        return uuid;
    }

    /**
     * 根据岗位查询
     *
     * @param ids /
     * @return /
     */
    public int countByJobs(Set<Long> ids) {
        return this.baseMapper.countByJobs(ids);
    }

    /**
     * <p>
     * 重置用户与岗位关系
     * </p>
     *
     * @param userId 用户id
     * @param jobIds 岗位id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndJob(Long userId, List<Long> jobIds) {
        LambdaQueryWrapper<UserJobDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserJobDO::getUserId, userId);
        this.userJobMapper.delete(queryWrapper);
        if (CollUtil.isNotEmpty(jobIds)) {
            for (Long jobId : jobIds) {
                UserJobDO userJobDO = new UserJobDO(userId, jobId);
                this.userJobMapper.insert(userJobDO);
            }
        }
    }

    /**
     * <p>
     * 重置用户与角色关系
     * </p>
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndRole(Long userId, List<Long> roleIds) {
        LambdaQueryWrapper<UserRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleDO::getUserId, userId);
        this.userRoleMapper.delete(queryWrapper);
        if (CollUtil.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                UserRoleDO userRoleDO = new UserRoleDO(userId, roleId);
                this.userRoleMapper.insert(userRoleDO);
            }
        }
    }

    /**
     * <p>
     * 获取今日用户数
     * </p>
     *
     * @return /
     */
    public Long countTodayUser() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("to_days(gmt_create) = to_days(now())");
        return this.baseMapper.selectCount(queryWrapper);
    }

    /**
     * <p>
     * 短信登录
     * </p>
     *
     * @param dto 入参
     * @return /
     */
    public LoginVO smslogin(AuthUserSmsDTO dto) {
        String phone = dto.getPhone();
        String uuid = dto.getSmsCodeUuid();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String redisUuid = securityJwtProperties.getCodeKey() + phone + ":" + uuid;
        String pCode = dto.getSmsCode();
        // 查询验证码
        String code = redisService.stringGetString(redisUuid);
        AssertUtil.notBlank(code, SysErrorCodeEnum.AUTH_PHONE_VALID_NOT_FOUND_ERROR);
        boolean smsCodeEqual = StrUtil.equals(pCode, code);
        if (BooleanUtil.isFalse(smsCodeEqual)) {
            String smsCodeErrorCountKey = StrUtil.format("{}:sms_error_count", redisUuid);
            Long smsCodeErrorCount = redisService.objectGetObject(smsCodeErrorCountKey, Long.class);
            if (smsCodeErrorCount == null) {
                smsCodeErrorCount = 1L;
                //短信的配置
                AliYunSmsCodeProperties smsCodeConfig = adminConfig.getSms();
                AssertUtil.notNull(smsCodeConfig, SysErrorCodeEnum.AUTH_PHONE_VALID_NOT_FOUND_ERROR);
                //保留时长
                Long smsCodeTimeInterval = smsCodeConfig.getTimeInterval();
                redisService.objectSetObject(smsCodeErrorCountKey, smsCodeErrorCount, smsCodeTimeInterval);
            } else {
                smsCodeErrorCount = redisService.stringIncrementLongString(smsCodeErrorCountKey, 1L);
            }
            boolean isErrorMax = smsCodeErrorCount > 3;
            if (isErrorMax) {
                redisService.delete(smsCodeErrorCountKey);
                redisService.delete(redisUuid);
            }
            AssertUtil.isFalse(isErrorMax, SysErrorCodeEnum.AUTH_PHONE_VALID_ALWAYS_ERROR);
        }
        AssertUtil.isTrue(smsCodeEqual, SysErrorCodeEnum.AUTH_PHONE_VALID_ERROR);

        SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(phone);

        LoginVO loginVO = this.loginByAuthenticationToken(authenticationToken, UserTypeEnum.SMS);
        // 清除验证码
        redisService.delete(redisUuid);
        return loginVO;
    }

    public List<UserVO> listUserByIds(Set<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(UserDO::getId, ids);
            return userMapStruct.toVO(this.list(queryWrapper));
        } else {
            return CollUtil.newArrayList();
        }
    }

    /**
     * <p>
     * 根据部门Ids查询
     * </p>
     *
     * @param deptIds 部门
     * @return /
     */
    public List<UserVO> listUserByDeptIds(Set<Long> deptIds) {
        if (CollUtil.isNotEmpty(deptIds)) {
            LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(UserDO::getDeptId, deptIds);
            return userMapStruct.toVO(this.list(queryWrapper));
        } else {
            return CollUtil.newArrayList();
        }
    }

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    public void validUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<UserDO> users = this.listByIds(ids);
        Map<Long, UserDO> userMap = MapUtil.newHashMap();;
        userMap = CollUtil.toMap(users, userMap, UserDO::getId);
        // 校验
        Map<Long, UserDO> finalUserMap = userMap;
        ids.forEach(id -> {
            UserDO user = finalUserMap.get(id);
            AssertUtil.notNull(user, SysErrorCodeEnum.USER_NOT_FOUND_ERROR);
        });
    }

    public Set<Long> listUserIdsByJobIds(Set<Long> jobIds) {
        return this.userJobMapper.selectUserIdByJobIds(jobIds);
    }
}
