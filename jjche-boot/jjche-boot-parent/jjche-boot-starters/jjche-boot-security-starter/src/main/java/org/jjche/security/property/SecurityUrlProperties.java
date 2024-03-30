package org.jjche.security.property;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 地址相关
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-18
 */
@Data
public class SecurityUrlProperties {
    /**
     * 不过滤的url如 - /test/** -/demo/
     */
    private List<String> excludeUrls;
    /**
     * 默认不过滤的url如 - /test/** -/demo/
     */
    private List<String> excludeDefaultUrls;
}
