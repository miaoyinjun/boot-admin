package org.jjche.common.constant;

/**
 * <p>
 * 包定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public interface PackageConstant {
    /**
     * base包路径 {@value}
     */
    String BASE_PATH = "org.jjche";

    /**
     * base包路径，通配符 {@value}
     */
    String DOT_STAR = ".*";

    /**
     * base包路径，通配符 {@value}
     */
    String MODULES_STAR = ".modules" + DOT_STAR;

    /**
     * base包路径，通配符 {@value}
     */
    String SERVICE_STAR = ".service";

    /**
     * base包路径，通配符 {@value}
     */
    String BASE_PATH_STAR = BASE_PATH + DOT_STAR;

    /**
     * base包路径，通配符 {@value}
     */
    String BASE_PATH_MODULES_STAR = BASE_PATH_STAR + MODULES_STAR;

    /**
     * controller包路径，通配符 {@value}
     */
    String CONTROLLER_PATH_STAR = BASE_PATH_MODULES_STAR + ".rest";

    /**
     * mapper包路径，通配符 {@value}
     */
    String MAPPER_PATH_STAR = BASE_PATH_MODULES_STAR + ".mapper";

    /**
     * service包路径，通配符 {@value}
     */
    String SERVICE_PATH_STAR = BASE_PATH_MODULES_STAR + SERVICE_STAR;

}
