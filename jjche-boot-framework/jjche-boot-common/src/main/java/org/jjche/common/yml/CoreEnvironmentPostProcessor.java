package org.jjche.common.yml;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>
 * 自定义加载yml类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class CoreEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final String YML_EXT = ".yml";
    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
    private String ymlName;
    private String extYmlName;
    private String extPropertyNameIsLoad;

    /**
     * <p>
     * 设置要加载的yml名称
     * </p>
     *
     * @param ymlName yml名称
     */
    public void setYmlName(String ymlName) {
        this.ymlName = ymlName;
    }

    /**
     * <p>
     * 设置要扩展加载的yml名称
     * </p>
     *
     * @param extYmlName            yml名称
     * @param extPropertyNameIsLoad 配置属性是否加载
     */
    public void setExtYmlName(String extYmlName, String extPropertyNameIsLoad) {
        this.extYmlName = extYmlName;
        this.extPropertyNameIsLoad = extPropertyNameIsLoad;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        if (StrUtil.isNotBlank(ymlName)) {
            Resource path = new ClassPathResource(ymlName);
            if (path.exists()) {
                PropertySource<?> propertySource = loadYaml(path);
                propertySources.addLast(propertySource);

                String activeProfile = SpringUtil.getActiveProfile();
                if (StrUtil.isNotBlank(activeProfile)) {
                    String ymPath = StrUtil.removeSuffix(ymlName, YML_EXT);
                    path = new ClassPathResource(ymPath + "-" + activeProfile + YML_EXT);
                    if (path.exists()) {
                        //addFirst会覆盖相同的属性定义
                        propertySources.addFirst(loadYaml(path));
                    }
                }
            }
        }
        if (StrUtil.isNotBlank(extPropertyNameIsLoad)) {
            ArrayList<PropertySource<?>> propertySourceList = CollUtil.newArrayList(propertySources.iterator());
            Boolean isLoad = null;
            for (PropertySource<?> propertySource : propertySourceList) {
                if (StrUtil.equals(ymlName, propertySource.getName())) {
                    isLoad = BooleanUtil.toBoolean(environment.getProperty(extPropertyNameIsLoad));
                    break;
                }
            }
            if (BooleanUtil.isTrue(isLoad) && StrUtil.isNotBlank(extYmlName)) {
                Resource path = new ClassPathResource(extYmlName);
                if (path.exists()) {
                    PropertySource<?> propertySource = loadYaml(path);
                    propertySources.addFirst(propertySource);
                }
            }
        }
    }

    private PropertySource<?> loadYaml(Resource path) {
        if (!path.exists()) {
            throw new IllegalArgumentException("Resource " + path + " does not exist");
        }
        try {
            return this.loader.load(path.getFilename(), path).get(0);
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Failed to load yaml configuration from " + path, ex);
        }
    }
}
