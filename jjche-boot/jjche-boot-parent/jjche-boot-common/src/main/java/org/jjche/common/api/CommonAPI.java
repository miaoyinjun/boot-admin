package org.jjche.common.api;

import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.LogRecordDTO;

public interface CommonAPI {

    /**
     * <p>
     * 清除在线用户token
     * </p>
     *
     * @param token /
     */
    void logoutOnlineUser(String token);

    /**
     * <p>
     * 获取认证
     * </p>
     *
     * @return /
     */
    JwtUserDto getUserDetails();

    void recordLog(LogRecordDTO logRecord);
}