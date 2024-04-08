package org.jjche.swagger.conf;

import org.jjche.common.constant.SwaggerConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 重新定义地址
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Controller
@RequestMapping(SwaggerConstant.SWAGGER_2_URL_PREFIX)
public class SwaggerController {


    /**
     * <p>resource</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/swagger-resources")
    public String resource() {
        return "forward:/swagger-resources";
    }

    /**
     * <p>ui</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/swagger-resources/configuration/ui")
    public String ui() {
        return "forward:/swagger-resources/configuration/ui";
    }

    /**
     * <p>doc</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping(SwaggerConstant.SWAGGER_2_URL_SUFFIX)
    public String doc() {
        return "forward:" + SwaggerConstant.SWAGGER_2_URL_SUFFIX;
    }

    /**
     * <p>docExt</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping(SwaggerConstant.SWAGGER_2_VERSION + "/api-docs-ext")
    public String docExt() {
        return "forward:" + SwaggerConstant.SWAGGER_2_VERSION + "/api-docs-ext";
    }

    /**
     * <p>security</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/swagger-resources/configuration/security")
    public String ssecurity() {
        return "forward:/swagger-resources/configuration/security";
    }
}
