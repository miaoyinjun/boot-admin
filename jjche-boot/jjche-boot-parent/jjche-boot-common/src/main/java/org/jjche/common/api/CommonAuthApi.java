package org.jjche.common.api;

import org.jjche.common.dto.JwtUserDTO;

/**
 * <p>
 * 认证
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-05
 */
public interface CommonAuthApi {
    /**
     * <p>
     * 获取认证
     * </p>
     *
     * @return /
     */
    JwtUserDTO getUserDetails();

    /**
     * <p>
     * 根据参数token获取认证信息
     * </p>
     *
     * @param token /
     * @return /
     */
    JwtUserDTO getUserDetails(String token);
}
