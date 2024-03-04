package org.jjche.common.system.api;

import org.jjche.common.api.CommonAPI;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.*;
import org.jjche.common.system.api.constant.ApiConstants;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * cloud接口
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@Component
@FeignClient(contextId = ApiConstants.NAME + "-base-api",
        name = ApiConstants.NAME,
        path = "/sys/base/")
public interface ISysBaseAPI extends CommonAPI {

    @Override
    @GetMapping(URL_LOGOUT_ONLINE_USER)
    void logoutOnlineUser(@RequestParam("token") String token);

    @Override
    @GetMapping(URL_GET_USER_DETAILS)
    JwtUserDto getUserDetails();

    @Override
    @GetMapping(URL_GET_USER_DETAILS)
    JwtUserDto getUserDetails(@RequestHeader(SecurityConstant.HEADER_AUTH) String token);

    @Override
    @PostMapping(URL_RECORD_LOG)
    void recordLog(@RequestBody LogRecordDTO logRecord);

    @Override
    @PostMapping(URL_RECORD_LOGS)
    void recordLogs(@RequestBody List<LogRecordDTO> list);

    @Override
    @GetMapping(URL_LIST_PERMISSION_DATA_RULE_BY_USER_ID)
    List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(@RequestParam("userId") Long userId);

    @Override
    @GetMapping(URL_GET_KEY_BY_APP_ID)
    SecurityAppKeyBasicVO getKeyByAppId(@RequestParam("appId") String appId);

    @Override
    @PostMapping(URL_LIST_PERMISSION_DATA_RESOURCE)
    List<DataPermissionFieldResultVO> listPermissionDataResource(@RequestBody PermissionDataResourceDTO dto);

    @Override
    @GetMapping(URL_GET_DICT_BY_NAME_VALUE)
    DictParam getDictByNameValue(@RequestParam("name") String name, @RequestParam("value") String value);
}
