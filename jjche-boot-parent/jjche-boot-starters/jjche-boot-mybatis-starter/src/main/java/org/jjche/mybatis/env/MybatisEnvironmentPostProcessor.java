package org.jjche.mybatis.env;

import org.jjche.common.yml.CoreEnvironmentPostProcessor;
import org.springframework.core.annotation.Order;

/**
 * <p>
 * 自定义加载yml类
 * </p>
 * <p>
 *  FlowableLiquibaseEnvironmentPostProcessor之前执行
 * </p>
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Order(99)
public class MybatisEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for MybatisEnvironmentPostProcessor</p>
     */
    public MybatisEnvironmentPostProcessor() {
        super.setYmlName("mybatis.yml");
        super.setExtYmlName("mybatis-sql.yml", "jjche.mybatis.is-print-sql");
    }
}
