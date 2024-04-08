package org.jjche.filter.enc.api.enums;

import java.lang.annotation.*;

/**
 * <p>
 * data加密注解
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpResDataEncrypt {
}
