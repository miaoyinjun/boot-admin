package org.jjche.common.api;

import org.jjche.common.dto.PermissionDataResourceDTO;
import org.jjche.common.dto.PermissionDataRuleDTO;
import org.jjche.common.vo.DataPermissionFieldResultVO;

import java.util.List;

/**
 * <p>
 * 数量权限字段
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-04
 */
public interface CommonDataPermissionFieldApi {
    /**
     * <p>
     * 执行过滤
     * </p>
     *
     * @param dto /
     * @return 结果
     */
    List<DataPermissionFieldResultVO> listPermissionDataResource(PermissionDataResourceDTO dto);

    /**
     * <p>
     * 根据用户id查询
     * </p>
     *
     * @param userId 用户id
     * @return /
     */
    List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(Long userId);
}
