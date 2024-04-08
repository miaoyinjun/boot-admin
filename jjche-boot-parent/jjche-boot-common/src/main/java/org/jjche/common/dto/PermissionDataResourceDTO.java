package org.jjche.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * <p>
 * 数据权限传输
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDataResourceDTO {
    private String permission;
    private Map<String, String> voMap;
    private Boolean filter;
}
