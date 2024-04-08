package org.jjche.common.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import org.jjche.common.annotation.JacksonAllowNull;
import org.jjche.common.constant.PackageConstant;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * <p>
 * api工具类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-14
 */
public class ApiUtil {
    /**
     * <p>
     * 获取APIDefineField注解内的API地址
     * </p>
     *
     * @return API地址
     * @author miaoyj
     * @since 2020-07-10
     */
    public static String getApiDefineField() {
        String pkgName = PackageConstant.BASE_PATH_STAR;
        pkgName = StrUtil.replace(pkgName, ".", "/");
        String packageSearchPath = "classpath*:{}/*.class";
        packageSearchPath = StrUtil.format(packageSearchPath, pkgName);
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
            if (ArrayUtil.isNotEmpty(resources)) {
                for (Resource resource : resources) {
                    String path = StrUtil.subAfter(resource.getFile().getPath(), "/classes/", true);
                    String className = StrUtil.replace(path, "/", ".");
                    className = StrUtil.removeSuffix(className, FileUtil.CLASS_EXT);
                    Class swaggerClass = ClassLoaderUtil.loadClass(className);
                    String value = AnnotationUtil.getAnnotationValue(swaggerClass, JacksonAllowNull.class);
                    if (StrUtil.isNotBlank(value)) {
                        return value;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
