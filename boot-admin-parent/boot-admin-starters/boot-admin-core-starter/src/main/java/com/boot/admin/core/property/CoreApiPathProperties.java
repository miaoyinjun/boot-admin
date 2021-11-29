package com.boot.admin.core.property;

import lombok.Data;

/**
 * <p>
 * api前缀配置
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-21
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class CoreApiPathProperties {
    String globalPrefix;
    String sysPrefix;
}
