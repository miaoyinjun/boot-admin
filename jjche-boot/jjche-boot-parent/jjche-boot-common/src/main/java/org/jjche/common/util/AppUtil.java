package org.jjche.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
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
        Assert.isTrue(MapUtil.isNotEmpty(map), "找不到SpringBootApplication注解");
        Object application = null;
        for (String key : map.keySet()) {
            application = map.get(key);
            break;
        }
        return application.getClass();
    }
}
