package org.jjche.system.modules.system.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jjche.common.api.CommonAPI;
import org.jjche.common.dto.*;
import org.jjche.common.system.api.ISysBaseAPI;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.security.annotation.rest.IgnoreGetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 系统基础
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@RequiredArgsConstructor
@Api(tags = "系统：基础")
@SysRestController("base")
public class SysBaseController extends BaseController {

    private final ISysBaseAPI sysBaseAPI;

    @GetMapping(CommonAPI.URL_LOGOUT_ONLINE_USER)
    public void logoutOnlineUser(String token) {
        this.sysBaseAPI.logoutOnlineUser(token);
    }

    @IgnoreGetMapping(CommonAPI.URL_GET_USER_DETAILS)
    public JwtUserDto getUserDetails() {
        return this.sysBaseAPI.getUserDetails();
    }

    @PostMapping(CommonAPI.URL_RECORD_LOG)
    public void recordLog(@RequestBody LogRecordDTO logRecord) {
        this.sysBaseAPI.recordLog(logRecord);
    }

    @PostMapping(CommonAPI.URL_RECORD_LOGS)
    public void recordLogs(@RequestBody List<LogRecordDTO> list) {
        this.sysBaseAPI.recordLogs(list);
    }

    @GetMapping(CommonAPI.URL_LIST_PERMISSION_DATA_RULE_BY_USER_ID)
    public List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(@RequestParam("userId") Long userId) {
        return sysBaseAPI.listPermissionDataRuleByUserId(userId);
    }

    @GetMapping(CommonAPI.URL_GET_KEY_BY_APP_ID)
    public SecurityAppKeyBasicVO getKeyByAppId(@RequestParam("appId") String appId) {
        return sysBaseAPI.getKeyByAppId(appId);
    }

    @PostMapping(CommonAPI.URL_LIST_PERMISSION_DATA_RESOURCE)
    public List<DataPermissionFieldResultVO> listPermissionDataResource(@RequestBody PermissionDataResourceDTO dto) {
        return sysBaseAPI.listPermissionDataResource(dto);
    }

    @GetMapping(CommonAPI.URL_GET_DICT_BY_NAME_VALUE)
    public DictParam getDictByNameValue(String name, String value) {
        return sysBaseAPI.getDictByNameValue(name, value);
    }
}