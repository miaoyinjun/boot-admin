package org.jjche.swagger.conf;

import lombok.Data;
import org.jjche.common.constant.PackageConstant;
import org.jjche.swagger.property.SwaggerSecurityJwtProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;

/**
 * <p>
 * swagger属性加载类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
@ConfigurationProperties(prefix = "jjche.swagger")
public class SwaggerProperties {
    /**
     * 安全认证配置
     */
    SwaggerSecurityJwtProperties securityJwt;
    private String filterPath;
    private String basePackage = PackageConstant.CONTROLLER_PATH_STAR;
    private String title;
    private String description;
    private String version;
    private String contactName;
    private String contactUrl;
    private String contactEmail;
    private Contact contact;
}
