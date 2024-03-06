package org.jjche.system.modules.permission.api;

import org.jjche.common.api.CommonAuthApi;

/**
 * <p>
 * 认证
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-09
 */
public interface AuthApi extends CommonAuthApi {
    /**
     * <p>
     * 清除在线用户token
     * </p>
     *
     * @param token /
     */
    void logoutOnlineUser(String token);
}
