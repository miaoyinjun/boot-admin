package org.jjche.system.modules.permission.api;

import org.jjche.common.api.CommonAuthApi;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.JwtUserDTO;
import org.jjche.system.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * cloud接口
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@Component
@FeignClient(contextId = ApiConstants.NAME + "-auth-api",
        name = ApiConstants.NAME,
        path = "/sys/auth/")
public interface AuthApi extends CommonAuthApi {

    /**
     * <p>
     * 清除在线用户token
     * </p>
     *
     * @param token /
     */
    @GetMapping("logout-token")
    void logoutOnlineUser(@RequestParam("token") String token);

    /**
     * <p>
     * 获取认证
     * </p>
     *
     * @return /
     */
    @GetMapping("user-details")
    JwtUserDTO getUserDetails();

    /**
     * <p>
     * 根据参数token获取认证信息
     * </p>
     *
     * @param token /
     * @return /
     */
    @GetMapping("user-details")
    JwtUserDTO getUserDetails(@RequestHeader(SecurityConstant.HEADER_AUTH) String token);
}
