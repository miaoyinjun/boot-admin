package org.jjche.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.context.ContextUtil;
import org.jjche.common.exception.enums.GlobalErrorCodeEnum;
import org.jjche.common.vo.DataScopeVO;
import org.jjche.common.util.StrUtil;
import org.jjche.core.exception.AuthenticationTokenExpiredException;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * 获取当前登录的用户
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-17
 */
public class SecurityUtil {
    /**
     * 默认系统用户名 {@value}
     */
    static String DEFAULT_SYSTEM_USER_NAME = "System";

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getUsername() {
        String value = ContextUtil.getUsername();
        if (StrUtil.isNotBlank(value)) {
            return value;
        }
        throw new AuthenticationTokenExpiredException(GlobalErrorCodeEnum.TOKEN_EXPIRED.getMsg());
    }

    /**
     * <p>
     * 获取系统用户或默认默认名称
     * </p>
     * <p>
     * 用户定时任务，或者无Token调用的情况
     * </p>
     *
     * @return 用户名
     * @author miaoyj
     * @since 2020-12-17
     */
    public static String getUsernameOrDefaultUsername() {
        try {
            // 这里应根据实际业务情况获取具体信息
            return getUsername();
        } catch (Exception ignored) {
        }
        // 用户定时任务，或者无Token调用的情况
        return DEFAULT_SYSTEM_USER_NAME;
    }

    /**
     * 获取系统用户ID
     *
     * @return 系统用户ID
     */
    public static Long getUserId() {
        Long value = ContextUtil.getUserId();
        if (value != null && value > 0) {
            return value;
        }
        throw new AuthenticationTokenExpiredException(GlobalErrorCodeEnum.TOKEN_EXPIRED.getMsg());
    }

    /**
     * 获取当前用户的数据权限
     *
     * @return /
     */
    public static DataScopeVO getUserDataScope() {
        DataScopeVO dataScopeVO = new DataScopeVO();
        Set<Long> deptIds = ContextUtil.getDataScopeDeptIds();
        boolean isAll = ContextUtil.getDataScopeIsAll();
        boolean isSelf = ContextUtil.getDataScopeIsSelf();
        Long userid = ContextUtil.getDataScopeUserId();
        String username = ContextUtil.getDataScopeUserName();

        dataScopeVO.setDeptIds(deptIds);
        dataScopeVO.setAll(isAll);
        dataScopeVO.setAll(isSelf);
        dataScopeVO.setUserId(userid);
        dataScopeVO.setUserName(username);
        return dataScopeVO;
    }

    /**
     * <p>
     * 是否admin
     * </p>
     *
     * @return 是否admin
     */
    public static Boolean isAdmin() {
        Set<String> permissions = listPermission();
        return permissions.contains(SecurityConstant.ADMIN_PERMISSION);
    }

    /**
     * <p>
     * 获取权限列表
     * </p>
     *
     * @return /
     */
    public static Set<String> listPermission() {
        Set<String> permissions = ContextUtil.getPermissions();
        if (CollUtil.isEmpty(permissions)) {
            throw new AuthenticationTokenExpiredException(GlobalErrorCodeEnum.TOKEN_EXPIRED.getMsg());
        }
        return permissions;
    }

    /**
     * <p>
     * 获取token
     * </p>
     *
     * @author miaoyj
     * @since 2022-06-22
     */
    public static String getToken() {
        String bearerToken = ContextUtil.getToken();
        String tokenStartWith = SecurityConstant.TOKEN_START_WITH;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenStartWith)) {
            // 去掉令牌前缀
            return bearerToken.replace(tokenStartWith, "");
        } else {
            StaticLog.warn("非法Token：{}", bearerToken);
        }
        return null;
    }

}
