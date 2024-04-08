package org.jjche.swagger.property.plugin;

import org.springframework.context.annotation.Bean;

/**
 * <p>
 * swagger定义入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class SwaggerConfiguration {

    /**
     * <p>enumModelPropertyBuilderPlugin</p>
     *
     * @return a {@link EnumModelPropertyBuilderPlugin} object.
     */
    @Bean
    public EnumModelPropertyBuilderPlugin enumModelPropertyBuilderPlugin() {
        return new EnumModelPropertyBuilderPlugin();
    }

    /**
     * <p>enumParameterBuilderPlugin</p>
     *
     * @return a {@link EnumParameterBuilderPlugin} object.
     */
    @Bean
    public EnumParameterBuilderPlugin enumParameterBuilderPlugin() {
        return new EnumParameterBuilderPlugin();
    }
}
