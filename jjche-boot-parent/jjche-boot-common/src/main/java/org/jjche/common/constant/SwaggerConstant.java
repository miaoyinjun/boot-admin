package org.jjche.common.constant;

/**
 * <p>
 * 文档定义
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-03
 */
public interface SwaggerConstant {

    /**
     * 文档地址前缀
     */
    String SWAGGER_2_VERSION = "/v2";

    /**
     * 文档地址前缀
     */
    String SWAGGER_2_URL_PREFIX = SbaConstant.SBA_URL_PREFIX + "/api";
    /**
     * 文档地址后缀
     */
    String SWAGGER_2_URL_SUFFIX = SWAGGER_2_VERSION + "/api-docs";
    /**
     * 文档地址
     */
    String SWAGGER_2_URL = SWAGGER_2_URL_PREFIX + SWAGGER_2_URL_SUFFIX;

}
