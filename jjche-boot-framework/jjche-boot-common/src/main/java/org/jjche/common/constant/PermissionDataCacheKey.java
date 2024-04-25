package org.jjche.common.constant;

/**
 * <p>
 * 数据权限缓存
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-06
 */
public interface PermissionDataCacheKey {
    /**
     * 数据规则，用户id
     */
    String PERMISSION_DATA_RULE_USER_ID = "permission_data:rule:userid:";

    /**
     * 数据字段，用户id
     */
    String PERMISSION_DATA_FIELD_USER_ID = "permission_data:field:userid:";
}
