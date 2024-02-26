package org.jjche.system.env;


import org.jjche.common.yml.CoreEnvironmentPostProcessor;
import org.springframework.core.annotation.Order;

/**
 * <p>
 * 自定义加载yml类
 * </p>
 * <p>
 *  MybatisEnvironmentPostProcessor之前执行
 * </p>
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Order(98)
public class AdminEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for SwaggerEnvironmentPostProcessor</p>
     */
    public AdminEnvironmentPostProcessor() {
        super.setYmlName("admin.yml");
    }
}
