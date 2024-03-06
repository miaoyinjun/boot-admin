package org.jjche.system.modules.permission.api;

import org.jjche.common.api.CommonDataPermissionFieldApi;
import org.jjche.common.dto.PermissionDataResourceDTO;
import org.jjche.common.dto.PermissionDataRuleDTO;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.system.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 数量权限字段
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-04
 */
@Component
@FeignClient(contextId = ApiConstants.NAME + "-dataPermissionField-api",
        name = ApiConstants.NAME,
        path = "/sys/data_permission_fields/",
        url = "${FEIGN_URL_SYSTEM:}"
)
public interface DataPermissionFieldApi extends CommonDataPermissionFieldApi {

    /**
     * 查询数据权限字段
     *
     * @param dto 查询条件
     * @return 查询结果
     */
    @Override
    @PostMapping("list-permission-data-resource")
    List<DataPermissionFieldResultVO> listPermissionDataResource(@RequestBody PermissionDataResourceDTO dto);

    @GetMapping("list-permission-data-ruleByUserId")
    List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(@RequestParam Long userId);
}
