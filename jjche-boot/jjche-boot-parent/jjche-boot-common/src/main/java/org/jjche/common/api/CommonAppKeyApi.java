package org.jjche.common.api;

import org.jjche.common.vo.SecurityAppKeyBasicVO;

/**
 * <p>
 * 应用密钥
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-04
 */
public interface CommonAppKeyApi {
    /**
     * <p>
     * 根据应用id获取密钥
     * </p>
     *
     * @param appId 应用id
     * @return /
     */
    SecurityAppKeyBasicVO getKeyByAppId(String appId);
}
