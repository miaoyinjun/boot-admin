package org.jjche.filter.property;

import lombok.Data;
import org.jjche.common.constant.FilterEncConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 加密属性配置加载类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-10
 */
@Data
@Component
@ConfigurationProperties(FilterEncConstant.PROPERTY_ENC_API_FILTER)
public class FilterEncProperties {
    /**
     * 过滤的地址如/test
     */
    private List<String> urls;
}
