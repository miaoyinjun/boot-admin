package org.jjche.cat.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 业务指标统计
 * 比如实时商品订下单量的统计、支付订单的统计
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-19
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CatMetricForCount {

    /**
     * 统计的名称
     */
    String value();

    /**
     * 统计次数，默认为1。
     */
    int count() default 1;
}
