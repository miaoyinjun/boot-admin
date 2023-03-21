package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.log.StaticLog;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.*;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.system.api.ISysBaseAPI;
import org.jjche.common.util.FileUtil;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.util.RsaUtils;
import org.jjche.common.util.StrUtil;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.core.util.SecurityUtil;
import org.jjche.log.modules.logging.domain.LogDO;
import org.jjche.log.modules.logging.mapstruct.LogRecordMapStruct;
import org.jjche.log.modules.logging.service.LogService;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.security.TokenProvider;
import org.jjche.security.service.JwtUserService;
import org.jjche.system.modules.app.service.SecurityAppKeyService;
import org.jjche.system.modules.system.api.dto.DictDetailDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 本地操作
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@Service
@AllArgsConstructor
public class SysBaseAPI implements ISysBaseAPI {

    private final SecurityProperties properties;
    private final RedisService redisService;
    private final JwtUserService jwtUserService;
    private final TokenProvider tokenProvider;
    private final LogService logService;
    private final DataPermissionRuleService dataPermissionRuleService;
    private final DataPermissionFieldService dataPermissionFieldService;
    private final LogRecordMapStruct logRecordMapper;
    private final SecurityAppKeyService appKeyService;
    private final DictDetailService dictDetailService;

    /**
     * 保存在线用户信息
     *
     * @param jwtUserDto /
     * @param token      /
     * @param request    /
     */
    public void save(JwtUserDto jwtUserDto, String token, HttpServletRequest request) {
        String dept = jwtUserDto.getUser().getDept().getName();
        String ip = ServletUtil.getClientIP(request);
        String address = HttpUtil.getCityInfo(ip);
        OnlineUserDTO onlineUserDto = null;
        try {
            String publicKey = properties.getRsa().getPublicKey();
            String encryptToken = RsaUtils.encryptBypPublicKey(publicKey, token);
            String ua = request.getHeader(HttpHeaders.USER_AGENT);
            UserAgent userAgent = UserAgentUtil.parse(ua);
            String browser = HttpUtil.getBrowser(userAgent);
            String os = HttpUtil.getOs(userAgent);
            onlineUserDto = new OnlineUserDTO(jwtUserDto.getUsername(),
                    jwtUserDto.getUser().getNickName(), dept, browser, ua, os, ip, address,
                    encryptToken, new Date(), new Date());
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
        }
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        redisService.objectSetObject(securityJwtProperties.getOnlineKey() + token, onlineUserDto, securityJwtProperties.getTokenValidityInMilliSeconds());
    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUserDTO> getAll(String filter) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        Set<String> keys = redisService.keys(securityJwtProperties.getOnlineKey());
        List<OnlineUserDTO> onlineUserDTOS = new ArrayList<>();
        for (String key : keys) {
            OnlineUserDTO onlineUserDto = redisService.objectGetObject(key, OnlineUserDTO.class);
            if (StrUtil.isNotBlank(filter)) {
                if (onlineUserDto.toString().contains(filter)) {
                    onlineUserDTOS.add(onlineUserDto);
                }
            } else {
                onlineUserDTOS.add(onlineUserDto);
            }
        }
        onlineUserDTOS.sort((o1, o2) -> o2.getLastAccessTime().compareTo(o1.getLastAccessTime()));
        return onlineUserDTOS;
    }

