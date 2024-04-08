package org.jjche.cat.property;

import lombok.Data;
import org.jjche.cat.constant.CatConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = CatConstant.JJCHE_CAT_PROPERTY_PREFIX)
@Data
public class JjcheCatProperties {
    /**
     * 服务器列表,格式为ip:httpPort:port,如127.0.0.1:8080:2080;
     */
    private String servers;
    private Boolean enabled = true;
}
