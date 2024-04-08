package org.jjche.filter.enc.field.enums;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * <p>
 * 安全字段注解，实现自动加密解密
 * 加在需要加密/解密的方法上
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-08-25
 */
@Documented
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface EncryptMethod {
    /**
     * <p>
     * 是否加密返回数据
     * </p>
     *
     * @return /
     */
    boolean returnVal() default false;
}
