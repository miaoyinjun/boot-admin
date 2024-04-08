package org.jjche.swagger;

import org.jjche.swagger.conf.*;
import org.jjche.swagger.property.plugin.EnumWebMvcConfiguration;
import org.jjche.swagger.property.plugin.SwaggerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Import({SwaggerProperties.class, SwaggerConfig.class,
        SwaggerWebMvcConfigurer.class, SwaggerController.class,
        BeanValidatorPluginsConfiguration.class,
        CustomizeModelAttributeParameterExpander.class,
        SwaggerConfiguration.class, EnumWebMvcConfiguration.class
})
@Configuration
@EnableOpenApi
public class SwaggerAutoConfiguration {
}