package org.jjche.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.jjche.common.enums.InfraErrorCodeEnum;
import org.jjche.common.exception.util.AssertUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Application工具
 * </p>
 *
 * @author miaoyj
 * @since 2024-04-01
 */
public class AppUtil {

    /**
     * App包名
     */
    private static String APP_PACKAGE_NAME = null;

    /**
     * <p>
     * 获取Application的包名
     * </p>
     *
     * @return /
     */
    public static String getAppPackageName() {
        if (StrUtil.isBlank(APP_PACKAGE_NAME)) {
            Package pack = getApplicationClass().getPackage();
            APP_PACKAGE_NAME = pack.getName();
        }
        return APP_PACKAGE_NAME;
    }

    /**
     * <p>
     * 获取Application的包名前缀
     * </p>
     *
     * @return /
     */
    public static String getAppPackageNamePrefix() {
        List<String> packageList = StrUtil.split(getAppPackageName(), StrUtil.C_DOT);
        return CollUtil.get(packageList, 2);
    }

    /**
     * <p>
     * 获取Application的class所在位置
     * </p>
     *
     * @return /
     */
    public static Class getApplicationClass() {
        Map<String, Object> map = SpringUtil.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class);
        AssertUtil.notEmpty(map, InfraErrorCodeEnum.COMMON_APP_NOT_FOUND_ERROR);
        Object application = null;
        for (String key : map.keySet()) {
            application = map.get(key);
            break;
        }
        return application.getClass();
    }
}
