package org.jjche.common.api;

import org.jjche.common.dto.*;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;

import java.util.List;

public interface CommonApi {
    /**
     * <p>
     * 根据应用id获取密钥
     * </p>
     *
     * @param appId 应用id
     * @return /
     */
    SecurityAppKeyBasicVO getAppKeyByAppId(String appId);

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
     * 根据用户id查询权限
     * </p>
     *
     * @param userId 用户id
     * @return /
     */
    List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(Long userId);

    /**
     * <p>
     * 获取根据字典名称和值
     * </p>
     *
     * @param name  字典名称
     * @param value 字典值
     * @return /
     */
    DictParam getDictByNameValue(String name, String value);

    /**
     * <p>
     * 记录日志
     * </p>
     *
     * @param logRecord /
     */
    void recordLog(LogRecordDTO logRecord);
}