    /**
     * 踢出用户
     *
     * @param token /
     */
    public void kickOut(String token) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        token = securityJwtProperties.getOnlineKey() + token;
        redisService.delete(token);
    }

    /**
     * 导出
     *
     * @param all      /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<OnlineUserDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUserDTO user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("部门", user.getDept());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName   用户名
     * @param igoreToken a {@link java.lang.String} object.
     */
    public void checkLoginOnUser(String userName, String igoreToken) {
        List<OnlineUserDTO> onlineUserDTOS = getAll(userName);
        if (onlineUserDTOS == null || onlineUserDTOS.isEmpty()) {
            return;
        }
        for (OnlineUserDTO onlineUserDto : onlineUserDTOS) {
            if (onlineUserDto.getUserName().equals(userName)) {
                try {
                    String privateKey = properties.getRsa().getPrivateKey();
                    String token = RsaUtils.decryptByPrivateKey(privateKey, onlineUserDto.getKey());
                    if (StrUtil.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
                        this.kickOut(token);
                    } else if (StrUtil.isBlank(igoreToken)) {
                        this.kickOut(token);
                    }
                } catch (Exception e) {
                    StaticLog.error("checkUser is error", e);
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) {
        List<OnlineUserDTO> onlineUsers = getAll(username);
        String privateKey = properties.getRsa().getPrivateKey();
        for (OnlineUserDTO onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                // 解密Key
                String token = RsaUtils.decryptByPrivateKey(privateKey, onlineUser.getKey());
                kickOut(token);
            }
        }
    }

    @Override
    public void logoutOnlineUser(String token) {
        String username = SecurityUtil.getUsernameOrDefaultUsername();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String key = securityJwtProperties.getOnlineKey() + token;
        redisService.delete(key);
        jwtUserService.removeByUserName(username);
    }

    @Override
    public JwtUserDto getUserDetails() {
        UserDetails userDetails = null;
        String token = tokenProvider.resolveToken();
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDTO onlineUserDto = null;
            boolean cleanUserCache = false;
            try {
                onlineUserDto = this.getOnlineUser(token);
            } catch (ExpiredJwtException e) {
                StaticLog.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    this.logoutOnlineUser(token);
                }
            }
            if (onlineUserDto != null && org.springframework.util.StringUtils.hasText(token)) {
                userDetails = this.getAuthentication(token, onlineUserDto);
            }
        }
        return (JwtUserDto) userDetails;
    }

    @Override
    public JwtUserDto getUserDetails(String token) {
        return null;
    }

    @Override
    public void recordLog(LogRecordDTO logRecord) {
        logRecord.setCreateTime(DateUtil.date().toTimestamp());
        LogDO log = logRecordMapper.toLog(logRecord);
        logService.saveLog(log);
    }

    @Override
    public void recordLogs(List<LogRecordDTO> list) {
        List<LogDO> logs = logRecordMapper.toLog(list);
        logService.saveLog(logs);
    }

    @Override
    public List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(Long userId) {
        return dataPermissionRuleService.listByUserId(userId);
    }

    @Override
    public SecurityAppKeyBasicVO getKeyByAppId(String appId) {
        return appKeyService.getKeyByAppId(appId);
    }

    @Override
    public List<DataPermissionFieldResultVO> listPermissionDataResource(PermissionDataResourceDTO dto) {
        return dataPermissionFieldService.getDataResource(dto);
    }

    @Override
    public DictParam getDictByNameValue(String name, String value) {
        List<DictDetailDTO> list = dictDetailService.getDictByName(name);
        if (CollUtil.isNotEmpty(list)) {
            DictDetailDTO dictDetailDTO = null;
            for (DictDetailDTO dict : list) {
                if (StrUtil.equals(dict.getValue(), value)) {
                    dictDetailDTO = dict;
                    break;
                }
            }
            if (dictDetailDTO != null) {
                DictParam dictParam = new DictParam();
                dictParam.setValue(dictDetailDTO.getValue());
                dictParam.setLabel(dictDetailDTO.getLabel());
                return dictParam;
            }
        }
        return null;
    }

    /**
     * <p>
     * 获取认证
     * </p>
     *
     * @param token         /
     * @param onlineUserDto /
     * @return /
     */
    private UserDetails getAuthentication(String token, OnlineUserDTO onlineUserDto) {
        UserDetails userDetails = null;
        Claims claims = tokenProvider.getClaims(token);
        String userType = claims.getIssuer();
        String username = claims.getSubject();
        UserDetailsService userDetailsService = null;
        //密码
        if (StrUtil.equals(UserTypeEnum.PWD.getValue(), userType)) {
            userDetailsService = SpringUtil.getBean(SecurityConstant.USER_DETAILS_PWD_SERVICE);
        } else {
            userDetailsService = SpringUtil.getBean(SecurityConstant.USER_DETAILS_SMS_SERVICE);
        }
        userDetails = userDetailsService.loadUserByUsername(username);
        // Token 续期
        this.checkRenewal(token, onlineUserDto);
        return userDetails;
    }

    /**
     * <p>
     * 检查token续期
     * </p>
     *
     * @param token         /
     * @param onlineUserDto /
     */
    private void checkRenewal(String token, OnlineUserDTO onlineUserDto) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String tokenKey = securityJwtProperties.getOnlineKey() + token;
        long renew = securityJwtProperties.getTokenValidityInMilliSeconds();

        //更新最后访问时间，由于每次都要修改token缓存，所以判断是否续期token减少redis操作的目的就没意义了
        onlineUserDto.setLastAccessTime(DateUtil.date());
        redisService.objectSetObject(tokenKey, onlineUserDto, renew);

        /**
         // 判断是否续期token,计算token的过期时间
         long time = redisService.getExpire(tokenKey);
         Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
         // 判断当前时间与过期时间的时间差
         long differ = expireDate.getTime() - System.currentTimeMillis();
         // 如果在续期检查的范围内，则续期
         if (differ <= securityJwtProperties.getDetect()) {
         long renew = securityJwtProperties.getTokenValidityInMilliSeconds();
         redisService.setExpire(tokenKey, renew);
         }
         */
    }

    /**
     * <p>
     * 获取在线用户
     * </p>
     *
     * @param token /
     * @return /
     */
    private OnlineUserDTO getOnlineUser(String token) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String tokenKey = securityJwtProperties.getOnlineKey() + token;
        return redisService.objectGetObject(tokenKey, OnlineUserDTO.class);
    }
}