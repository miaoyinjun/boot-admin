package org.jjche.system.modules.permission.api;

import org.jjche.common.api.CommonAppKeyApi;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.system.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 应用密钥
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-04
 */
@Component
@FeignClient(contextId = ApiConstants.NAME + "-appKey-api",
        name = ApiConstants.NAME,
        path = "/sys/security_app_keys/",
        url = "${FEIGN_URL_SYSTEM:}"
)
public interface SecurityCommonAppKeyApi extends CommonAppKeyApi {
    /**
     * <p>
     * 根据应用id获取密钥
     * </p>
     *
     * @param appId 应用id
     * @return /
     */
    @GetMapping("key-by-app-id")
    SecurityAppKeyBasicVO getKeyByAppId(@RequestParam("appId") String appId);

}
