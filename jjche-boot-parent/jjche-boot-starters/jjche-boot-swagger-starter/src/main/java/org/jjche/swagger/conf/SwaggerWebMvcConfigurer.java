package org.jjche.swagger.conf;

import org.jjche.common.constant.SwaggerConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 屏蔽原始地址
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class SwaggerWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * {@inheritDoc}
     *
     * <p>
     * 解决spring security冲突 404
     * </p>
     *
     * @author miaoyj
     * @since 2020-07-09
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(SwaggerConstant.SWAGGER_2_URL_PREFIX + "/**")
                .addResourceLocations("classpath:/META-INF/resources/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * 屏蔽原始地址
     * </p>
     *
     * @author miaoyj
     * @since 2020-07-09
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addStatusController("swagger-ui.html", HttpStatus.NOT_FOUND);
        registry.addStatusController("doc.html", HttpStatus.NOT_FOUND);
    }
}
